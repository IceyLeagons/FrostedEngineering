package net.iceyleagons.frostedengineering.textures;

import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import net.iceyleagons.frostedengineering.textures.base.TexturedItem;

public class TextureUtils {

	public static ItemMeta createItemMeta(ItemStack item, String title, int id) {
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
		itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
		itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		itemMeta.setUnbreakable(true);
		itemMeta.setDisplayName(title);
		((Damageable) itemMeta).setDamage(item.getType().getMaxDurability() - id);

		return itemMeta;
	}

	public static TexturedItem getMainOrOffHandItem(PlayerEvent e) {
		if (Textures.isTexturedItem(e.getPlayer().getInventory().getItemInMainHand())) {
			return Textures.getTexturedItem(e.getPlayer().getInventory().getItemInMainHand());
		} else if (Textures.isTexturedItem(e.getPlayer().getInventory().getItemInOffHand())) {
			return Textures.getTexturedItem(e.getPlayer().getInventory().getItemInOffHand());
		}
		return null;
	}

	public static void setMainOrOffHandStack(PlayerEvent e, ItemStack stack) {
		if (Textures.isTexturedItem(e.getPlayer().getInventory().getItemInMainHand())) {
			e.getPlayer().getInventory().setItemInMainHand(stack);
		} else if (Textures.isTexturedItem(e.getPlayer().getInventory().getItemInOffHand())) {
			e.getPlayer().getInventory().setItemInOffHand(stack);
		}
	}
}
