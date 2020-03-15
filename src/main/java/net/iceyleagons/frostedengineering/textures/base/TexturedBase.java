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
