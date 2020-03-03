package net.iceyleagons.frostedengineering.storage.types;

import java.util.List;

import org.bukkit.Location;

public interface ICraftingTableDatabase {
	
	void init();
	void addCraftingTable(Location loc);
	void removeCraftingTable(Location loc);
	List<Location> getCraftingTables();
}
