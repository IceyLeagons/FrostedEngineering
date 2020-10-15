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
package net.iceyleagons.frostedengineering.utils.festruct;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.utils.festruct.elements.Data;
import net.iceyleagons.frostedengineering.utils.festruct.elements.Layer;
import net.iceyleagons.frostedengineering.utils.festruct.elements.Loot;

public class FEStruct {

    private File f;
    private Data d;

    public FEStruct(File f) {
        this.f = f;
        this.d = new Data();
    }

    /**
     * This function is used to spawn a chest with a loot on a specific location
     *
     * @param loc is the {@link Location} to spawn the chest to
     * @param l   is the {@link Loot}
     * @param bd  is the saved {@link BlockData}
     */
    private void spawnChest(Location loc, Loot l, BlockData bd) {
        Random r = new Random();
        loc.getBlock().setType(Material.CHEST);
        loc.getBlock().setBlockData(bd, false);
        for (String s : l.getItems()) {
            String[] split = s.split(">");
            String item = split[0];
            int amount = Integer.parseInt(split[1]);

            if (item.contains("minecraft")) {
                item = item.replace("minecraft:", "");
                final Material mat = Material.valueOf(item.toUpperCase());

                if (mat != null) {
                    for (int i = 1; i <= amount; i++) {
                        Chest chest = (Chest) loc.getBlock().getState();
                        int slot = r.nextInt(chest.getInventory().getSize() - 1);
                        chest.getInventory().setItem(slot, new ItemStack(mat, 1));
                    }
                }
            }
        }
    }

    /**
     * This function is udes to spawn a spawner with a specified entity on a location
     *
     * @param loc is the {@link Location} where the spawner is needed to be placed
     * @param ent is the {@link EntityType} to be spawned
     */
    private void spawnSpawner(Location loc, EntityType ent) {
        loc.getBlock().setType(Material.SPAWNER);
        BlockState bs = loc.getBlock().getState();
        CreatureSpawner s = ((CreatureSpawner) bs);
        s.setSpawnedType(ent);
        bs.update();
    }

    int i = 0;
    int ec = 0;

    /**
     * This function is used to paste the structure block-by-block on a location
     *
     * @param loc is the location where the pasting will start
     */
    public void pasteToLocationBlockByBlock(Location loc) {
        i = 0;
        ec = 0;
        HashMap<Block, BlockData> blocks = new HashMap<Block, BlockData>();
        new Thread() {
            @Override
            public void run() {
                Layer[] layers = new Layer[d.getLayers().size()];
                layers = d.getLayers().toArray(layers);
                Location start = loc;
                double x = loc.getX();
                double z = loc.getZ();
                for (int i = 0; i < layers.length; i++) {
                    if (i != 0) {
                        start = start.add(0, 1, 0);
                    }
                    start.setX(x);
                    start.setZ(z);

                    Layer l = layers[i];
                    String[] rows = new String[l.getLayerRows().size()];
                    rows = l.getLayerRows().toArray(rows);
                    for (int j = 0; j < rows.length; j++) {
                        start.setX(x);
                        Location toPaste = start;

                        for (int k = 0; k < rows[j].chars().count(); k++) {
                            toPaste.add(1, 0, 0);
                            final BlockData bd = d.getBlockDataFromCode(String.valueOf(rows[j].charAt(k)));
                            if (bd.getMaterial() == Material.CHEST) {
                                Loot loot = d.getLootFromCode(String.valueOf(rows[j].charAt(k)));
                                if (loot != null) {
                                    spawnChest(toPaste, loot, bd);
                                }
                            } else {
                                if (toPaste.getBlock().getType() == Material.AIR && bd.getMaterial() == Material.AIR)
                                    continue;

                                blocks.put(toPaste.getBlock(), bd);
                            }
                        }
                        start.add(0, 0, 1);
                    }
                }
                paste(blocks);
            }
        }.start();
    }


    /**
     * This function is used to instantly paste the structure.
     *
     * @param loc is the {@link Location} where the pasting will happen
     */
    public void pasteToLocation(Location loc) {
        i = 0;
        ec = 0;
        Layer[] layers = new Layer[d.getLayers().size()];
        layers = d.getLayers().toArray(layers);
        Location start = loc;
        double x = loc.getX();
        double z = loc.getZ();
        for (int i = 0; i < layers.length; i++) {
            if (i != 0) {
                start = start.add(0, 1, 0);
            }
            start.setX(x);
            start.setZ(z);

            Layer l = layers[i];
            String[] rows = new String[l.getLayerRows().size()];
            rows = l.getLayerRows().toArray(rows);
            for (int j = 0; j < rows.length; j++) {
                start.setX(x);
                Location toPaste = start;

                for (int k = 0; k < rows[j].chars().count(); k++) {
                    toPaste.add(1, 0, 0);
                    final BlockData bd = d.getBlockDataFromCode(String.valueOf(rows[j].charAt(k)));
                    if (bd.getMaterial() == Material.CHEST) {
                        Loot loot = d.getLootFromCode(String.valueOf(rows[j].charAt(k)));
                        if (loot != null) {
                            spawnChest(toPaste, loot, bd);
                        }
                    } else {
                        if (bd.getMaterial() == Material.BARRIER) {
                            if (ec < d.getEntities().size()) {
                                String ent = d.getEntities().get(ec);
                                if (ent.contains("minecraft:")) {
                                    ent = ent.replace("minecraft:", "");
                                    ent = ent.toUpperCase();
                                    spawnSpawner(toPaste, EntityType.valueOf(ent));
                                }
                                ec++;
                            }
                            continue;
                        }
                        if (toPaste.getBlock().getType() == Material.AIR && bd.getMaterial() == Material.AIR)
                            continue;

                        toPaste.getBlock().setBlockData(bd, false);
                    }
                }
                start.add(0, 0, 1);
            }
        }
    }

