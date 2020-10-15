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
package net.iceyleagons.frostedengineering.common.gui;

import java.util.HashMap;
import java.util.Map;

import net.iceyleagons.frostedengineering.utils.InventoryFactory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.common.items.FrostedItems;
import net.iceyleagons.frostedengineering.common.textures.base.TexturedBlock;
import net.iceyleagons.frostedengineering.utils.CustomCrafting;

public class CustomCraftingTable extends TexturedBlock {

    public static Map<Location, InventoryFactory> craftingTableMap = new HashMap<>();

    public CustomCraftingTable(JavaPlugin plugin) {
        super(plugin, "crafting_table", "block/ccrafting_table", "§rCrafting Table");
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null)
            openCraftingTable(event.getClickedBlock().getLocation(), event.getPlayer());
    }


    @Override
    public void onBroken(BlockBreakEvent e) {
        if (craftingTableMap.containsKey(e.getBlock().getLocation())) {
            craftingTableMap.remove(e.getBlock().getLocation());
        }
    }

    @Override
    public void onPlacement(Block block, Player player) {
        if (!craftingTableMap.containsKey(block.getLocation())) {
            craftingTableMap.put(block.getLocation(), new InventoryFactory("Crafting Table", 54, FrostedItems.INVENTORY_FILLER, false));
        }
    }

    public static void openCraftingTable(Location location, Player player) {
        if (craftingTableMap.containsKey(location)) {
            InventoryFactory fac = craftingTableMap.get(location);
            for (int i = 0; i <= 4; i++) {
                for (int j = 2; j <= 6; j++) {
                    if (i > 0) {
                        fac.setItem(new ItemStack(Material.AIR), i * 9 + j);
                    } else {
                        fac.setItem(new ItemStack(Material.AIR), j);
                    }
                }
                if (i == 2) {
                    fac.setItem(new ItemStack(Material.AIR), i * 9);
                    fac.setItem(new ItemStack(Material.AIR), i * 9 + 8);
                }
                if (i == 4) {
                    fac.setItem(new ItemStack(Material.AIR), (i + 1) * 9);
                }
            }
            Bukkit.getScheduler().runTaskTimer(Main.MAIN, () -> {
                if (fac.isOpen()) {
                    CustomCrafting.list.forEach(cc -> {
                        cc.check(fac);
                    });
                }
            }, 0L, 2L);
        }
    }

}
