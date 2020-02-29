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
