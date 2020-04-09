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
package net.iceyleagons.frostedengineering.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Location;

import net.iceyleagons.frostedengineering.network.energy.EnergyNetwork;
import net.iceyleagons.frostedengineering.network.energy.EnergyUnit;

/**
 * @author TOTHT
 *
 *         This class handles the connecting,disconnection ability of an energy
 *         network.
 *
 */
public class Tracer {

	private List<Unit> visited;
	private Location start;

	/**
	 * @param start is the start location (use {@link EnergyUnit#getLocation()})
	 */
	public Tracer(Location start) {
		this.start = start;
		visited = new ArrayList<Unit>();

	}

	/**
	 * This is used to easily modify EnergyNetworks
	 * 
	 * @param unit       is the target
	 * @param newNetwork is the {@link EnergyNetwork} to set the {@link EnergyUnit}'s
	 *                   EnergyNetwork to
	 */
	private void modifyUnitNetwork(Unit unit, Network newNetwork) {
		if (unit.getNetwork() != null)
			unit.getNetwork().removeUnit(unit);
		unit.setNetwork(newNetwork);
		unit.getNetwork().addUnit(unit);
	}

	/*
	 * Splitting
	 */
	/**
	 * This will split the networks at that location. (Ex.: If you have 3 units next
	 * to each other & break the middle one there will be 2 EnergyNetworks)
	 */
	public void splitNetworks() {
		visited.clear();
		Unit startUnit = getUnitAtLocation(start);
		if (startUnit != null) {
			visited.add(startUnit);
			startUnit.getNetwork().removeUnit(startUnit);
			startUnit.setDestroyed(true);
			startUnit.setNetwork(null);
			Tracer.removeUnit(startUnit);
			Iterator<Unit> startunitNeighbours = startUnit.getNeighbours().iterator();
			while (startunitNeighbours.hasNext()) {
				Unit startunitNeighbour = startunitNeighbours.next();
				EnergyNetwork net = new EnergyNetwork();
				System.out.println(net.toString());
				net.addUnit(startunitNeighbour);
				startunitNeighbour.getNetwork().removeUnit(startunitNeighbour);
				startunitNeighbour.setNetwork(net);
				new Tracer(startunitNeighbour.getLocation()).mergeIntoOneNetworkForSplitting(startUnit);
			}
		}

	}

	/*
	 * Merging
	 */

	/**
	 * This will merge networks together only used by the Tracer itself (in
	 * {@link #splitNetworks()}).
	 * 
	 * @param startU is the Unit to start from
	 */
	private void mergeIntoOneNetworkForSplitting(Unit startU) {
		visited.clear();
		visited.add(startU);
		Unit startUnit = getUnitAtLocation(start);
		if (startUnit != null) {
			visited.add(startUnit);
			Network newNetwork = startUnit.getNetwork();
			Iterator<Unit> startunitNeighbours = startUnit.getNeighbours().iterator();
			while (startunitNeighbours.hasNext()) {
				Unit startunitNeighbour = startunitNeighbours.next();
				if (!visited.contains(startunitNeighbour)) {
					modifyUnitNetwork(startunitNeighbour, newNetwork);
					visited.add(startunitNeighbour);
					mergeIntoOneNetwork(startunitNeighbour);
				}
			}
		}
	}

	/**
	 * This will merge networks together in to one EnergyNetwork
	 * 
	 */
	public void mergeIntoOneNetwork() {
		visited.clear();
		Unit startUnit = getUnitAtLocation(start);
		if (startUnit != null) {
			visited.add(startUnit);
			Network newNetwork = startUnit.getNetwork();
			Iterator<Unit> startunitNeighbours = startUnit.getNeighbours().iterator();
			while (startunitNeighbours.hasNext()) {
				Unit startunitNeighbour = startunitNeighbours.next();
				if (!visited.contains(startunitNeighbour)) {
					modifyUnitNetwork(startunitNeighbour, newNetwork);
					visited.add(startunitNeighbour);
					mergeIntoOneNetwork(startunitNeighbour);
				}
			}
		}
	}

	/**
	 * This is used for looping in {@link #mergeIntoOneNetwork()}
	 * (Note that we have a visited list to prevent infinite loops)
	 * 
	 * @param from is the Unit to start the madness again.
	 */
	private void mergeIntoOneNetwork(Unit from) {
		if (from != null) {
			Iterator<Unit> fromNeighbours = from.getNeighbours().iterator();
			while (fromNeighbours.hasNext()) {
				Unit fromNeighbour = fromNeighbours.next();
				if (!visited.contains(fromNeighbour)) {
					modifyUnitNetwork(fromNeighbour, from.getNetwork());
					visited.add(fromNeighbour);
					mergeIntoOneNetwork(fromNeighbour);
				}
			}
		}
	}
	
	public static HashMap<Location,Unit> units = new HashMap<Location,Unit>();
	
	/**
	 * @param location is the location to check for
	 * @return a {@link Unit} if there is one at that location.
	 */
	public static Unit getUnitAtLocation(Location location) {
		return units.containsKey(location) ? units.get(location) : null;
	}
	
	public static void addUnit(Unit u) {
		if (u instanceof EnergyUnit) EnergyUnit.addUnit(u);
		units.put(u.getLocation(), u);
	}
	
	public static void removeUnit(Unit u) {
		if (u instanceof EnergyUnit) EnergyUnit.removeUnit(u);
		units.remove(u.getLocation());
	}
	
}
