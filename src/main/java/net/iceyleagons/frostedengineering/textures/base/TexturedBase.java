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
package net.iceyleagons.frostedengineering.textures.base;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class TexturedBase {

	private Material baseMaterial;
	private String name;
	private String modelPath;
	private Plugin plugin;
	private int id;

	public TexturedBase(JavaPlugin plugin, String name, String modelPath, Material baseMaterial) {
		this.baseMaterial = baseMaterial;
		this.name = name;
		this.modelPath = modelPath;
		this.plugin = plugin;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getModel() {
		return modelPath;
	}

	public Material getBaseMaterial() {
		return baseMaterial;
	}

	public Plugin getPlugin() {
		return plugin;
	}

	public ItemStack getItem() {
		return null;
	}

	public Recipe getRecipe() {
		return null;
	}

}
