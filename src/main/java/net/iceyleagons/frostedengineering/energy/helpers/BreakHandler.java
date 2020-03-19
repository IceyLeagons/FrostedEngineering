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
