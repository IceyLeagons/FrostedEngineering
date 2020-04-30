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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.interfaces.ISecond;
import net.iceyleagons.frostedengineering.interfaces.ITick;
import net.iceyleagons.frostedengineering.network.energy.EnergyNetwork;
import net.iceyleagons.frostedengineering.network.energy.EnergyUnit;

public abstract class Generator extends EnergyUnit implements ITick, ISecond {

    protected float generates = 1.0f; // how many FP it generates/tick
    protected boolean enabled = true;
    protected int remainigBurn = 0;

    /**
     * @param loc       is the {@link Location} of the Generator
     * @param network   is the {@link EnergyNetwork} of this {@link EnergyUnit}
     * @param generates is the amount of FrostedPower it generates on every single
     *                  game tick
     */
    public Generator(Location loc, EnergyNetwork network, float generates) {
        super(loc, network);
        this.generates = generates;
        Main.registerITick(this);
        Main.registerISecond(this);
        Main.debug("Creating generator...");
        initInventory();
    }

    /**
     * @param loc         is the {@link Location} of the Generator
     * @param network     is the {@link EnergyNetwork} of this {@link EnergyUnit}
     * @param uuid        is the uuid of the Unit (used when loading)
     * @param generates   is the amount of FrostedPower it generates on every single
     *                    game tick
     * @param itemsInside is the items inside the Unit (used when loading)
     */
    public Generator(Location loc, EnergyNetwork network, UUID uuid, float generates, List<ItemStack> itemsInside) {
        super(loc, network, uuid, itemsInside);
        this.generates = generates;
        Main.registerITick(this);
        Main.registerISecond(this);
        Main.debug("Creating generator...");
        initInventory();
    }

    /**
     * @return the amount of FrostedPower it generates on every single game tick
     */
    public float getGenerates() {
        return generates;
    }

    /**
     * @return true if the {@link Generator} is working
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @param enabled is a boolean to set the Generator's state to, aka. to enable
     *                or disable it
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * This function is used to create our inventory.
     */
    public abstract void initInventory();

    /**
     * This function is used to update our inventory. However it only runs 1/1s to
     * not freak bukkit out.
     */
    public abstract void updateInventory();

    public void open(Player p) {
        if (!super.isDestroyed()) {
            if (super.getInventoryFactory() == null)
                initInventory();
            super.getInventoryFactory().openInventory(p);
        }
    }

    public void update() {
        enabled = (remainigBurn != 0) ? true : false;
    }


    @Override
    public abstract List<ItemStack> getItemsInside();

}
