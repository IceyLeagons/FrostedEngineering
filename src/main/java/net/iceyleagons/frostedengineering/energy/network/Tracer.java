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

import net.iceyleagons.frostedengineering.Main;


public class Tracer {

	private List<Unit> units = new ArrayList<Unit>();

	public void updateStack(Unit start) {
		Main.debug("Updating energy networks... (Merging)");
		start.update();
		Iterator<Unit> it = start.getNeighbours().listIterator();
		while (it.hasNext()) {
			Unit target = it.next();
			if (target != start) {
				target.getNetwork().getUnits().forEach(u -> {
					u.setNetwork(start.getNetwork());
					start.getNetwork().addUnit(u);
					u.update();
				});
			}

		}
	}
	
	public void splitUpdateStack(Unit start) {
		Main.debug("Updating energy networks... (Splitting)");
		Iterator<Unit> it = start.getNeighbours().listIterator();
		while (it.hasNext()) {
			Unit target = it.next();
			EnergyNetwork net = new EnergyNetwork();
			target.getNetwork().removeUnit(target);
			target.setNetwork(net);
			net.addUnit(target);
			target.update();
			target.getNeighbours().remove(start);
			target.visited = true;
			units.add(target);
			split(target);
		}
		start.getNetwork().removeUnit(start);
		Unit.removeUnit(start);
		
		/*
		for (int j = 0; j < start.getNeighbours().size(); j++) {
			Unit target = start.getNeighbours().get(j);
			EnergyNetwork net = new EnergyNetwork();
			target.getNetwork().getUnits().remove(target);
			target.setNetwork(net);
			net.getUnits().add(target);
			target.update();
			target.getNeighbours().remove(start);
			target.visited = true;
			units.add(target);
			split(target);
		}
		*/
		Bukkit.getScheduler().runTaskLater(Main.MAIN, ()->{
			Iterator<Unit> iterator = units.iterator();
			while (iterator.hasNext()) {
				iterator.next().visited = false;
			}
			units.clear();
		}, 10L);
	}
	
	private void split(Unit u) {
		Iterator<Unit> it = u.getNeighbours().listIterator();
		while (it.hasNext()) {
			Unit target = it.next();
			if (target != u && target.visited == false) {
				target.visited = true;
				target.getNetwork().removeUnit(target);
				target.setNetwork(u.getNetwork());
				u.getNetwork().addUnit(target);
				System.out.println("IN process: "+u.getNetwork());
				target.update();
				units.add(target);
				split(target);
			}
		}
	}

}
