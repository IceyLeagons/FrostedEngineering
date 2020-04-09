package net.iceyleagons.frostedengineering.network.energy;

import java.util.List;

import org.bukkit.Location;

public class EnergyAPI {
	
	public static EnergyAPI INSTANCE = null;
	public EnergyAPI() {
		INSTANCE = this;
	}
	
	
	/**
	 * This will register the Unit in our lists so our listeners can work with them
	 * 
	 * @param u is the {@link EnergyUnit} to add to the list
	 */
	public static void addUnit(EnergyUnit u) {
		EnergyUnit.addUnit(u);
	}
	
	/**
	 * This will remove the Unit in our lists so our listeners can work with them
	 * 
	 * @param u is the {@link EnergyUnit} to remove from the list
	 */
	public static void removeUnit(EnergyUnit u) {
		EnergyUnit.removeUnit(u);
	}
	
	/**
	 * This is used to get all the Units near a location
	 * 
	 * @param loc is the {@link Location}
	 * @return the List of {@link EnergyUnit}s
	 */
	public static List<EnergyUnit> getNeighbours(Location loc) {
		return EnergyUnit.getNeighbours(loc);
	}
	
	/**
	 * @param loc the {@link Location}
	 * @return the {@link EnergyUnit} at that {@link Location} if it found one
	 */
	public static EnergyUnit getUnitAtLocation(Location loc) {
		return EnergyUnit.getEnergyUnitAtLocation(loc);
	}
	
}
