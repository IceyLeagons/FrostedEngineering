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
package net.iceyleagons.frostedengineering.energy.network.components;

import org.bukkit.Location;
import org.bukkit.Material;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.energy.network.EnergyNetwork;
import net.iceyleagons.frostedengineering.energy.network.ITick;
import net.iceyleagons.frostedengineering.energy.network.Unit;

public class Consumer extends Unit implements ITick {

	private float consumes = 1.0f; //how many FP it consumes/tick
	private boolean enabled = false;
	
	public Consumer(Location loc, EnergyNetwork network, float consumes) {
		super(loc, network);
		this.consumes = consumes;
		Unit.tickListeners.add(this);
		Main.debug("Creating consumer...");
	}
	
	@Override
	public void onTick() {
		//enabled = destroy ? false : getNetwork().consumeFP(consumes);
		
		
		if (destroy == false)
		if (enabled) {
			getLocation().getBlock().setType(Material.EMERALD_BLOCK);
		} else {
			getLocation().getBlock().setType(Material.STONE);
		}
	}

	public float getConsuming() {
		return consumes;
	}
	

}
