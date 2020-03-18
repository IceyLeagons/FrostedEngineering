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
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.energy.interfaces.Visitable;

/**
 * @author TOTHT
 *
 *         This class handles the connecting,disconnection ability of an energy
 *         network.
 *
 */
public class Tracer {

	private List<Unit> units = new ArrayList<Unit>();
	private List<Visitable> visited = new ArrayList<Visitable>();
	private int taskstatus = 5;
	private BukkitTask task = null;

	public void merge(Unit start) {
		taskstatus = 5;
		task = Bukkit.getScheduler().runTaskTimer(Main.MAIN, new Runnable() {
			@Override
			public void run() {
				taskstatus--;
				if (taskstatus == 0) {
					stopMerging();
					visited.forEach(v -> {
						v.unvisit();
					});
					visited.clear();
				}
			}
		}, 0L, 5L);
		Iterator<Unit> startNeighbours = start.getNeighbours().listIterator();
		EnergyNetwork toset = start.getNetwork();
		start.visit();
		visited.add(start);
		while (startNeighbours.hasNext()) {
			Unit neighbourofstart = startNeighbours.next();
			modifyEnergyNetwork(neighbourofstart, toset);
			neighbourofstart.visit();
			visited.add(neighbourofstart);
			merge(neighbourofstart, toset);
		}

	}

	private void stopMerging() {
		if (task != null)
			task.cancel();
	}

	private void modifyEnergyNetwork(Unit u, EnergyNetwork toset) {
		u.getNetwork().removeUnit(u);
		u.setNetwork(toset);
		toset.addUnit(u);
	}

	private void merge(Unit start, EnergyNetwork toset) {
		Iterator<Unit> startNeighbours = start.getNeighbours().listIterator();
		while (startNeighbours.hasNext()) {
			Unit neighbourofstart = startNeighbours.next();
			if (!neighbourofstart.isVisited()) {
				modifyEnergyNetwork(neighbourofstart, toset);
				neighbourofstart.visit();
				visited.add(neighbourofstart);
				merge(neighbourofstart, toset);
				taskstatus = 5;
			}
		}
	}

	boolean debounce = false;

	/**
	 * This is the starting method of the splitting function of an
	 * {@link EnergyNetwork}
	 * 
	 * @param start is the {@link Unit} where to start
	 */
	public void splitUpdateStack(Unit start) {
		Main.debug("Updating energy networks... (Splitting)");
		taskstatus = 5;
		task = Bukkit.getScheduler().runTaskTimer(Main.MAIN, new Runnable() {
			@Override
			public void run() {
				taskstatus--;
				if (taskstatus == 0) {
					stopMerging();
					visited.forEach(v -> {
						v.unvisit();
					});
					visited.clear();
				}
			}
		}, 0L, 5L);
		Iterator<Unit> it = start.getNeighbours().listIterator();
		while (it.hasNext()) {
			Unit target = it.next();
			EnergyNetwork net = new EnergyNetwork();
			target.getNetwork().removeUnit(start);
			target.getNetwork().removeUnit(target);
			target.setNetwork(net);
			net.addUnit(target);
			target.getNeighbours().remove(start);
			// target.visited = true;
			target.visit();
			units.add(target);
			split(target);

		}
		start.getNetwork().removeUnit(start);
		Unit.removeUnit(start);
	}

	/**
	 * This function will split the {@link EnergyNetwork} into proper sections.
	 * 
	 * @param u is the starting {@link Unit}
	 */
	private void split(Unit u) {
		Iterator<Unit> it = u.getNeighbours().listIterator();
		while (it.hasNext()) {
			Unit target = it.next();
			if (target != u && target.isVisited() == false) {
				target.visit();
				target.getNetwork().removeUnit(target);
				target.getNetwork().removeUnit(u);
				target.setNetwork(u.getNetwork());
				u.getNetwork().addUnit(target);
				units.add(target);
				split(target);
			}
		}
	}

}