    BukkitTask task = null;

    /**
     * This function is used to create the block-by-block effect with {@link BukkitScheduler}
     *
     * @param blocks is a map with {@link Block}s and {@link BlockData}s
     */
    private void paste(final Map<Block, BlockData> blocks) {
        Block[] b = new Block[blocks.keySet().size()];
        b = blocks.keySet().toArray(b);

        BlockData[] mats = new BlockData[blocks.values().size()];
        mats = blocks.values().toArray(mats);

        final Block[] fblocks = b;
        final BlockData[] fmats = mats;

        task = Bukkit.getScheduler().runTaskTimer(Main.MAIN, new Runnable() {

            @Override
            public void run() {
                paste(fblocks, fmats);
            }
        }, 0L, 1L);
    }

    /**
     * This function is called from {@link #paste(Map)}
     *
     * @param blocks is the {@link Block} array.
     * @param bds    is the {@link BlockData} array
     */
    private void paste(Block[] blocks, BlockData[] bds) {
        blocks[i].setBlockData(bds[i], false);
        blocks[i].getLocation().getWorld().playSound(blocks[i].getLocation(), Sound.BLOCK_STONE_BREAK, 1L, 1L);
        if (i >= blocks.length) {
            stop();
            return;
        }
        i++;
    }

    /**
     * This will stop the {@link BukkitScheduler} found in {@link #paste(Map)}
     */
    private void stop() {
        task.cancel();
        i = 0;
    }

    /**
     * This will load in the .festruct file.
     *
     * @return the {@link FEStruct} instance
     * @throws FileNotFoundException if the file cannot be found
     */
    @SuppressWarnings("resource")
    public FEStruct load() throws FileNotFoundException {
        Scanner s = new Scanner(f, "UTF-16");
        StringBuilder sb = new StringBuilder();
        while (s.hasNextLine()) {
            String data = s.nextLine();
            sb.append(data);
        }

        String[] data = sb.toString().split(";");
        for (int i = 0; i < data.length; i++) { // Iterate through our sections
            String section = data[i]; // Get the section
            if (section.contains("rarity")) { // Test for section rarity
                d.setRarity(Integer.parseInt(section.split(" ")[1]));

                /*
                 * Entities
                 */
            } else if (section.contains("entities")) { // Test for section entities
                section = section.replace("entities", "");
                String[] entities = section.split("\\.", -1);
                for (int j = 0; j < entities.length; j++)
                    d.getEntities().add(entities[j]);

                /*
                 * Loot
                 */
            } else if (section.contains("loot")) { // Test for section loot
                Loot l = new Loot();
                section = section.replace("loot", "");
                String[] lootdata = section.split("\\.", -1);
                for (int j = 0; j < lootdata.length; j++) {
                    String ldata = lootdata[j];
                    if (ldata.contains("id")) {
                        l.setId(Integer.parseInt(ldata.split(" ")[1]));
                    } else if (ldata.contains("letter")) {
                        l.setLetter(ldata.split(" ")[1]);
                    } else {
                        l.getItems().add(ldata);
                    }
                }
                d.getLoots().add(l);

                /*
                 * Materials
                 */
            } else if (section.contains("materials")) { // Test for section materials
                section = section.replace("materials", "");
                String[] materials = section.split("\\.", -1);
                for (int j = 0; j < materials.length; j++) {
                    String[] material = materials[j].split(">");
                    d.getMaterials().put(material[1], material[0]);
                }

                /*
                 * Layers
                 */
            } else if (section.contains("layer")) {
                section = section.replace("layer", "");
                String[] layers = section.split("\\.", -1);
                Layer l = new Layer();
                for (int j = 0; j < layers.length; j++) {
                    String layer = layers[j];
                    if (layer.contains("id")) {
                        l.setId(Integer.parseInt(layer.split(" ")[1]));
                    } else {
                        l.getLayerRows().add(layer);
                    }
                    // System.out.println(layers[j]);
                }
                d.getLayers().add(l);
            }

        }
        return this;
    }

}
