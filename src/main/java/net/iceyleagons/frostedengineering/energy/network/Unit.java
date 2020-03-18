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
package net.iceyleagons.frostedengineering.energy.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.energy.interfaces.ISecond;
import net.iceyleagons.frostedengineering.energy.interfaces.ITick;
import net.iceyleagons.frostedengineering.energy.interfaces.Visitable;
import net.iceyleagons.frostedengineering.energy.network.components.Storage;
import net.iceyleagons.frostedengineering.gui.InventoryFactory;

public abstract class Unit implements Visitable {

	private Location loc;
	private List<Unit> neighbours;
	private EnergyNetwork network;
	private InventoryFactory inv;
	protected boolean destroy = false;
	private boolean visited = false;
	private Tracer tracer;

	/**
	 * @param loc     is the {@link Location} of unit
	 * @param network is the {@link EnergyNetwork} it's in(adding to the energy
	 *                network is done automatically)
	 */
	public Unit(Location loc, EnergyNetwork network) {
		this.loc = loc;
		this.network = network;
		this.network.addUnit(this);
		tracer = new Tracer();
		init();
	}

	/**
	 * This will be used by our algorithms
	 */
	public void destroy() {
		Main.debug("Destorying unit at location " + loc.toString());
		destroy = true;
		tracer.splitUpdateStack(this);
	}

	@Override
	public void visit() {
		visited = true;
	}
	
	@Override
	public boolean isVisited() {
		return visited;
	}
	
	@Override
	public void unvisit() {
		visited = false;
	}
	
	/**
	 * This is the same as above, but here we have a player, so we can give items,
	 * etc.
	 * 
	 * @param p is the {@link Player} of the destruction
	 */
	public void destroy(Player p) {
		Main.debug("Destorying unit at location " + loc.toString());
		destroy = true;
		tracer.splitUpdateStack(this);

		if (this instanceof Storage) {
			loc.getWorld().dropItemNaturally(loc, ((Storage) this).getItem());
		}
	}

	/**
	 * í This method will setup fatal properties to the Unit
	 */
	private void init() {
		Main.debug("Initializing Unit at location " + loc.toString());
		addUnit(this);
		neighbours = getNeighbours(getLocation());

		neighbours.forEach(u -> {
			u.neighbours.add(this);
		});

		/*
		 * Merging
		 */
		tracer.merge(this);
	}

	/**
	 * This is used for the units GUI
	 * 
	 * @param inv is the {@link InventoryFactory}
	 */
	public void setInventoryFactory(InventoryFactory inv) {
		this.inv = inv;
	}

	/**
	 * @return the Units {@link InventoryFactory}, aka. the GUI of it
	 */
	public InventoryFactory getInventoryFactory() {
		return this.inv;
	}

	/**
	 * This is used by our algorithms to set the Energy Network
	 * 
	 * @param net is the {@link EnergyNetwork} to set to
	 */
	public void setNetwork(EnergyNetwork net) {
		this.network = net;
	}

	/**
	 * @return the {@link EnergyNetwork} of this Unit
	 */
	public EnergyNetwork getNetwork() {
		return this.network;
	}

	/**
	 * @return the {@link Location} of this Unit
	 */
	public Location getLocation() {
		return loc;
	}

	/**
	 * @return the list of {@link Unit}s around this Unit
	 */
	public List<Unit> getNeighbours() {
		return neighbours;
	}

	/*
	 * Statics
	 */

	private static List<Unit> units = new ArrayList<Unit>();
	public static List<Location> elements = new ArrayList<Location>();
	public static List<ITick> tickListeners = new ArrayList<ITick>();
	public static List<ISecond> secondListeners = new ArrayList<ISecond>();
	public static Map<String, Storage> storageIds = new HashMap<String, Storage>();

	/**
	 * @param ID is the storage ID
	 * @return the found storage of this ID
	 */
	public static Storage getStorage(String ID) {
		return storageIds.get(ID);
	}

	/**
	 * @param ID is the storage ID
	 * @return true if MultipleSameStoragesExists with the ID
	 */
	public static boolean doMultipleSameStoragesExists(String ID) {
		List<String> list = new ArrayList<String>();
		for (Unit u : units) {
			Storage s = (Storage) u;
			if (list.contains(ID))
				return true;
			list.add(s.getID());

		}
		return false;
	}

	/**
	 * This will register the Unit in our lists so our listeners can work with them
	 * 
	 * @param u is the {@link Unit} to add to the list
	 */
	public static void addUnit(Unit u) {
		units.add(u);
	}

	/**
	 * This will remove the Unit in our lists so our listeners can work with them
	 * 
	 * @param u is the {@link Unit} to remove from the list
	 */
	public static void removeUnit(Unit u) {
		units.remove(u);
	}

	/**
	 * This is used to get all the Units near a location
	 * 
	 * @param loc is the {@link Location}
	 * @return the List of {@link Unit}s
	 */
	public static List<Unit> getNeighbours(Location loc) {
		Main.debug("Getting neighbour units for location " + loc.toString());
		List<Unit> units = new ArrayList<Unit>();

		Unit target = null;

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

	// TODO rewrite neighbour to neighbor

	/**
	 * This will return a neighbor at {@link Location}
	 * 
	 * @param start is the start {@link Location}
	 * @param x     is the amount of x coordinates added to the starting location
	 * @param y     is the amount of y coordinates added to the starting location
	 * @param z     is the amount of z coordinates added to the starting location
	 * @return the {@link Unit} at that {@link Location}, if it found one
	 */
	private static Unit getNeighbourAtLocation(Location start, int x, int y, int z) {
		Location target = start.clone().add(x, y, z);
		Main.debug("Getting unit at location " + start.toString());
		if (!target.equals(start)) {
			Unit targetUnit = getUnitAtLocation(target);
			if (targetUnit != null)
				return targetUnit;
		}
		return null;
	}

	/**
	 * 
	 * 
	 * @param loc the {@link Location}
	 * @return the {@link Unit} at that {@link Location} if it found one
	 */
	public static Unit getUnitAtLocation(Location loc) {
		Main.debug("Getting unit at location " + loc.toString());
		for (Unit u : units) {
			if (u.getLocation().equals(loc)) {
				return u;
			}
		}
		return null;
	}

}
