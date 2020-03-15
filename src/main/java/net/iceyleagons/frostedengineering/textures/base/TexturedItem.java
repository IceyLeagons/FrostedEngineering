package net.iceyleagons.frostedengineering.textures.base;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import net.iceyleagons.frostedengineering.textures.TextureUtils;

public class TexturedItem extends TexturedBase {

	private String title;
	private List<String> lore;

	public TexturedItem(JavaPlugin plugin, String name, String modelPath, Material baseMaterial) {
		super(plugin, name, modelPath, baseMaterial);
	}

	public TexturedItem(JavaPlugin plugin, String name, String modelPath, List<String> lore, Material baseMaterial) {
		super(plugin, name, modelPath, baseMaterial);
		this.lore = lore;
	}

	public TexturedItem(JavaPlugin plugin, String name, String modelPath, List<String> lore, String title,
			Material baseMaterial) {
		super(plugin, name, modelPath, baseMaterial);
		this.lore = lore;
		this.title = title;
	}

	public TexturedItem(JavaPlugin plugin, String name, String modelPath, String title, Material baseMaterial) {
		super(plugin, name, modelPath, baseMaterial);
		this.title = title;
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(getBaseMaterial());
		item.setItemMeta(TextureUtils.createItemMeta(item, title, getId()));

		if (lore != null) {
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
		}

		return item;
	}

	public void onSlotChange(Player player, int slot) {
		// Nothing, we let the user implement stuff like this.
	}

	public void onInteractEntity(PlayerInteractEntityEvent event) {
		// Nothing, we let the user implement stuff like this.
	}

	public void onInteract(PlayerInteractEvent event) {
		// Nothing, we let the user implement stuff like this.
	}

}
