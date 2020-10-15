/*******************************************************************************
 * Copyright (C) IceyLeagons(https://iceyleagons.net/) 
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iceyleagons.frostedengineering.common.textures.interfaces;

import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.parser.HTMLParserListener;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;
import lombok.SneakyThrows;
import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.common.textures.BlockStorage;
import net.iceyleagons.frostedengineering.common.textures.Textures;
import net.iceyleagons.frostedengineering.common.textures.base.TexturedBase;
import net.iceyleagons.frostedengineering.common.textures.base.TexturedInterconnectible;
import net.iceyleagons.frostedengineering.common.textures.events.TextureSetupEvent;
import net.iceyleagons.frostedengineering.common.textures.initialization.McMeta;
import net.iceyleagons.frostedengineering.common.textures.initialization.Sounds;
import net.iceyleagons.frostedengineering.common.textures.initialization.Sounds.SoundData;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.time.Duration;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public interface IUploadable {

    void init();

    String[] keywords();

    default boolean needReupload() {
        return false;
    }

    default Duration reuploadIntervals() {
        return Duration.ZERO;
    }

    default void common(net.iceyleagons.frostedengineering.common.textures.initialization.Function<File, byte[], String> fileFunction) {
        try {
            printData();

            File resourcepacksFolder = createFolder(new File(Textures.homeFolder, "resourcepacks"));
            File finalResourcesFolder = createFolder(new File(resourcepacksFolder, "final_resources"));

            File assetsFolder = new File(finalResourcesFolder, "assets");

            // We get the assets.zip files and extract them into the final_resources folder.
            Textures.plugins.forEach((plugin) -> {
                File pluginFolder = new File(resourcepacksFolder, plugin.getName());
                deleteFile(pluginFolder);
                createFolder(pluginFolder);

                File assets = extractFile(plugin, "assets.zip", new File(pluginFolder, "assets.zip"));

                unzipFile(assets, pluginFolder);
                deleteFile(assets);

                try {
                    FileUtils.copyDirectory(pluginFolder, assetsFolder);
                } catch (IOException exception) {
                    Main.error(Optional.of("Textures"), exception);
                }
            });

            // We write our custom x.json file.
            File itemModelsFolder = createFolder(new File(finalResourcesFolder, "assets/minecraft/models/item"));
            File blockModelsFolder = createFolder(new File(finalResourcesFolder, "assets/minecraft/models/block"));
            Textures.usedMaterials.forEach(material -> {
                try {
                    FileOutputStream fOS = new FileOutputStream(
                            new File(material.isBlock() ? blockModelsFolder : itemModelsFolder,
                                    material.name().toLowerCase() + ".json"));
                    PrintWriter printWriter = new PrintWriter(fOS);
                    printModelData(printWriter, material);

                    fOS.close();
                } catch (IOException exception) {
                    Main.error(Optional.of("Textures"), exception);
                }
            });

            Plugin plugin = Main.MAIN;
            if (Textures.USE_PACK_IMAGE)
                plugin.saveResource("pack.png", true);

            plugin.saveResource("mob_spawner.png", true);

            deleteFile(new File(finalResourcesFolder, "pack.mcmeta"));
            if (Textures.USE_PACK_IMAGE)
                deleteFile(new File(finalResourcesFolder, "pack.png"));

            File blocksFolder = createFolder(new File(finalResourcesFolder, "assets/minecraft/textures/block"));
            File minecraftFolder = createFolder(new File(finalResourcesFolder, "assets/minecraft"));
            File soundsFile = new File(minecraftFolder, "sounds.json");
            File mobSpawnerFile = new File(blocksFolder, "spawner.png");
            deleteFile(mobSpawnerFile);
            deleteFile(soundsFile);

            File oldResourcePack = new File(Textures.homeFolder, "old-resourcepack.zip");
            File finalResourcePack = new File(Textures.homeFolder, "final-resourcepack.zip");

            try {
                writeMcMeta(new File(finalResourcesFolder, "pack.mcmeta"));
                writeSounds(soundsFile);
                if (Textures.USE_PACK_IMAGE)
                    FileUtils.moveFile(new File(Textures.mainFolder, "pack.png"),
                            new File(finalResourcesFolder, "pack.png"));

                FileUtils.moveFile(new File(Textures.mainFolder, "mob_spawner.png"), mobSpawnerFile);
                FileUtils.copyFile(mobSpawnerFile, new File(blocksFolder, "mob_spawner.png"));

                deleteFile(oldResourcePack);

                if (finalResourcePack.exists())
                    FileUtils.copyFile(finalResourcePack, oldResourcePack);
            } catch (IOException exception) {
                Main.error(Optional.of("Textures"), exception);
            }

            if (!Textures.USE_PACK_IMAGE)
                download(Textures.PACK_IMAGE_LINK, new File(finalResourcesFolder, "pack.png"));

            deleteFile(finalResourcePack);

            try {
                ZipFile zip = new ZipFile(finalResourcePack);
                ZipParameters params = new ZipParameters();
                params.setOverrideExistingFilesInZip(true);
                params.setCompressionLevel(CompressionLevel.MAXIMUM);
                params.setCompressionMethod(CompressionMethod.STORE);

                zip.addFolder(new File(finalResourcesFolder, "assets"), params);
                zip.addFile(new File(finalResourcesFolder, "pack.mcmeta"), params);
                zip.addFile(new File(finalResourcesFolder, "pack.png"), params);

                zip.getFile();
            } catch (ZipException exception) {
                exception.printStackTrace();
            }

            new Timer().schedule(new TimerTask() {
                @SneakyThrows
                @Override
                public void run() {

                    String stringHash = Textures.getData("resourcepack-hash");
                    byte[] currentHash = sha1Code(finalResourcePack);
                    String stringCurrentHash = bytesToHexString(currentHash);
                    if (stringHash != null) {
                        if (!stringHash.equalsIgnoreCase(stringCurrentHash))
                            fileFunction.run(finalResourcePack, currentHash, stringCurrentHash);
                    } else
                        fileFunction.run(finalResourcePack, currentHash, stringCurrentHash);
                    // Compare the old and the new resourcepack to check if there's any changes.

                    Textures.plugins.forEach((pl) -> {
                        File pluginFolder = new File(resourcepacksFolder, pl.getName());
                        deleteFile(pluginFolder);
                    });

                    deleteFile(oldResourcePack);
                }
            }, 10000L);

        } catch (Exception uncatchedException) {
            // This is bad. We have an uncatched exception.
            Main.error(Optional.of("Textures"), uncatchedException);
        } finally {
            // Create block storages for all the registered worlds.
            Bukkit.getWorlds().forEach((world) -> {
                File blocksList = new File(world.getWorldFolder(), "block.map");

                if (blocksList.exists()) {
                    try {
                        FileInputStream fIS = new FileInputStream(blocksList);
                        ObjectInputStream oIS = new ObjectInputStream(fIS);

                        Textures.storageMap.put(world, new BlockStorage(world,
                                (Map<Map<String, Object>, Map<String, Object>>) oIS.readObject()));

                        oIS.close();
                        fIS.close();
                    } catch (IOException | ClassNotFoundException exception) {
                        Main.error(Optional.of("Textures"), exception);
                    }
                } else {
                    Textures.storageMap.put(world, new BlockStorage(world));
                }
            });

            throwEvent();

            if (needReupload())
                newTask(fileFunction);
        }
    }

    default void newTask(net.iceyleagons.frostedengineering.common.textures.initialization.Function<File, byte[], String> fileFunction) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                fileFunction.run(new File(Textures.homeFolder, "final-resourcepack.zip"), Textures.hash, Textures.getData("resourcepack-hash"));
                newTask(fileFunction);
            }
        }, reuploadIntervals().toMillis());
    }

    default WebElement fluentWait(final By locator, final WebDriver driver) {
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class);

        return wait.until(driver1 -> driver1.findElement(locator));
    }

    default WebClient modify(final WebClient client) {
        client.getOptions().setThrowExceptionOnScriptError(false);
        client.getCookieManager().setCookiesEnabled(true);
        client.setRefreshHandler(new ThreadedRefreshHandler());
        client.getOptions().setDownloadImages(false);
        client.getOptions().setCssEnabled(false);
        client.getOptions().setUseInsecureSSL(true);
        client.getOptions().setRedirectEnabled(true);
        client.getOptions().setThrowExceptionOnFailingStatusCode(false);

        client.setIncorrectnessListener((arg0, arg1) -> {
            // Ignored.
        });
        client.setJavaScriptErrorListener(new JavaScriptErrorListener() {

            @Override
            public void timeoutError(HtmlPage arg0, long arg1, long arg2) {
                // Ignored.
            }

            @Override
            public void scriptException(HtmlPage arg0, ScriptException arg1) {
                // Ignored.
            }

            @Override
            public void malformedScriptURL(HtmlPage arg0, String arg1, MalformedURLException arg2) {
                // Ignored.
            }

            @Override
            public void loadScriptError(HtmlPage arg0, URL arg1, Exception arg2) {
                // Ignored.
            }

            @Override
            public void warn(String arg0, String arg1, int arg2, String arg3, int arg4) {
                // Ignored.
            }
        });
        client.setHTMLParserListener(new HTMLParserListener() {

            @Override
            public void error(String arg0, URL arg1, String arg2, int arg3, int arg4, String arg5) {
                // Ignored.
            }

            @Override
            public void warning(String arg0, URL arg1, String arg2, int arg3, int arg4, String arg5) {
                // Ignored.
            }
        });

        return client;
    }

    @SneakyThrows
    default byte[] sha1Code(File file) {
        FileInputStream fileInputStream = new FileInputStream(file);
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        DigestInputStream digestInputStream = new DigestInputStream(fileInputStream, digest);
        byte[] bytes = new byte[1024];

        while (digestInputStream.read(bytes) > 0) ;

        return digest.digest();
    }

    default String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            int value = b & 0xFF;
            if (value < 16)
                sb.append("0");
            sb.append(Integer.toHexString(value).toUpperCase());
        }
        return sb.toString();
    }

    default void unzipFile(File assets, File where) {
        try {
            ZipInputStream zipIn = new ZipInputStream(new FileInputStream(assets));
            ZipEntry entry = zipIn.getNextEntry();
            while (entry != null) {
                Main.debug("Extracting entry " + entry.getName() + " from the zip file " + assets.getName());
                if (!entry.isDirectory())
                    extractEntry(zipIn, new File(where, entry.getName()));
                else
                    createFolder(new File(where, entry.getName()));
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
            zipIn.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    default void extractEntry(ZipInputStream zipIn, File file) throws IOException {
        FileOutputStream fOS = new FileOutputStream(file);
        byte[] bytesIn = new byte[8192];
        int read;
        while ((read = zipIn.read(bytesIn)) != -1)
            fOS.write(bytesIn, 0, read);

        fOS.close();
    }

    default void printData() {
        if (Textures.items.size() != 0)
            Main.info(Optional.of("Textures"), "Registered " + Textures.items.size() + " textured item(s).");
        if (Textures.blocks.size() != 0)
            Main.info(Optional.of("Textures"), "Registered " + Textures.blocks.size() + " textured block(s).");
        if (Textures.plugins.size() != 0) {
            Main.info(Optional.of("Textures"), "Registered plugins include:");
            for (Plugin plugin : Textures.plugins)
                Main.info(Optional.of("Textures"), plugin.getName());
        }

        Main.info(Optional.of("Textures"),
                "Registered " + (Textures.blocks.size() + Textures.items.size()) + " textured instance(s).");
    }

    default File extractFile(Plugin plugin, String name, File where) {
        Main.info(Optional.of("Textures"), " Retrieving assets.zip out of the plugin \"" + plugin.getName() + "\".");

        try {
            FileOutputStream fOS = new FileOutputStream(where);
            byte[] resourceData = IOUtils.toByteArray(Objects.requireNonNull(plugin.getResource(name)));
            fOS.write(resourceData, 0, resourceData.length);
            fOS.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return where;
    }

    default void deleteFile(File file) {
        if (file.exists())
            try {
                FileUtils.forceDelete(file);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
    }

    default void printModelData(PrintWriter printWriter, Material baseMaterial) {
        List<TexturedBase> texturedList = new ArrayList<>(Textures.items.size() + Textures.blocks.size());

        texturedList.addAll(Textures.blocks);

        texturedList.addAll(Textures.items);

        if (texturedList.size() != 0)
            printWriter.write("{\n\t\"parent\": \"item/handheld\",\n\t\"textures\":\n\t{\n\t\t\"layer0\": \"items/"
                    + baseMaterial.name().toLowerCase()
                    + "\"\n\t},\n\t\"overrides\": [\n\t\t{\n\t\t\t\"predicate\":\n\t\t\t{\n\t\t\t\t\"custom_model_data\": 0\n\t\t\t},\n\t\t\t\"model\": \""
                    + baseMaterial.name().toLowerCase() + "\"\n\t\t}");

        texturedList.forEach(textured -> {
            if (textured instanceof TexturedInterconnectible) {
                ((TexturedInterconnectible) textured).getRegisterMap().forEach((directions, texturedBlock) -> {
                    if (texturedBlock.getBaseMaterial() == baseMaterial) {
                        printWriter.println(",\n\t\t{\n\t\t\t\"predicate\":\n\t\t\t{\n\t\t\t\t\"custom_model_data\": " + texturedBlock.getId()
                                + "\n\t\t\t},\n\t\t\t\"model\": \"" + texturedBlock.getModel() + "\"\n\t\t}");

                        Main.info(Optional.of("TEXTURES"), "Wrote custom JSON data for TexturedBase #" + texturedBlock.getId()
                                + " (" + texturedBlock.getName() + ") with id " + texturedBlock.getId());
                    }
                });
            } else if (textured.getBaseMaterial() == baseMaterial) {
                printWriter.println(",\n\t\t{\n\t\t\t\"predicate\":\n\t\t\t{\n\t\t\t\t\"custom_model_data\": " + textured.getId()
                        + "\n\t\t\t},\n\t\t\t\"model\": \"" + textured.getModel() + "\"\n\t\t}");

                Main.info(Optional.of("TEXTURES"), "Wrote custom JSON data for TexturedBase #" + textured.getId()
                        + " (" + textured.getName() + ") with id " + textured.getId());
            }
        });

        if (texturedList.size() != 0)
            printWriter.println("\n\t]\n}");

        printWriter.close();
    }

    default void writeSounds(File file) throws IOException {
        HashMap<String, SoundData> soundsMap = new HashMap<>();

        Textures.sounds.forEach((sound) -> soundsMap.put(sound.getName(), new SoundData("master", Collections.singletonList(sound.getModel()))));

        Sounds sounds = new Sounds(soundsMap);

        FileWriter fw = new FileWriter(file);
        fw.write(sounds.getJson());
        fw.close();
    }

    default void writeMcMeta(File file) throws IOException {
        String finalJson = Textures.GSON.toJson(new McMeta(Textures.pack_format, Textures.pack_description));

        FileWriter fW = new FileWriter(file);
        fW.write(finalJson);
        fW.close();
    }

    default void throwEvent() {
        Bukkit.getPluginManager().callEvent(new TextureSetupEvent());
    }

    default void download(String link, File file) {
        try {
            BufferedInputStream bIS = new BufferedInputStream(new URL(link).openStream());

            FileOutputStream fOS = new FileOutputStream(file);

            byte[] buff = new byte[32 * 1024];
            int len;
            while ((len = bIS.read(buff)) > 0)
                fOS.write(buff, 0, len);

            fOS.close();
            bIS.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    default File createFolder(File file) {
        if (!file.exists())
            file.mkdirs();

        return file;
    }

}
