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
import net.iceyleagons.frostedengineering.energy.interfaces.ExplodableComponent;
import net.iceyleagons.frostedengineering.energy.interfaces.ITick;
import net.iceyleagons.frostedengineering.energy.network.EnergyNetwork;
import net.iceyleagons.frostedengineering.energy.network.NetworkType;
import net.iceyleagons.frostedengineering.energy.network.Unit;

public class Consumer extends Unit implements ITick, ExplodableComponent {

	private float consumes = 1.0f; // how many FP it consumes/second
	private boolean enabled = false;
	private NetworkType capable;

	/**
	 * @param loc      is the {@link Location} of the Consumer
	 * @param network  is the {@link EnergyNetwork} of this {@link Unit}
	 * @param consumes is the amount of FrostedPower it consumes on every single
	 *                 ticks.
	 */
	public Consumer(Location loc, EnergyNetwork network, float consumes, NetworkType capable) {
		super(loc, network);
		this.capable = capable;
		this.consumes = consumes;
		Unit.tickListeners.add(this);
		Main.debug("Creating consumer...");
	}

	@Override
	public void onTick() {
		if (destroy == false) {
			//if (NetworkType.doExplode(getCapable(), getNetwork().getType()))
				//this.explode();
			
			enabled = getNetwork().consumeFP(consumes);
			
			if (enabled) {
				getLocation().getBlock().setType(Material.EMERALD_BLOCK);
			} else {
				getLocation().getBlock().setType(Material.STONE);
			}
		}
	}

	/**
	 * @return the amount of FrostedPower this consumes on every single ticks.
	 */
	public float getConsuming() {
		return consumes;
	}

	public NetworkType getCapable() {
		return this.capable;
	}

	@Override
	public void explode() {
		//this.destroy();
		//if (destroy == false) {
		//	this.getLocation().getWorld().playSound(this.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1L, 1L);
		//}
	}

}
