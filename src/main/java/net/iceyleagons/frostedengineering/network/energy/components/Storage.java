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
import net.iceyleagons.frostedengineering.network.energy.EnergyUnit;

public abstract class Storage extends EnergyUnit implements ITick {

    protected float maxStores = 1.0f; //how many FP it can store
    protected float stores = 0.0f;
    public boolean full = false;

    /**
     * @param loc       is the location of the unit
     * @param network   is the energy network it's in
     * @param maxStores the capacity of the storage
     */
    public Storage(Location loc, EnergyNetwork network, float maxStores) {
        super(loc, network);
        this.maxStores = maxStores;
        Main.debug("Creating storage...");
        initInventory();
        Main.registerITick(this);
    }

    /**
     * @param loc       is the location of the unit
     * @param network   is the energy network it's in
     * @param uuid      is the uuid of the Unit (used when loading)
     * @param maxStores the capacity of the storage
     */
    public Storage(Location loc, EnergyNetwork network, UUID uuid, float maxStores, List<ItemStack> itemsInside) {
        super(loc, network, uuid, itemsInside);
        this.maxStores = maxStores;
        Main.debug("Creating storage...");
        Main.registerITick(this);
        initInventory();
    }


    /**
     * @return the capacity of the storage
     */
    public float getMaxStorage() {
        return maxStores;
    }

    /**
     * @return the currently stored energy
     */
    public float getStored() {
        return stores;
    }

    /**
     * Adds power to the storage, if the capacity is breached the storage can explode
     *
     * @param fp is the power to add to the storage
     * @return the remaining power if the storage gets full, the main logic is handled by the energy network.
     */
    public abstract float addPower(float fp);


    /**
     * This function is used to create our inventory.
     */
    public abstract void initInventory();

    /**
     * This function is used to update our inventory.
     * However it only runs 1/1s to not freak bukkit out.
     */
    public abstract void updateInventory();

    /**
     * @param fp is the power needed
     * @return the remainig power to discharge, the main logic is handled by the energy network.
     */
    public abstract float consumePower(float fp);


    @Override
    public abstract List<ItemStack> getItemsInside();

}

