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
package net.iceyleagons.frostedengineering.energy.helpers;

import org.bukkit.Material;
import org.bukkit.event.block.BlockPlaceEvent;

import net.iceyleagons.frostedengineering.energy.network.EnergyNetwork;
import net.iceyleagons.frostedengineering.energy.network.components.Storage;
import net.md_5.bungee.api.ChatColor;

/**
 * This class will handle all the energy component related placement stuff.
 * 
 * @author TOTHT
 *
 *
 */
public class PlacementHandler {
	
	public static void place(BlockPlaceEvent e) {
		placeStorage(e);
	}
	
	private static void placeStorage(BlockPlaceEvent e) {
		if (e.getBlock().getType() == Material.COBBLESTONE) {
			if (e.getItemInHand().getItemMeta().getDisplayName().contains("§b§lStorage")) {
				if (ChatColor.stripColor(e.getItemInHand().getLore().get(3).split("#")[1]) != "0000") {
					String s = ChatColor.stripColor(e.getItemInHand().getLore().get(1).split(":")[1]);
					s = s.substring(1, s.length()-3);
					float stores = Float.parseFloat(s);
					
					new Storage(e.getBlock().getLocation(), new EnergyNetwork(), stores,
							ChatColor.stripColor(e.getItemInHand().getLore().get(3).split("#")[1]));
				}else {
					String s = ChatColor.stripColor(e.getItemInHand().getLore().get(1).split(":")[1]);
					s = s.substring(1, s.length()-3);
					float stores = Float.parseFloat(s);
					
					new Storage(e.getBlock().getLocation(), new EnergyNetwork(), stores);
				}
			}
		}
	}

}
