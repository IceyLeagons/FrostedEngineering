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
import java.util.List;
import java.util.Random;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.energy.network.components.Storage;

/**
 * The energy network and its unit all know each other(unit has a reference to the network, and vice versa)
 * The energy distribution and all the main logic is handled by this class.
 * 
 * Tracing and consuming,generating logic handled by the units themselves.
 * 
 * @author TOTHT
 *
 */
public class EnergyNetwork {

	private List<Unit> units;
	private List<Unit> storages;
	private float frostedpower = 0.0f;
	private float capacity = 5.0f;

	public EnergyNetwork() {
		units = new ArrayList<Unit>();
		storages = new ArrayList<Unit>();
		Main.debug("Creating energy network...");
	}

	/**
	 * This method is used to add Units to the network
	 * 
	 * @param u is the {@link Unit} to add
	 */
	public void addUnit(Unit u) {
		units.add(u);
		Main.debug("Adding unit " + u.toString() + " to network" + this.toString());
		updateStorages();
	}

	/**
	 * This method is used to remove Units from the network
	 * 
	 * @param u is the {@link Unit} to delete
	 */
	public void removeUnit(Unit u) {
		units.remove(u);
		Main.debug("Removing unit " + u.toString() + " from network" + this.toString());
		updateStorages();
	}

	/**
	 * This method is to add power to the network.
	 * 
	 * @param fp is the power to be added to the network
	 */
	public void generateFP(float fp) {
		calculateCapacity();
		addPowerToStorages(fp);
	}

	/**
	 * This is to consume frostedpower from the network.
	 * 
	 * @param fp is the needed power
	 * @return true if the consumer can work(aka. got the needed energy) or false if
	 *         it can't work.
	 */
	public boolean consumeFP(float fp) {
		calculateFrostedPower();
		if (hasEnoughFP(fp)) {
			removePowerFromStorages(fp);
			return true;
		} else {
			return false;
		}
	}


	/**
	 * This is used to store the power to a non filled storage.
	 * 
	 * @param power is the generate amount of FP
	 */
	private void addPowerToStorages(float power) {
		Storage currentlyFilling = getNextNotFullStorage();
		if (currentlyFilling == null) {
			return;
		} else {
			float remaining = currentlyFilling.addPower(power);
			if (remaining > 0) {
				addPowerToStorages(remaining);
			}
		}
	}
	
	/**
	 * This is used to remove the power from a storage.
	 * 
	 * @param power is the needed amount of FP
	 */
	private boolean removePowerFromStorages(float power) {
		Storage currentlyFilling = getNextFullStorage();
		if (currentlyFilling == null) {
			Main.debug("EnergyNetwork line 113, it doesn't supposed to happen.");
			return false;
		} else {
			float remaining = currentlyFilling.consumePower(power);
			if (remaining > 0) {
				return removePowerFromStorages(remaining);
			} else {
				return true;
			}
		}
	}

	/**
	 * This method will return the next not full {@link Storage}
	 * 
	 * @return the found {@link Storage}
	 */
	private Storage getNextNotFullStorage() {
		for (Unit u : storages) {
			Storage s = (Storage) u;
			if (s.getStored() != s.getMaxStorage())
				return s;
		}
		return null;
	}
	
	/**
	 * This method will return the next full {@link Storage} otherwise return a random one to prevent not consuming power even if storage has the required amount
	 * 
	 * @return the found {@link Storage}
	 */
	private Storage getNextFullStorage() {
		for (Unit u : storages) {
			Storage s = (Storage) u;
			if (s.getStored() == s.getMaxStorage())
				return s;
		}
		return (Storage) storages.get(new Random().nextInt(storages.size()));
	}

	/**
	 * @return the units of this network
	 */
	public List<Unit> getUnits() {
		return this.units;
	}

	/**
	 * This method will calculate the stored energy from the storages
	 */
	private void calculateFrostedPower() {
		float start = 0.0f;
		for (Unit s : storages) {
			start += ((Storage) s).getStored();
		}
		frostedpower = start;
	}

	/**
	 * This method will calculate the capacity from the storages
	 */
	private void calculateCapacity() {
		float start = 5.0f;
		for (Unit s : storages) {
			start += ((Storage) s).getMaxStorage();
		}
		capacity = start;
	}

	/**
	 * This will update the storages arraylist
	 */
	private void updateStorages() {
		storages.clear();
		for (Unit u : units) {
			if (u instanceof Storage) {
				storages.add(u);
			}
		}
		calculateCapacity();
		calculateFrostedPower();
	}

	/**
	 * @param needed is the needed fp
	 * @return true if this amount of power can be discharged
	 */
	public boolean hasEnoughFP(float needed) {
		return ((frostedpower - needed) >= 0) ? true : false;
	}

	/**
	 * @param needed is the power to be generated
	 * @return true if the network can handle the amount generated
	 */
	public boolean hasEnoughCapacity(float needed) {
		return ((frostedpower + needed) <= capacity) ? true : false;
	}

	/**
	 * @return the stored frosted power.
	 */
	public float getFP() {
		return frostedpower;
	}

}
