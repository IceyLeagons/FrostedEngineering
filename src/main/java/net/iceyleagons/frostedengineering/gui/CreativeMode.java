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
package net.iceyleagons.frostedengineering.gui;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import dev.arantes.inventorymenulib.PaginatedGUIBuilder;
import dev.arantes.inventorymenulib.buttons.ClickAction;
import dev.arantes.inventorymenulib.buttons.ItemButton;
import dev.arantes.inventorymenulib.menus.PaginatedGUI;
import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.items.FrostedItems;

public class CreativeMode {

    public static void open(Player p) {
        ArrayList<ItemButton> items = new ArrayList<ItemButton>();
        Main.customBases.forEach(tb -> {
            items.add(new ItemButton(tb.getItem()));
        });
        items.add(new ItemButton(FrostedItems.STORAGE));
        PaginatedGUI gui = new PaginatedGUIBuilder("§8Menu | page: {page}",
                "xxxxxxxxx" + "x#######x" + "<#######>" + "x#######x" + "xxxxxxxxx")

                // Define the material of the border and the name.
                // You can also define actions if you want the border to be clickable
                .setBorder(new ItemButton(FrostedItems.INVENTORY_FILLER))

                // Set an item in the 4 position of the hotbar (lastrow)
                .setHotbarButton((byte) 4,
                        new ItemButton(Material.COMPASS, 1, "§cClose").addAction(ClickType.LEFT,
                                (InventoryClickEvent e) -> e.getWhoClicked().closeInventory()))

                // Set the item for the next page button.
                .setNextPageItem(Material.ARROW, 1, "§6Next page")

                // Set the item for the previous page buttom.
                .setPreviousPageItem(Material.ARROW, 1, "§6Previous page")
                .setContent(items)
                .setContentDefaultAction(new ClickAction() {

                    @Override
                    public void run(InventoryClickEvent ev) {
                        ev.setCancelled(false);
                        Bukkit.getScheduler().runTaskLater(Main.MAIN, () -> {
                            ev.getClickedInventory().setItem(ev.getSlot(), items.get(ev.getSlot() - 10).getItem());
                        }, 1L);
                    }
                })
                // Build and return the PaginatedGUI
                .build();

        gui.show(p);
    }

}
