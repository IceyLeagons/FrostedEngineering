package net.iceyleagons.frostedengineering.textures;

import com.google.common.io.ByteStreams;
import lombok.NonNull;
import lombok.SneakyThrows;
import net.iceyleagons.frostedengineering.FrostedEngineering;
import net.iceyleagons.frostedengineering.FrostedEngineeringBootstrapper;
import net.iceyleagons.frostedengineering.api.IFrostedEngineering;
import net.iceyleagons.frostedengineering.api.textures.ITextureProvider;
import net.iceyleagons.frostedengineering.api.textures.types.ITextured;
import net.iceyleagons.frostedengineering.textures.sound.Sounds;
import net.iceyleagons.frostedengineering.textures.sound.Sounds.SoundData;
import net.iceyleagons.icicle.location.block.BlockStorage;
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
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public interface ITextureUploader {

    /**
     * Does all the good stuff.
     *
     * @param frostedEngineering self-explanatory.
     * @param mainFolder         the folder we want to branch off of.
     */
    default void commence(IFrostedEngineering frostedEngineering, File mainFolder) {
        File resourcePackFile = preUpload(frostedEngineering, frostedEngineering.getTextureProvider(), createFolder(new File(mainFolder, "textures/")));
        Map.Entry<String, byte[]> urlAndHashEntry = upload(resourcePackFile);
        postUpload(frostedEngineering.getTextureProvider(), urlAndHashEntry.getKey(), urlAndHashEntry.getValue());
    }

    /**
     * Uploads the provided file.
     *
     * @param file the file we wish to upload.
     * @return an entry containing the url and the hash of the resource pack.
     */
    Map.Entry<String, byte[]> upload(File file);

    /**
     * @return when was the last upload?
     */
    @NonNull
    long lastUpload();

    /**
     * Common stuff we do before every upload.
     *
     * @param frostedEngineering self-explanatory.
     * @param textureProvider    self-explanatory.
     * @param mainFolder         the folder we want to start in.
     * @return the resource packs file.
     */
    @Nullable
    default File preUpload(IFrostedEngineering frostedEngineering, ITextureProvider textureProvider, File mainFolder) {
        try {
            printData(textureProvider);

            File resourcepacksFolder = createFolder(new File(mainFolder, "resourcepacks"));
            File finalResourcesFolder = createFolder(new File(resourcepacksFolder, "final_resources"));

            File finalResourcePack = new File(mainFolder, "final-resourcepack.zip");

            File assetsFolder = new File(finalResourcesFolder, "assets");

            // We get the assets.zip files and extract them into the final_resources folder.
            textureProvider.getRegistrars().forEach((plugin) -> {
                File pluginFolder = new File(resourcepacksFolder, plugin.getName());
                deleteFile(pluginFolder);
                createFolder(pluginFolder);

                File assets = extractFile(plugin, "assets.zip", new File(pluginFolder, "assets.zip"));

                unzipFile(assets, pluginFolder);
                deleteFile(assets);

                try {
                    FileUtils.copyDirectory(pluginFolder, assetsFolder);
                } catch (IOException exception) {
                    // Main.error(Optional.of("Textures"), exception);
                }
            });

            // We write our custom x.json file.
            File itemModelsFolder = createFolder(new File(finalResourcesFolder, "assets/minecraft/models/item"));
            File blockModelsFolder = createFolder(new File(finalResourcesFolder, "assets/minecraft/models/block"));
            textureProvider.getUsedMaterials().forEach(material -> writeModelData(new File(material.isBlock() ? blockModelsFolder : itemModelsFolder,
                    material.name().toLowerCase() + ".json"), textureProvider));

            Plugin plugin = frostedEngineering.getPlugin();
            if (TextureProvider.USE_PACK_IMAGE)
                plugin.saveResource("pack.png", true);

            plugin.saveResource("mob_spawner.png", true);

            deleteFile(new File(finalResourcesFolder, "pack.mcmeta"));
            if (TextureProvider.USE_PACK_IMAGE)
                deleteFile(new File(finalResourcesFolder, "pack.png"));

            File blocksFolder = createFolder(new File(finalResourcesFolder, "assets/minecraft/textures/block"));
            File minecraftFolder = createFolder(new File(finalResourcesFolder, "assets/minecraft"));
            File soundsFile = new File(minecraftFolder, "sounds.json");
            File mobSpawnerFile = new File(blocksFolder, "spawner.png");
            deleteFile(mobSpawnerFile);
            deleteFile(soundsFile);

            File oldResourcePack = new File(mainFolder, "old-resourcepack.zip");

            try {
                writeMcMeta(new File(finalResourcesFolder, "pack.mcmeta"), textureProvider);
                writeSounds(soundsFile, textureProvider);
                if (TextureProvider.USE_PACK_IMAGE)
                    FileUtils.moveFile(new File(frostedEngineering.getPlugin().getDataFolder(), "pack.png"),
                            new File(finalResourcesFolder, "pack.png"));

                FileUtils.moveFile(new File(frostedEngineering.getPlugin().getDataFolder(), "mob_spawner.png"), mobSpawnerFile);
                FileUtils.copyFile(mobSpawnerFile, new File(blocksFolder, "mob_spawner.png"));

                deleteFile(oldResourcePack);

                if (finalResourcePack.exists())
                    FileUtils.copyFile(finalResourcePack, oldResourcePack);
            } catch (IOException exception) {
                // Main.error(Optional.of("Textures"), exception);
            }

            if (!TextureProvider.USE_PACK_IMAGE)
                net.iceyleagons.icicle.file.FileUtils.downloadFile(TextureProvider.PACK_IMAGE_URL, new File(finalResourcesFolder, "pack.png"));

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

                // Why #getFile()? We need to zip it up, and as far as I know, calling this is equal to zipping it all up.
                zip.getFile();
            } catch (ZipException exception) {
                exception.printStackTrace();
            }

            // Compare the old and the new resourcepack to check if there's any changes.
            if (textureProvider.getResourcePackHash() != null && Arrays.equals(textureProvider.getResourcePackHashBytes(), sha1Code(finalResourcePack)))
                return null;

            textureProvider.getRegistrars().forEach((pl) -> {
                File pluginFolder = new File(resourcepacksFolder, pl.getName());
                deleteFile(pluginFolder);
            });

            deleteFile(oldResourcePack);
            return finalResourcePack;
        } catch (Exception uncatchedException) {
            // This is bad. We have an uncatched exception.
            uncatchedException.printStackTrace();
            // Main.error(Optional.of("Textures"), uncatchedException);
        } finally {
            // Create block storages for all the registered worlds.
            Bukkit.getWorlds().forEach((world) -> {
                File blocksList = new File(world.getWorldFolder(), "block.map");

                if (blocksList.exists()) {
                    try {
                        FileInputStream fIS = new FileInputStream(blocksList);
                        ObjectInputStream oIS = new ObjectInputStream(fIS);

                        textureProvider.getStorageMap().put(world, new BlockStorage(world,
                                (Map<Map<String, Object>, Map<String, Object>>) oIS.readObject()));

                        oIS.close();
                        fIS.close();
                    } catch (IOException | ClassNotFoundException exception) {
                        // Main.error(Optional.of("Textures"), exception);
                    }
                } else {
                    textureProvider.getStorageMap().put(world, new BlockStorage(world));
                }
            });

            // throwEvent();
        }

        return null;
    }

    /**
     * Common stuff we do after every upload.
     *
     * @param textureProvider self-explanatory.
     * @param url             the url of the resource pack.
     * @param hash            the hash of the resource pack. (byte[])
     */
    default void postUpload(ITextureProvider textureProvider, String url, byte[] hash) {
        System.out.printf("Resource pack uploaded.%n");
        System.out.printf("Resource pack link is: %s%n", url);
        System.out.printf("Resource pack hash is: %s%n", bytesToHexString(hash));

        textureProvider.setResourcePackUrl(url);
        textureProvider.setResourcePackHash(bytesToHexString(hash));
        textureProvider.setResourcePackHashBytes(hash);

        if (textureProvider.getResourcePackUrl() == null || textureProvider.getResourcePackHashBytes() == null)
            throw new RuntimeException("Something really bad happened.");

        Bukkit.getOnlinePlayers().forEach(player -> player
                .setResourcePack(textureProvider.getResourcePackUrl(), textureProvider.getResourcePackHashBytes()));
    }

    /**
     * Write an McMeta file to the provided file.
     *
     * @param file            the file we wish to write it to.
     * @param textureProvider self-explanatory.
     */
    default void writeMcMeta(File file, ITextureProvider textureProvider) {
        String finalJson = TextureProvider.GSON.toJson(new McMeta(7, "Very cool pack."));

        try (FileWriter fW = new FileWriter(file)) {
            fW.write(finalJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write sound data to the sounds.json.
     *
     * @param file            the file we wish to write it to.
     * @param textureProvider self-explanatory.
     */
    default void writeSounds(File file, ITextureProvider textureProvider) {
        HashMap<String, SoundData> soundsMap = new HashMap<>();

        textureProvider.getSoundMap().forEach((id, sound) -> soundsMap.put(sound.getName(), new SoundData("master", Collections.singletonList(sound.getLocation()))));

        Sounds sounds = new Sounds(soundsMap);

        try (FileWriter fw = new FileWriter(file)) {
            fw.write(sounds.getJson());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes an items data to the provided file.
     *
     * @param file            the file we with to write.
     * @param textureProvider self-explanatory.
     */
    default void writeModelData(File file, ITextureProvider textureProvider) {
        try (PrintWriter printWriter = new PrintWriter(new FileOutputStream(file))) {
            Map<String, ITextured> texturedList = new HashMap<>(textureProvider.getBlockMap());
            texturedList.putAll(textureProvider.getItemMap());

            Material baseMaterial = Material.valueOf(file.getName().replace(".json", ""));

            if (texturedList.size() != 0)
                printWriter.write("{\n\t\"parent\": \"item/handheld\",\n\t\"textures\":\n\t{\n\t\t\"layer0\": \"items/"
                        + baseMaterial.name().toLowerCase()
                        + "\"\n\t},\n\t\"overrides\": [\n\t\t{\n\t\t\t\"predicate\":\n\t\t\t{\n\t\t\t\t\"custom_model_data\": 0\n\t\t\t},\n\t\t\t\"model\": \""
                        + baseMaterial.name().toLowerCase() + "\"\n\t\t}");

            texturedList.entrySet().stream().filter(entry -> entry.getValue().getBaseMaterial() == baseMaterial).forEach(entry -> {
                printWriter.println(",\n\t\t{\n\t\t\t\"predicate\":\n\t\t\t{\n\t\t\t\t\"custom_model_data\": " + entry.getKey()
                        + "\n\t\t\t},\n\t\t\t\"model\": \"" + entry.getValue().getLocation() + "\"\n\t\t}");

                // Main.info(Optional.of("TEXTURES"), "Wrote custom JSON data for TexturedBase #" + id
                //        + " (" + textured.getName() + ") with id " + id);
            });

            if (texturedList.size() != 0)
                printWriter.println("\n\t]\n}");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints data about the current texture provider.
     *
     * @param textureProvider self-explanatory.
     */
    default void printData(ITextureProvider textureProvider) {
        if (textureProvider.getBlockMap().size() != 0)
            System.out.printf("There are %s block(s) registered.%n", textureProvider.getBlockMap().size());
        if (textureProvider.getItemMap().size() != 0)
            System.out.printf("There are %s item(s) registered.%n", textureProvider.getItemMap().size());
        if (textureProvider.getSoundMap().size() != 0)
            System.out.printf("There are %s sound(s) registered.%n", textureProvider.getSoundMap().size());

        if (textureProvider.getBlockMap().size() + textureProvider.getItemMap().size() + textureProvider.getSoundMap().size() != 0)
            System.out.printf("In total, there's %s TexturedBase(s) registered from %s total registrars.%n",
                    textureProvider.getBlockMap().size() + textureProvider.getItemMap().size() + textureProvider.getSoundMap().size(),
                    textureProvider.getRegistrars().size());
    }

    /**
     * Sums the file up into an SHA-1 sum.
     *
     * @param file the file that we wish to acquire the sum of.
     * @return SHA-1 checksum of the provided file.
     */
    @SneakyThrows
    default byte[] sha1Code(File file) {
        FileInputStream fileInputStream = new FileInputStream(file);
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        DigestInputStream digestInputStream = new DigestInputStream(fileInputStream, digest);
        byte[] bytes = new byte[1024];

        while (digestInputStream.read(bytes) > 0) {
            // Load the entire thing into RAM.
        }

        return digest.digest();
    }

    /**
     * Converts an array of bytes to a hexadecimal string.
     *
     * @param bytes the array we wish to convert.
     * @return the byte array provided but in a hexadecimal string.
     */
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

    /**
     * Unzips the selected file to the provided directory.
     *
     * @param assets the file we wish to extract.
     * @param where  where we want to extract the files to.
     */
    default void unzipFile(File assets, File where) {
        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(assets))) {
            ZipEntry entry = zipIn.getNextEntry();
            while (entry != null) {
                if (!entry.isDirectory())
                    extractEntry(zipIn, new File(where, entry.getName()));
                else createFolder(new File(where, entry.getName()));
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Extracts an entry from a zip.
     *
     * @param zipIn the input stream of the zip.
     * @param file  where we want to extract it.
     */
    default void extractEntry(ZipInputStream zipIn, File file) {
        try (FileOutputStream fOS = new FileOutputStream(file)) {
            byte[] bytesIn = new byte[8192];
            int read;
            while ((read = zipIn.read(bytesIn)) != -1)
                fOS.write(bytesIn, 0, read);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Extracts a file from inside of a plugin.
     *
     * @param plugin the plugin we want to extract from.
     * @param name   the name of the file.
     * @param where  where we want to extract it.
     * @return the location of the extracted file.
     */
    default File extractFile(Plugin plugin, String name, File where) {
        try (FileOutputStream fOS = new FileOutputStream(where)) {
            byte[] resourceData = IOUtils.toByteArray(plugin.getResource(name));
            fOS.write(resourceData, 0, resourceData.length);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return where;
    }

    /**
     * Force deletes a file.
     *
     * @param file the file we wish to delete.
     */
    default void deleteFile(File file) {
        try {
            if (file.exists())
                FileUtils.forceDelete(file);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Creates a folder at the specified file.
     *
     * @param file the folder we wish to create.
     * @return the file we provided.
     */
    default File createFolder(File file) {
        if (!file.exists())
            file.mkdirs();

        return file;
    }

}
