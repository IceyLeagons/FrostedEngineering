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
package net.iceyleagons.frostedengineering.network.energy.components;

import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.interfaces.ITick;
import net.iceyleagons.frostedengineering.network.energy.EnergyNetwork;
import net.iceyleagons.frostedengineering.network.energy.EnergyNetworkType;
import net.iceyleagons.frostedengineering.network.energy.EnergyUnit;
import net.iceyleagons.frostedengineering.network.energy.interfaces.ExplodableComponent;

public abstract class Consumer extends EnergyUnit implements ITick, ExplodableComponent {

    protected float consumes = 1.0f; // how many FP it consumes/second
    protected boolean enabled = false;
    protected EnergyNetworkType capable;

    /**
     * @param loc      is the {@link Location} of the Consumer
     * @param network  is the {@link EnergyNetwork} of this {@link EnergyUnit}
     * @param consumes is the amount of FrostedPower it consumes on every single
     *                 ticks.
     */
    public Consumer(Location loc, EnergyNetwork network, float consumes, EnergyNetworkType capable) {
        super(loc, network);
        this.capable = capable;
        this.consumes = consumes;
        Main.registerITick(this);
        Main.debug("Creating consumer...");
    }

    /**
     * @param loc         is the {@link Location} of the Consumer
     * @param network     is the {@link EnergyNetwork} of this {@link EnergyUnit}
     * @param uuid        is the uuid of the Unit (used when loading)
     * @param consumes    is the amount of FrostedPower it consumes on every single
     *                    ticks.
     * @param itemsInside is the items inside the Unit (used when loading)
     */
    public Consumer(Location loc, EnergyNetwork network, UUID uuid, float consumes, EnergyNetworkType capable, List<ItemStack> itemsInside) {
        super(loc, network, uuid, itemsInside);
        this.capable = capable;
        this.consumes = consumes;
        Main.registerITick(this);
        Main.debug("Creating consumer...");
    }
	
	
	/*
	 * 		if (destroy == false) {
			//if (NetworkType.doExplode(getCapable(), getNetwork().getType()))
				//this.explode();
			
			enabled = getNetwork().consumeFP(consumes);
			
			if (enabled) {
				getLocation().getBlock().setType(Material.EMERALD_BLOCK);
			} else {
				getLocation().getBlock().setType(Material.STONE);
			}
		}
	 */

    /**
     * @return the amount of FrostedPower this consumes on every single ticks.
     */
    public float getConsuming() {
        return consumes;
    }

    public EnergyNetworkType getCapable() {
        return this.capable;
    }

    @Override
    public void explode() {
        //this.destroy();
        //if (destroy == false) {
        //	this.getLocation().getWorld().playSound(this.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1L, 1L);
        //}
    }

    @Override
    public abstract List<ItemStack> getItemsInside();

}
