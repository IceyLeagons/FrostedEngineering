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
package net.iceyleagons.frostedengineering.network.energy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import net.iceyleagons.frostedengineering.network.Tracer;
import net.iceyleagons.frostedengineering.network.energy.EnergyNetwork;
import net.iceyleagons.frostedengineering.network.energy.exceptions.UnsupportedUnitType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.gui.InventoryFactory;
import net.iceyleagons.frostedengineering.network.Network;
import net.iceyleagons.frostedengineering.network.Unit;

/**
 * @author TOTHT
 * <p>
 * This is the smallest unit of an {@link EnergyNetwork} all the
 * components have relations with this superclass (except
 * Transformators, those aren't units)
 */
public abstract class EnergyUnit implements Unit, Serializable {

    private Location loc;
    private List<EnergyUnit> neighbours;
    private EnergyNetwork network;
    private boolean destroy = false;
    private UUID uuid;
    private List<ItemStack> itemsInside;
    private boolean traceractivated = true;

    /**
     * @param loc     is the {@link Location} of unit
     * @param network is the {@link EnergyNetwork} it's in(adding to the energy
     *                network is done automatically)
     */
    public EnergyUnit(Location loc, EnergyNetwork network) throws UnsupportedUnitType {
        units.add(this);
        this.loc = loc;
        this.network = network;
        this.network.addUnit(this);
        init();
        uuid = UUID.randomUUID();
        traceractivated = true;
    }

