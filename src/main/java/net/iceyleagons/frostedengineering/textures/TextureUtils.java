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
package net.iceyleagons.frostedengineering.textures;

import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
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
        itemMeta.setDisplayName(title);
        itemMeta.setCustomModelData(id);

        return itemMeta;
    }

    public static TexturedItem getMainOrOffHandItem(PlayerEvent e) {
        if (Textures.isTexturedItem(e.getPlayer().getInventory().getItemInMainHand()))
            return Textures.getTexturedItem(e.getPlayer().getInventory().getItemInMainHand());
        else if (Textures.isTexturedItem(e.getPlayer().getInventory().getItemInOffHand()))
            return Textures.getTexturedItem(e.getPlayer().getInventory().getItemInOffHand());

        return null;
    }

    public static void setMainOrOffHandStack(PlayerEvent e, ItemStack stack) {
        if (Textures.isTexturedItem(e.getPlayer().getInventory().getItemInMainHand()))
            e.getPlayer().getInventory().setItemInMainHand(stack);
        else if (Textures.isTexturedItem(e.getPlayer().getInventory().getItemInOffHand()))
            e.getPlayer().getInventory().setItemInOffHand(stack);
    }
}
