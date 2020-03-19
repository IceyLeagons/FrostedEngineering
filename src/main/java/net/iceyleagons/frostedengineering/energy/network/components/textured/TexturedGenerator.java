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
package net.iceyleagons.frostedengineering.energy.network.components.textured;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.energy.network.EnergyNetwork;
import net.iceyleagons.frostedengineering.energy.network.Unit;
import net.iceyleagons.frostedengineering.energy.network.components.Generator;
import net.iceyleagons.frostedengineering.textures.base.TexturedBlock;

public class TexturedGenerator extends TexturedBlock {

	Map<Location, Generator> generators = new HashMap<Location, Generator>();

	public TexturedGenerator() {
		super(Main.MAIN, "coal_generator", "block/coal_generator", "§rCoal Generator");
	}

	public Generator generateNewInstanceAtLocation(Location loc, EnergyNetwork net, float generates) {
		Generator g = new Generator(loc, net, generates);
		generators.put(loc, g);
		return g;
	}

	@Override
	public void onBroken(BlockBreakEvent e) {
		Unit u = Unit.getUnitAtLocation(e.getBlock().getLocation());
		if (u != null) {
			u.destroy();
		}
		updateMap();
	}

	@Override
	public void onPlacement(Block block, Player player) {
		generateNewInstanceAtLocation(block.getLocation(), new EnergyNetwork(), 2f);
	}

	@Override
	public void onInteract(PlayerInteractEvent event) {
		updateMap();

		if (generators.get(event.getClickedBlock().getLocation()) != null) {
			generators.get(event.getClickedBlock().getLocation()).open(event.getPlayer());
		}
	}

	private void updateMap() {
		Iterator<Location> it = generators.keySet().iterator();
		try {
			while (it.hasNext()) {
				Location loc = it.next();
				if (loc != null)
					if (Unit.getUnitAtLocation(loc) == null)
						generators.remove(loc);
			}
		} catch (ConcurrentModificationException ignore) {
		}
	}

}
