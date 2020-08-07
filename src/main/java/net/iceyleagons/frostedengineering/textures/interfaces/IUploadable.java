/*
 *  Copyright (C) IceyLeagons(https://iceyleagons.net/)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package net.iceyleagons.frostedengineering.textures.interfaces;

import java.io.*;
import java.net.URL;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import lombok.SneakyThrows;
import okhttp3.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.textures.Textures;
import net.iceyleagons.frostedengineering.textures.base.TexturedBase;
import net.iceyleagons.frostedengineering.textures.events.TextureSetupEvent;
import net.iceyleagons.frostedengineering.textures.initialization.McMeta;
import net.iceyleagons.frostedengineering.textures.initialization.Sounds;
import net.iceyleagons.frostedengineering.textures.initialization.Sounds.SoundData;

public interface IUploadable {

    void init();

    default void upload(File file) {
        new Timer().schedule(new TimerTask() {
            @SneakyThrows
            @Override
            public void run() {
                String url = new OkHttpClient().newCall(new Request.Builder()
                        .url("https://www.station307.com/")
                        .put(RequestBody.create(MediaType.parse("application/zip"), file))
                        .build()).execute().header("com.station307.located-at");

                Main.executor.execute(new Runnable() {
                    @SneakyThrows
                    @Override
                    public void run() {
                        while (Main.MAIN.isEnabled()) {
                            assert url != null;
                            new OkHttpClient().newCall(new Request.Builder()
                                    .url(url)
                                    .put(RequestBody.create(MediaType.parse("application/zip"), file))
                                    .build()).execute();
                        }
                    }
                });

                Main.info(Optional.of("Textures"), "Resource pack uploaded.");
                Main.info(Optional.of("Textures"),
                        "Resource pack link is: " + url);
                Main.info(Optional.of("Textures"), "Calculating SHA-1 hash...");
                String hash = sha1Code(file);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Main.info(Optional.of("Textures"),
                                "Resource pack hash is: " + hash);

                        Textures.setData("resourcepack-link", url);
                        Textures.setData("resourcepack-hash", hash);

                        Bukkit.getOnlinePlayers().forEach(player -> player
                                .setResourcePack(Textures.getData("resourcepack-link"), Textures.getData("resourcepack-hash")));
                    }
                }, 1000L);
            }
        }, 10000L);
    }

    @SneakyThrows
    default String sha1Code(File file) {
        FileInputStream fileInputStream = new FileInputStream(file);
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        DigestInputStream digestInputStream = new DigestInputStream(fileInputStream, digest);
        byte[] bytes = new byte[1024];

        while (digestInputStream.read(bytes) > 0) ;

        byte[] resultByteArry = digest.digest();
        return bytesToHexString(resultByteArry);
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
            printWriter.write("{ \"parent\": \"item/handheld\", \"textures\": { \"layer0\": \"items/"
                    + baseMaterial.name().toLowerCase()
                    + "\" }, \"overrides\": [ { \"predicate\": {\"custom_model_data\": 0}, \"model\": \""
                    + baseMaterial.name().toLowerCase() + "\"}\"");

        texturedList.forEach((textured) -> {
            if (textured.getBaseMaterial() == baseMaterial) {
                Main.info(Optional.of("TEXTURES"), "Wrote custom JSON data for TexturedBase #" + textured.getId()
                        + " (" + textured.getName() + ") with id " + textured.getId());

                printWriter.println(",{ \"predicate\": {\"custom_model_data\": " + textured.getId()
                        + "}, \"model\": \"" + textured.getModel() + "\"}");
            }
        });

        if (texturedList.size() != 0)
            printWriter.println(" ]}");

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
