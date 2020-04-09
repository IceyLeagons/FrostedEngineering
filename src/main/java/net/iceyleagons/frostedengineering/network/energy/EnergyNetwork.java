/*******************************************************************************
e * Copyright (C) IceyLeagons(https://iceyleagons.net/) 
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.network.Network;
import net.iceyleagons.frostedengineering.network.Unit;
import net.iceyleagons.frostedengineering.network.energy.components.Storage;

/**
 * The energy network and its unit all know each other(unit has a reference to
 * the network, and vice versa) The energy distribution and all the main logic
 * is handled by this class.
 * 
 * Tracing and consuming,generating logic is handled by the units themselves.
 * 
 * @author TOTHT
 *
 */
public class EnergyNetwork implements Network {

	
	private List<EnergyUnit> energyUnits;
	private List<Unit> units;
	private List<EnergyUnit> storages;
	private float frostedpower = 0.0f;
	private float capacity = 5.0f;
	private EnergyNetworkType type;
	private UUID uuid;

	public EnergyNetwork() {
		energyUnits = new ArrayList<EnergyUnit>();
		storages = new ArrayList<EnergyUnit>();
		units = new ArrayList<Unit>();
		Main.debug("Creating energy network...");
		type = EnergyNetworkType.LOW_VOLTAGE;
		uuid = UUID.randomUUID();
	}

	public EnergyNetwork(UUID uuid) {
		energyUnits = new ArrayList<EnergyUnit>();
		storages = new ArrayList<EnergyUnit>();
		Main.debug("Load energy network with UUID: " + uuid.toString());
		this.uuid = uuid;
	}

	@Override
	public void addUnit(Unit unit) {
		if (unit instanceof EnergyUnit) {
			EnergyUnit u = (EnergyUnit) unit;
			units.add(u);
			energyUnits.add(u);
			Main.debug("Adding unit " + u.toString() + " to network" + this.toString());
			updateStorages();
			calculateCapacity();
		} else throw new IllegalArgumentException("Only EnergyUnits can be supplied!");
	}

	@Override
	public void removeUnit(Unit unit) {
		if (unit instanceof EnergyUnit) {
			EnergyUnit u = (EnergyUnit) unit;
			energyUnits.remove(u);
			units.remove(u);
			Main.debug("Removing unit " + u.toString() + " from network" + this.toString());
			updateStorages();
			calculateCapacity();
		} else throw new IllegalArgumentException("Only EnergyUnits can be supplied!");
	}

	/**
	 * This method is to add power to the network.
	 * 
	 * @param fp is the power to be added to the network
	 */
	public void generateFP(float fp) {
		addPowerToStorages(fp);
		type = EnergyNetworkType.getClassification(frostedpower);
	}

	/**
	 * Sets the network's capacity to the desired amount
	 * 
	 * @param capacity is the capacity to set
	 */
	public void setCapacity(float capacity) {
		this.capacity = capacity;
	}

	/**
	 * Sets the network's type to the set type
	 * 
	 * @param type is the {@link EnergyNetworkType} to set
	 */
	public void setType(EnergyNetworkType type) {
		this.type = type;
	}

	/**
	 * Sets the network's stored FP to the desired amount
	 * 
	 * @param fp is the FP to set
	 */
	public void setStored(float fp) {
		this.frostedpower = fp;
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
			type = EnergyNetworkType.getClassification(frostedpower);
			return true;
		} else {
			return false;
		}
	}

	int j = 0;

	/**
	 * This is used to store the power to a non filled storage.
	 * 
	 * @param power is the generate amount of FP
	 */

	private void addPowerToStorages(float power) {
		if (storages.isEmpty()) {
			if ((frostedpower + power) <= capacity)
				frostedpower += power;
			else if ((frostedpower + power) > capacity) {
				frostedpower = capacity;
			}
		}
		Storage currentlyFilling = getNextNotFullStorage();
		if (currentlyFilling == null) {
		} else {
			float remaining = currentlyFilling.addPower(power);
			if (remaining > 0) {
				addPowerToStorages(remaining);
			}
		}
		calculateFrostedPower();
	}

	/**
	 * This is used to remove the power from a storage.
	 * 
	 * @param power is the needed amount of FP
	 */
	private boolean removePowerFromStorages(float power) {
		Storage currentlyFilling = getNextFullStorage();
		if (currentlyFilling == null) {
			Main.debug("EnergyNetwork line 113, this doesn't supposed to happen.");
			return false;
		} else {
			float remaining = currentlyFilling.consumePower(power);
			if (remaining > 0) {
				calculateFrostedPower();
				return removePowerFromStorages(remaining);
			} else {
				calculateFrostedPower();
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
		if (storages.isEmpty())
			return null;
		for (EnergyUnit u : storages) {
			Storage s = (Storage) u;
			if (s.getStored() != s.getMaxStorage())
				return s;
		}
		return null;
	}

	/**
	 * This method will return the next full {@link Storage} otherwise return a
	 * random one to prevent not consuming power even if storage has the required
	 * amount
	 * 
	 * @return the found {@link Storage}
	 */
	private Storage getNextFullStorage() {
		for (EnergyUnit u : storages) {
			Storage s = (Storage) u;
			if (s.getStored() == s.getMaxStorage())
				return s;
		}
		return (Storage) storages.get(new Random().nextInt(storages.size()));
	}

	/**
	 * @return the {@link EnergyUnit}s of this network
	 */
	public List<EnergyUnit> getEnergyUnits() {
		return this.energyUnits;
	}
	
	@Override
	public List<Unit> getUnits() {
		return this.units;
	}

	/**
	 * @return the {@link EnergyNetworkType} of this {@link EnergyNetwork}
	 */
	public EnergyNetworkType getType() {
		return this.type;
	}

	@Override
	public UUID getUUID() {
		return this.uuid;
	}

	/**
	 * This method will calculate the stored energy from the storages
	 */
	private void calculateFrostedPower() {
		calculateCapacity();
		float start = 0.0f;
		for (EnergyUnit s : storages) {
			if (((Storage) s).getStored() > 0)
				start += ((Storage) s).getStored();
		}
		frostedpower = start;
	}

	/**
	 * This method will calculate the capacity from the storages
	 */
	private void calculateCapacity() {
		float start = 5.0f;
		for (EnergyUnit s : storages) {
			start += ((Storage) s).getMaxStorage();
		}
		capacity = start;
	}

	/**
	 * This will update the storages arraylist
	 */
	private void updateStorages() {
		storages.clear();
		for (EnergyUnit u : energyUnits) {
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
		return ((this.frostedpower - needed) >= 0) ? true : false;
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

	/**
	 * @return the capacity of the network
	 */
	public float getCapacity() {
		return this.capacity;
	}

	public List<Storage> getStorages() {
		List<Storage> s = new ArrayList<Storage>();
		for (EnergyUnit u : storages) {
			if (u instanceof Storage)
				s.add(((Storage) u));
		}
		return s;
	}

}