    /**
     * @param loc         is the {@link Location} of unit
     * @param network     is the {@link EnergyNetwork} it's in(adding to the energy
     *                    network is done automatically)
     * @param uuid        is the {@link UUID} of the Unit (used when loading)
     * @param itemsInside is the items inside the Unit (used when loading)
     */
    public EnergyUnit(Location loc, EnergyNetwork network, UUID uuid, List<ItemStack> itemsInside) throws UnsupportedUnitType {
        units.add(this);
        this.loc = loc;
        this.network = network;
        this.network.addUnit(this);
        init();
        this.uuid = uuid;
        this.itemsInside = itemsInside;
        traceractivated = false;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public boolean isDestroyed() {
        return destroy;
    }

    @Override
    public void setDestroyed(boolean bool) {
        destroy = bool;
    }

    /**
     * @return a list of {@link ItemStack} from loading in order to set it into the
     * inventory, items are in the same ordar as {@link #getItemsInside()}
     */
    public List<ItemStack> getLoadedItems() {
        return itemsInside;
    }

    @Override
    public void destroy() {
        Main.debug("Destorying unit at location " + loc.toString());
        setDestroyed(true);
        System.out.println("destory");
        Tracer.trace(this);//new TracerOld(this.getLocation()).splitNetworks();
        //closeInventory();
        //if (this instanceof Generator) {
        //    TexturedCoalGenerator.sound.stop(getLocation());
        //}
    }

    @Override
    public void destroy(Player p) {
        Main.debug("Destorying unit at location " + loc.toString());
        setDestroyed(true);
        Tracer.trace(this);
    }

    /**
     * Used for saving
     *
     * @return the list of items in the inventory
     */
    public abstract List<ItemStack> getItemsInside();

    /**
     * í This method will setup fatal properties to the Unit
     */
    private void init() {
        Main.debug("Initializing Unit at location " + loc.toString());
        Tracer.trace(this);
        neighbours = getNeighbours(getLocation());

        neighbours.forEach(u -> {
            u.neighbours.add(this);
        });

        /*
         * Merging
         */
        Tracer.trace(this);
    }


    /**
     * This is used by our algorithms to set the Energy Network
     *
     * @param net is the {@link EnergyNetwork} to set to
     * @deprecated use {@link #setNetwork(Network)}
     */
    @Deprecated
    public void setEnergyNetwork(EnergyNetwork net) {
        this.network = net;
    }

    /**
     * @return the {@link EnergyNetwork} of this Unit
     * @deprecated use {@link #getNetwork()}
     */
    @Deprecated
    public EnergyNetwork getEnergyNetwork() {
        return this.network;
    }

    public void setNetwork(Network net) {
        if (net == null) {
            this.network = null;
            return;
        }
        if (!(net instanceof EnergyNetwork))
            throw new IllegalArgumentException("Only EnergyNetwork can be supplied!");
        this.network = (EnergyNetwork) net;
    }

    @Override
    public Network getNetwork() {
        return this.network;
    }

    @Override
    public Location getLocation() {
        return loc;
    }

    /**
     * @return the locations around the block, note this won't return locations
     * diagonally
     */
    public List<Location> getLocationsAroundBlock() {
        List<Location> locs = new ArrayList<Location>();
        locs.add(getLocation().clone().add(1, 0, 0));
        locs.add(getLocation().clone().add(-1, 0, 0));
        locs.add(getLocation().clone().add(0, 1, 0));
        locs.add(getLocation().clone().add(0, -1, 0));
        locs.add(getLocation().clone().add(0, 0, 1));
        locs.add(getLocation().clone().add(0, 0, -1));
        return locs;
    }

    /**
     * @return the list of {@link EnergyUnit}s around this Unit
     */
    public List<EnergyUnit> getEnergyNeighbours() {
        return getNeighbours(getLocation());
    }

    @Override
    public List<Unit> getNeighbours() {
        List<Unit> units = new ArrayList<Unit>();
        for (EnergyUnit eu : getNeighbours(getLocation())) {
            units.add(eu);
        }
        return units;
    }

    /*
     * Statics
     */

    private static List<EnergyUnit> units = new ArrayList<EnergyUnit>();
    public static List<Location> elements = new ArrayList<Location>();

    /**
     * @return the all of the Units registered
     */
    public static List<EnergyUnit> getUnits() {
        return units;
    }

    /**
     * This will register the Unit in our lists so our listeners can work with them
     *
     * @param u is the {@link EnergyUnit} to add to the list
     */
    public static void addUnit(Unit u) {
        if (!(u instanceof EnergyUnit))
            throw new IllegalArgumentException("Only EnergyUnits can be supplied!");
        units.add((EnergyUnit) u);
    }

    /**
     * This will remove the Unit in our lists so our listeners can work with them
     *
     * @param u is the {@link EnergyUnit} to remove from the list
     */
    public static void removeUnit(Unit u) {
        if (!(u instanceof EnergyUnit))
            throw new IllegalArgumentException("Only EnergyUnits can be supplied!");
        units.remove((EnergyUnit) u);
    }

    /**
     * This is used to get all the Units near a location
     *
     * @param loc is the {@link Location}
     * @return the List of {@link EnergyUnit}s
     */
    public static List<EnergyUnit> getNeighbours(Location loc) {
        Main.debug("Getting neighbour units for location " + loc.toString());
        List<EnergyUnit> units = new ArrayList<EnergyUnit>();

        EnergyUnit target = null;

        target = getNeighbourAtLocation(loc, 0, 0, 1);
        if (target != null)
            units.add(target);

        target = getNeighbourAtLocation(loc, 0, 0, -1);
        if (target != null)
            units.add(target);

        target = getNeighbourAtLocation(loc, 1, 0, 0);
        if (target != null)
            units.add(target);

        target = getNeighbourAtLocation(loc, -1, 0, 0);
        if (target != null)
            units.add(target);

        target = getNeighbourAtLocation(loc, 0, 1, 0);
        if (target != null)
            units.add(target);

        target = getNeighbourAtLocation(loc, 0, -1, 0);
        if (target != null)
            units.add(target);

        return units;
    }

    /**
     * This will return a neighbour at {@link Location}
     *
     * @param start is the start {@link Location}
     * @param x     is the amount of x coordinates added to the starting location
     * @param y     is the amount of y coordinates added to the starting location
     * @param z     is the amount of z coordinates added to the starting location
     * @return the {@link EnergyUnit} at that {@link Location}, if it found one
     */
    private static EnergyUnit getNeighbourAtLocation(Location start, int x, int y, int z) {
        Location target = start.clone().add(x, y, z);
        Main.debug("Getting unit at location " + start.toString());
        if (!target.equals(start)) {
            EnergyUnit targetUnit = getEnergyUnitAtLocation(target);
            if (targetUnit != null)
                return targetUnit;
        }
        return null;
    }

    /**
     * @param loc the {@link Location}
     * @return the {@link EnergyUnit} at that {@link Location} if it found one
     */
    public static EnergyUnit getEnergyUnitAtLocation(Location loc) {
        Main.debug("Getting unit at location " + loc.toString());
        for (EnergyUnit u : units) {
            if (u.getLocation().equals(loc)) {
                return u;
            }
        }
        return null;
    }

}
