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
package net.iceyleagons.frostedengineering.common.textures;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import net.iceyleagons.frostedengineering.common.textures.base.*;
import net.iceyleagons.frostedengineering.common.textures.initialization.Station307;
import net.iceyleagons.frostedengineering.common.textures.initialization.TransferSH;
import net.iceyleagons.frostedengineering.common.textures.initialization.ZeroxZero;
import net.iceyleagons.frostedengineering.common.textures.interfaces.IUploadable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import net.iceyleagons.frostedengineering.common.textures.events.TextureInitializationEvent;
import net.iceyleagons.frostedengineering.utils.Reflections;

public class Textures {

    public static ArrayList<TexturedBlock> blocks = new ArrayList<>();
    public static ArrayList<TexturedItem> items = new ArrayList<>();
    public static ArrayList<TexturedSound> sounds = new ArrayList<>();
    public static ArrayList<Plugin> plugins = new ArrayList<>();
    public static ArrayList<Material> usedMaterials = new ArrayList<>();

    private static ConcurrentHashMap<String, Integer> idMap;
    public static HashMap<World, BlockStorage> storageMap = new HashMap<>();

    public static boolean USE_PACK_IMAGE = true; // If this is set to false, we need to set the PACK_IMAGE_LINK
    public static String PACK_IMAGE_LINK = "";
    public static int pack_format = Reflections.version.packFormat;
    public static String pack_description = "Just your normal everyday resourcepack";

    public static Gson GSON = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

    public static Textures instance;
    public static byte[] hash;

    private static YamlConfiguration resourceConfig;
    public static File mainFolder = Main.MAIN.getDataFolder();
    private static final File resourceData = new File(mainFolder, "resource-pack.yml");
    public static File homeFolder = createFolder(new File(mainFolder, "textures"));

    private static final List<IUploadable> uploadables = new ArrayList<>();

    /**
     * @deprecated Don't use outside of FrostedEngineering! For internal use only!
     */
    @Deprecated
    public Textures() {
        Textures.instance = this;

        init();

        // First mode registered is considered as default.
        registerInitMethod(new ZeroxZero());
        registerInitMethod(new TransferSH());
        // TODO: this.
        // registerInitMethod(new Dropbox());
        registerInitMethod(new Station307());

        Bukkit.getPluginManager().registerEvents(new TextureListeners(), Main.MAIN);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (getData("method") != null) {
                    for (IUploadable uploadable : uploadables)
                        for (String keyword : uploadable.keywords())
                            if (keyword.equalsIgnoreCase(getData("method")))
                                uploadable.init();
                } else
                    new ZeroxZero().init();
            }
        }.runTask(Main.MAIN);
    }

    public void registerInitMethod(IUploadable uploadable) {
        if (!resourceConfig.contains("method"))
            resourceConfig.addDefault("method", uploadable.keywords()[0]);
        uploadables.add(uploadable);
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
                try {
                    idMap = (ConcurrentHashMap<String, Integer>) oIS.readObject();
                } catch (ClassCastException exception) {
                    Main.error(Optional.of("Textures"), exception);
                }
                if (idMap == null)
                    idMap = new ConcurrentHashMap<>();

                fIS.close();
                oIS.close();
            } catch (IOException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        } else
            idMap = new ConcurrentHashMap<>();
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
        Main.executor.execute(() -> {
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

            storageMap.forEach((ignored, storage) -> storage.save());

            saveResourceData();
        });
    }

    private static File createFolder(File file) {
        if (!file.exists())
            file.mkdirs();

        return file;
    }

    @SuppressWarnings("deprecation")
    private static void create(TexturedBase base) {
        Main.executor.execute(() -> {
            if (base.getModel() != null)
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
                    base.setId(idMap.get(base.getName()));
                }
        });
    }

    public static BlockStorage getBlockStorage(World world) {
        return storageMap.get(world);
    }

    public static boolean isTexturedBlock(Block block) {
        return getBlock(block) != null;
    }

    public static boolean isTexturedBlock(ItemStack itemStack) {
        return getTexturedBlock(itemStack) != null;
    }

    public static boolean isTexturedItem(ItemStack itemStack) {
        for (TexturedItem texturedItem : items)
            if (texturedItem.getItem().getItemMeta().equals(itemStack.getItemMeta()))
                return true;

        return false;
    }

    public static TexturedBlock getTexturedBlock(ItemStack itemStack) {
        for (TexturedBlock texturedBlock : blocks)
            if (texturedBlock.getItem().getItemMeta().equals(itemStack.getItemMeta()))
                return texturedBlock;

        return null;
    }

    public static TexturedItem getTexturedItem(ItemStack itemStack) {
        for (TexturedItem texturedItem : items)
            if (texturedItem.getItem().getItemMeta().equals(itemStack.getItemMeta()))
                return texturedItem;

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

        if (!(base instanceof TexturedSound))
            create(base);

        if (base instanceof TexturedBlock) {
            if (base instanceof TexturedInterconnectible) {
                Collection<TexturedBlock> a;
                Collections.addAll(blocks, (a = ((TexturedInterconnectible) base).getRegisterMap().values()).toArray(new TexturedBlock[a.size()]));
            } else
                blocks.add((TexturedBlock) base);
        }

        if (base instanceof TexturedItem)
            items.add((TexturedItem) base);

        if (base instanceof TexturedSound)
            sounds.add((TexturedSound) base);

        if (base.getBaseMaterial() != null)
            if (!usedMaterials.contains(base.getBaseMaterial()))
                usedMaterials.add(base.getBaseMaterial());

        Bukkit.getPluginManager().callEvent(new TextureInitializationEvent(base));

        if (base.getRecipe() != null)
            Bukkit.addRecipe(base.getRecipe());
    }

}
