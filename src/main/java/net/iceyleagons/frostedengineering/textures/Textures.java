package net.iceyleagons.frostedengineering.textures;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.textures.base.TexturedBase;
import net.iceyleagons.frostedengineering.textures.base.TexturedBlock;
import net.iceyleagons.frostedengineering.textures.base.TexturedItem;
import net.iceyleagons.frostedengineering.textures.initialization.Minepack;

public class Textures {

	public static ArrayList<TexturedBlock> blocks = new ArrayList<>();
	public static ArrayList<TexturedItem> items = new ArrayList<>();
	public static ArrayList<Plugin> plugins = new ArrayList<>();

	public static HashMap<String, Integer> idMap;
	public static HashMap<World, BlockStorage> storageMap = new HashMap<>();

	public static boolean USE_PACK_IMAGE = true; // If this is set to false, we need to set the PACK_IMAGE_LINK
	public static String PACK_IMAGE_LINK = "";
	public static int pack_format = Reflections.version.packFormat;
	public static String pack_description = "Just your normal everyday resourcepack";

	public static Gson GSON = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

	public static Textures instance;
	public static Minepack minepack;

	private static YamlConfiguration resourceConfig;
	public static File mainFolder = Main.MAIN.getDataFolder();
	private static File resourceData = new File(mainFolder, "resource-pack.yml");
	public static File homeFolder = createFolder(new File(mainFolder, "textures"));

	/**
	 * @deprecated Don't use outside of FrostedEngineering! For internal use only!
	 */
	@Deprecated
	public Textures() {
		Textures.instance = this;

		init();

		Bukkit.getPluginManager().registerEvents(new TextureListeners(), Main.MAIN);

		new BukkitRunnable() {
			@Override
			public void run() {
				(minepack = new Minepack()).init();
			}
		}.runTask(Main.MAIN);
	}

	public void onDisable() {
		save();
	}

	@SuppressWarnings("unchecked")
	public static void init() {
		// Create directories
		File file = new File(mainFolder, "id.map");

		try {
			resourceData.createNewFile();
		} catch (IOException exception) {
			exception.printStackTrace();
		}

		resourceConfig = new YamlConfiguration();

		if (resourceData.exists()) {
			try {
				resourceConfig.load(resourceData);
			} catch (IOException | InvalidConfigurationException exception) {
				exception.printStackTrace();
			}
		}

		if (file.exists()) {
			try {
				FileInputStream fIS = new FileInputStream(file);
				ObjectInputStream oIS = new ObjectInputStream(fIS);

				idMap = (HashMap<String, Integer>) oIS.readObject();
				if (idMap == null)
					idMap = new HashMap<>();

				fIS.close();
				oIS.close();
			} catch (IOException | ClassNotFoundException exception) {
				exception.printStackTrace();
			}
		} else
			idMap = new HashMap<>();
	}

	public static String getData(String key) {
		return resourceConfig.getString(key);
	}

	public static void setData(String key, String value) {
		resourceConfig.set(key, value);
		saveResourceData();
	}

	public static void saveResourceData() {
		try {
			resourceConfig.save(resourceData);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public static void save() {
		File idMapFile = new File(mainFolder, "id.map");

		if (idMapFile.exists())
			idMapFile.delete();

		try {
			FileOutputStream fos = new FileOutputStream(idMapFile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(idMap);
			oos.close();
			fos.close();

		} catch (IOException exception) {
			exception.printStackTrace();
		}

		storageMap.forEach((ignored, storage) -> {
			storage.save();
		});

		saveResourceData();
	}

	private static File createFolder(File file) {
		if (!file.exists())
			file.mkdirs();

		return file;
	}

	private static void create(TexturedBase base) {
		if (base.getModel() != null) {
			if (!idMap.containsKey(base.getName())) {

				Collection<Integer> c = idMap.values();
				int i = 0;
				while (true) {
					i++;
					if (!c.contains(i)) {
						idMap.put(base.getName(), i);
						base.setId(i);
						System.out.println(base.getId());
						break;
					}
				}
			} else {
				base.setId(idMap.get(base.getName()).intValue());
			}
		}

	}

	public static BlockStorage getBlockStorage(World world) {
		return storageMap.get(world);
	}

	public static boolean isTexturedBlock(Block block) {
		if (getBlock(block) != null)
			return true;

		return false;
	}

	public static boolean isTexturedBlock(ItemStack itemStack) {
		if (getTexturedBlock(itemStack) != null)
			return true;

		return false;
	}

	public static boolean isTexturedItem(ItemStack itemStack) {
		for (TexturedItem texturedItem : items) {
			if (texturedItem.getItem().getItemMeta().equals(itemStack.getItemMeta()))
				return true;
		}

		return false;
	}

	public static TexturedBlock getTexturedBlock(ItemStack itemStack) {
		for (TexturedBlock texturedBlock : blocks) {
			if (texturedBlock.getItem().getItemMeta().equals(itemStack.getItemMeta()))
				return texturedBlock;
		}

		return null;
	}

	public static TexturedItem getTexturedItem(ItemStack itemStack) {
		for (TexturedItem texturedItem : items) {
			if (texturedItem.getItem().getItemMeta().equals(itemStack.getItemMeta()))
				return texturedItem;
		}

		return null;
	}

	public static TexturedBlock getBlock(Block block) {
		for (TexturedBlock texturedBlock : blocks) {
			if (texturedBlock.compare(block))
				return texturedBlock;
		}

		return null;
	}

	public static void register(TexturedBase base) {
		if (!plugins.contains(base.getPlugin()))
			plugins.add(base.getPlugin());

		create(base);

		if (base instanceof TexturedBlock)
			blocks.add((TexturedBlock) base);

		if (base instanceof TexturedItem)
			items.add((TexturedItem) base);

		if (base.getRecipe() != null)
			Bukkit.addRecipe(base.getRecipe());
	}

}
