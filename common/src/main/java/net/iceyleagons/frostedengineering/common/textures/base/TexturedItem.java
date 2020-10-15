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
package net.iceyleagons.frostedengineering.common.textures.base;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import net.iceyleagons.frostedengineering.common.textures.TextureUtils;

public class TexturedItem extends TexturedBase {

    private String title;
    private List<String> lore;

    /**
     * Creates a new TexturedItem instance
     *
     * @param plugin       the base plugin which contains the assets.zip
     * @param name         the name of the item (used in give)
     * @param modelPath    the path to the model in the assets.zip
     * @param baseMaterial the base material you want to "extend"
     */
    public TexturedItem(JavaPlugin plugin, String name, String modelPath, Material baseMaterial) {
        super(plugin, name, modelPath, baseMaterial);
    }

    /**
     * Creates a new TexturedItem instance
     *
     * @param plugin       the base plugin which contains the assets.zip
     * @param name         the name of the item (used in give)
     * @param modelPath    the path to the model in the assets.zip
     * @param lore         the lore of the item
     * @param baseMaterial the base material you want to "extend"
     */
    public TexturedItem(JavaPlugin plugin, String name, String modelPath, List<String> lore, Material baseMaterial) {
        super(plugin, name, modelPath, baseMaterial);
        this.lore = lore;
    }

    /**
     * Creates a new TexturedItem instance
     *
     * @param plugin       the base plugin which contains the assets.zip
     * @param name         the name of the item (used in give)
     * @param modelPath    the path to the model in the assets.zip
     * @param lore         the lore of the item
     * @param title        the display name of the item
     * @param baseMaterial the base material you want to "extend"
     */
    public TexturedItem(JavaPlugin plugin, String name, String modelPath, List<String> lore, String title,
                        Material baseMaterial) {
        super(plugin, name, modelPath, baseMaterial);
        this.lore = lore;
        this.title = title;
    }

    /**
     * Creates a new TexturedItem instance
     *
     * @param plugin       the base plugin which contains the assets.zip
     * @param name         the name of the item (used in give)
     * @param modelPath    the path to the model in the assets.zip
     * @param title        the display name of the item
     * @param baseMaterial the base material you want to "extend"
     */
    public TexturedItem(JavaPlugin plugin, String name, String modelPath, String title, Material baseMaterial) {
        super(plugin, name, modelPath, baseMaterial);
        this.title = title;
    }

    /**
     * Returns the item of this TexturedItem
     */
    @Override
    public ItemStack getItem() {
        ItemStack item = new ItemStack(super.getBaseMaterial());
        item.setItemMeta(TextureUtils.createItemMeta(item, title, super.getId()));

        if (lore != null) {
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);
        }

        return item;
    }

    /**
     * This method gets run every time the player switches to a slot with a custom
     * item in it
     *
     * @param player the player who changed the slot
     * @param slot   the slot the player changed to
     */
    public void onSlotChange(Player player, int slot) {
        // Nothing, we let the user implement stuff like this.
    }

    /**
     * This method gets run every time the player interacts with another entity
     * whilst holding a custom item in it
     *
     * @param event the event thrown
     */
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        // Nothing, we let the user implement stuff like this.
    }

    /**
     * This method gets run every time the player interacts whilst holding a custom
     * item in it
     *
     * @param event the event thrown
     */
    public void onInteract(PlayerInteractEvent event) {
        // Nothing, we let the user implement stuff like this.
    }

}
