package net.iceyleagons.frostedengineering.textures.initialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.textures.BlockStorage;
import net.iceyleagons.frostedengineering.textures.Textures;
import net.iceyleagons.frostedengineering.textures.interfaces.IUploadable;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;

public class Minepack implements IUploadable {

	@SuppressWarnings("unchecked")
	@Override
	public void init() {
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
				exception.printStackTrace();
			}
		});

		// We write our custom diamond_hoe.json file.
		File modelsFolder = createFolder(new File(finalResourcesFolder, "assets/minecraft/models/item"));
		try {
			FileOutputStream fOS = new FileOutputStream(new File(modelsFolder, "diamond_hoe.json"));
			PrintWriter printWriter = new PrintWriter(fOS);
			printModelData(printWriter, Material.DIAMOND_HOE);

			fOS.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}

		Plugin plugin = Main.MAIN;
		if (Textures.USE_PACK_IMAGE)
			plugin.saveResource("pack.png", true);

		plugin.saveResource("mob_spawner.png", true);

		deleteFile(new File(finalResourcesFolder, "pack.mcmeta"));
		if (Textures.USE_PACK_IMAGE)
			deleteFile(new File(finalResourcesFolder, "pack.png"));

		File blocksFolder = createFolder(new File(finalResourcesFolder, "assets/minecraft/textures/block"));
		File mobSpawnerFile = new File(blocksFolder, "spawner.png");
		deleteFile(mobSpawnerFile);

		File oldResourcePack = new File(Textures.homeFolder, "old-resourcepack.zip");
		File finalResourcePack = new File(Textures.homeFolder, "final-resourcepack.zip");

		try {
			writeMcMeta(new File(finalResourcesFolder, "pack.mcmeta"));
			if (Textures.USE_PACK_IMAGE)
				FileUtils.moveFile(new File(Textures.mainFolder, "pack.png"),
						new File(finalResourcesFolder, "pack.png"));

			FileUtils.moveFile(new File(Textures.mainFolder, "mob_spawner.png"), mobSpawnerFile);
			FileUtils.copyFile(mobSpawnerFile, new File(blocksFolder, "mob_spawner.png"));

			deleteFile(oldResourcePack);

			if (finalResourcePack.exists())
				FileUtils.copyFile(finalResourcePack, oldResourcePack);
		} catch (IOException exception) {
			exception.printStackTrace();
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

		// Compare the old and the new resourcepack to check if there's any changes.
		if (!oldResourcePack.exists())
			upload(finalResourcePack);
		else {
			upload(finalResourcePack);
		}

		Textures.plugins.forEach((pl) -> {
			File pluginFolder = new File(resourcepacksFolder, pl.getName());
			deleteFile(pluginFolder);
		});

		deleteFile(oldResourcePack);

		// Create block storages for all the registered worlds.
		Bukkit.getWorlds().forEach((world) -> {
			File blocksList = new File(world.getWorldFolder(), "block.map");

			if (blocksList.exists()) {
				try {
					FileInputStream fIS = new FileInputStream(blocksList);
					ObjectInputStream oIS = new ObjectInputStream(fIS);

					Textures.storageMap.put(world,
							new BlockStorage(world, (Map<Map<String, Object>, Map<String, Object>>) oIS.readObject()));

					oIS.close();
					fIS.close();
				} catch (IOException | ClassNotFoundException exception) {
					exception.printStackTrace();
				}
			} else {
				Textures.storageMap.put(world, new BlockStorage(world));
			}
		});

	}

}
