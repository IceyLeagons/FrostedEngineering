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
package net.iceyleagons.frostedengineering.utils.festruct;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.utils.Cuboid;
import net.iceyleagons.frostedengineering.utils.RandomString;
import net.iceyleagons.frostedengineering.utils.festruct.elements.Loot;

public class FEStructSaver {

	private int numoflayers, numofblocksinlayer;
	private List<Block> blocks;
	private Cuboid c;

	private String name;
	private List<String> entities;

	private int rarity;

	/**
	 * @param c is the {@link Cuboid} of the area we want to save
	 * @param name is the name of the structure --> name of the file
	 * @param entities the list of entity names which can spawn there
	 * @param rarity is the rarity of the structure when generating
	 */
	public FEStructSaver(Cuboid c, String name, List<String> entities, int rarity) {
		this.c = c;
		this.numoflayers = c.getSizeY();
		this.numofblocksinlayer = c.getBlocks().size() / numoflayers;
		this.blocks = c.getBlocks();
		this.name = name;
		this.entities = entities;
		this.rarity = rarity;
	}

	/**
	 * @param p is the {@link Player} who issued the save.
	 */
	public void save(Player p) {
		List<Loot> loots = new ArrayList<Loot>();
		Map<Integer, List<Block>> blocksinlayer = new HashMap<Integer, List<Block>>();
		int last = 0;
		for (int i = 0; i < numoflayers; i++) {
			List<Block> blockss = new ArrayList<Block>();
			int lasttemp = last;
			for (int j = 0; j < numofblocksinlayer; j++) {
				blockss.add(this.blocks.get(last + j));
				lasttemp++;
			}
			last = lasttemp;
			blocksinlayer.put(i, blockss);
		}

		Map<String, String> mats = getMaterials();

		last = 0;
		Map<Integer, List<String>> layers = new HashMap<Integer, List<String>>();
		for (int i = 0; i < numoflayers; i++) {
			List<String> blockss = new ArrayList<String>();
			int lasttemp = last;
			int k = 0;
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < numofblocksinlayer; j++) {
				if (k == c.getSizeZ() - 1) {
					sb.append(getMaterialCode(this.blocks.get(last + j), mats));
					blockss.add(sb.toString());
					sb = new StringBuilder();
					k = 0;
				} else {
					if ((this.blocks.get(last + j).getState()) instanceof Chest) {
						Loot l = new Loot();
						l.setId(loots.size());
						Chest chest = (Chest) this.blocks.get(last + j).getState();
						l.setItems(getItemsInChest(chest));
						String code = getMaterialCodeForChest(this.blocks.get(last + j), mats);
						l.setLetter(code);
						loots.add(l);
						sb.append(code);
					} else {
						sb.append(getMaterialCode(this.blocks.get(last + j), mats));
					}
					k++;
				}
				lasttemp++;
			}
			last = lasttemp;
			layers.put(i, blockss);
		}

		StringBuilder entity;
		if (!entities.isEmpty()) {
			entity = new StringBuilder("entities\n" + entities.get(0));
			for (int i = 1; i < entities.size(); i++) {
				entity.append(".\n" + entities.get(i));
			}
			entity.append("\n;\n");
		} else {
			entity = new StringBuilder();
		}

		StringBuilder loot;
		if (!loots.isEmpty()) {
			loot = new StringBuilder();
			loots.forEach(l -> {
				loot.append("loot\n");
				loot.append("id " + l.getId() + ".\n");
				loot.append("letter " + l.getLetter());
				l.getItems().forEach(item -> {
					loot.append(".\n" + item);
				});
				loot.append("\n;\n");
			});
		} else {
			loot = new StringBuilder();
		}

		StringBuilder material;
		if (!mats.isEmpty()) {
			List<String> materials = converMaterials(mats);
			material = new StringBuilder("materials\n" + materials.get(0));
			for (int i = 1; i < materials.size(); i++) {
				material.append(".\n" + materials.get(i));
			}
		} else {
			material = new StringBuilder();
		}

		StringBuilder layer;
		if (!layers.isEmpty()) {
			layer = new StringBuilder("\n;\nlayer\n");
			layers.forEach((id, list) -> {
				layer.append("id " + id);
				list.forEach(l -> {
					layer.append(".\n" + l);
				});
				layer.append("\n;\nlayer\n");
			});
		} else {
			layer = new StringBuilder();
		}

