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

package net.iceyleagons.frostedengineering.network.interfaces;

import net.iceyleagons.frostedengineering.gui.InventoryFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Used for {@link net.iceyleagons.frostedengineering.network.Unit}s that needs some sort of GUI.
 *
 * @author TOTHTOMI
 */
public interface InteractiveComponent {

    /**
     * @return a {@link List} of {@link ItemStack}s in that Inventory, the items here are added by the developer
     */
    List<ItemStack> getItemsInside();

    /**
     * Updates the inventory, mainly used with {@link net.iceyleagons.frostedengineering.interfaces.ITick}
     */
    void updateInventory();

    /**
     * Used for initializing the {@link InventoryFactory}, and for initializing any other objects related to the GUI part of the {@link net.iceyleagons.frostedengineering.network.Unit}
     */
    void initInventory();

    /**
     * @return the {@link InventoryFactory} of the {@link net.iceyleagons.frostedengineering.network.Unit}
     */
    InventoryFactory getInventoryFactory();

    /**
     *
     * Opens the {@link InventoryFactory} for the player, please use {@link InventoryFactory#openInventory(Player)}
     *
     * @param player the {@link Player} to open the Inventory for.
     */
    void open(Player player);

    /**
     *
     * @return the list of {@link GUIElement}s
     */
    List<GUIElement> getGUIElements();

}
