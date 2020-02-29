package net.iceyleagons.frostedengineering.energy.helpers;

import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;

import net.iceyleagons.frostedengineering.energy.network.Unit;

/**
 * This class will handle all the energy component related break stuff.
 * 
 * @author TOTHT
 *
 *
 */
public class BreakHandler {
	
	public static void breakk(BlockBreakEvent e) {
		breakStorage(e);
	}
	
	private static void breakStorage(BlockBreakEvent e) {
		if (e.getBlock().getType() == Material.COBBLESTONE) {
			Unit u = Unit.getUnitAtLocation(e.getBlock().getLocation());
			if (u != null)
				u.destroy(e.getPlayer());
		}
	}

}