		File folder = new File(Main.MAIN.getDataFolder() + File.separator + "saved-structfiles");
		if (!folder.exists())
			folder.mkdirs();

		File f = new File(folder, name + ".festruct");

		Writer fw = null;
		FileOutputStream fos = null;
		
		try {
			fos = new FileOutputStream(f);
			fw = new OutputStreamWriter(fos, StandardCharsets.UTF_16);
			f.createNewFile();
			fw.write("rarity "+rarity+"\n");
			fw.write(";\n");
			fw.write(entity.toString());
			fw.write(loot.toString());
			fw.write(material.toString());
			fw.write(layer.toString());

			p.sendMessage("Successfully saved " + name + ".festruct !");
			fw.close();
			fos.close();
		} catch (IOException e1) {
			e1.printStackTrace();
			p.sendMessage("There was an error trying to save " + name + ".festruct ! More info in console!" );
		}
	}

	/**
	 * This function is used to retrieve a List of item information in that chest
	 * 
	 * @param c is the chest
	 * @return a List<String>, strings look like this: minecraft:diamond>3
	 */
	private List<String> getItemsInChest(Chest c) {
		List<Material> mats = new ArrayList<Material>();
		List<String> items = new ArrayList<String>();
		for (int i = 0; i < c.getInventory().getSize(); i++) {
			ItemStack is = c.getInventory().getItem(i);
			if (is != null)
				if (!mats.contains(is.getType())) {
					mats.add(is.getType());
					items.add(is.getType().getKey()/*"minecraft:" + is.getType().name().toLowerCase()*/ + ">" + is.getAmount());
				}
		}
		return items;
	}

	/**
	 * @param b is the block to get the code for
	 * @param mats is the materials generated by {@link #getMaterials()}
	 * @return the found code.
	 */
	private String getMaterialCode(Block b, Map<String, String> mats) {
		for (String code : mats.keySet()) {
			if (mats.get(code).equals(b.getBlockData().getAsString()))
				return code;
		}
		return null;
	}
	
	List<String> already = new ArrayList<String>();
	/**
	 * @param b is the Chest to get code fore
	 * @param mats is the materials generated by {@link #getMaterials()}
	 * @return the found code.
	 */
	private String getMaterialCodeForChest(Block b, Map<String, String> mats) {
		for (String code : mats.keySet()) {
			if (mats.get(code).equals(b.getBlockData().getAsString()) && !already.contains(code)) {
				already.add(code);
				return code;
			}
		}
		return null;
	}

	/**
	 * This is used to generator custom codes to materials found in the structure
	 * 
	 * @return the map with the codes and the material 
	 */
	private Map<String, String> getMaterials() {
		List<BlockData> datas = new ArrayList<BlockData>();
		for (Block b : this.blocks) {
			datas.add(b.getBlockData());
		}
		Map<String, String> mats2 = new HashMap<String, String>();
		for (BlockData d : datas) {
			if (d.getMaterial() != Material.CHEST) {
				if (!mats2.containsValue(d.getAsString()))
					mats2.put(getRandomCharacter(), d.getAsString());
			} else {
				mats2.put(getRandomCharacter(), d.getAsString());
			}
		}
		return mats2;
	}

	/**
	 * This will convert the map into a ArrayList in order to write to files more easily
	 * 
	 * @param mats is the map to convert
	 * @return the create ArrayList of Strings
	 */
	private List<String> converMaterials(Map<String, String> mats) {
		List<String> materials = new ArrayList<String>();
		mats.forEach((code, material) -> {
			materials.add(material + ">" + code);
		});
		return materials;
	}

	private List<String> blacklist = new ArrayList<String>();

	/**
	 * @return a random characther which hasn't been created before.
	 */
	RandomString gen = new RandomString();
	private String getRandomCharacter() {
		String s = gen.nextString();//RandomStringUtils.randomAlphabetic(1);
		if (s == ":" || s == ">" || s == "," || s == ";" || s == "." || blacklist.contains(s))
			return getRandomCharacter();
		blacklist.add(s);
		return s;
	}

	/**
	 * @return the number of layers in the structure.
	 */
	public int getNumoflayers() {
		return numoflayers;
	}

	/**
	 * @return the number of blocks in a layer
	 */
	public int getNumofblocksinlayer() {
		return numofblocksinlayer;
	}

	/**
	 * @return the list of blocks
	 */
	public List<Block> getBlocks() {
		return blocks;
	}
}
