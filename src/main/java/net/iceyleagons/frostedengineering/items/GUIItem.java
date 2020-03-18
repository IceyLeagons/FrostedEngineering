package net.iceyleagons.frostedengineering.items;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import net.iceyleagons.frostedengineering.textures.base.TexturedItem;

public class GUIItem extends TexturedItem {

	public GUIItem(JavaPlugin plugin) {
		super(plugin, "guiitem", "item/customgui", " ", Material.DIAMOND_HOE);
	}

}
