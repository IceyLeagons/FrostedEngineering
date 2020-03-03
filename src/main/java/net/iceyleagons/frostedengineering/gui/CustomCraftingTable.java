package net.iceyleagons.frostedengineering.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.items.FrostedItems;
import net.iceyleagons.frostedengineering.utils.CustomCrafting;

public class CustomCraftingTable {

	private Listener listener;
	private Location loc;
	private InventoryFactory fac;

	public CustomCraftingTable(Location loc) {
		new CustomCraftingTable(loc,false);
	}
	
	public CustomCraftingTable(Location loc, boolean init) {
		this.loc = loc;
		list.add(this);
		if (init == false) Main.STORAGE_MANAGER.CRAFTING_TABLE.addCraftingTable(loc);
		this.fac = new InventoryFactory("Crafting Table", 54, FrostedItems.INVENTORY_FILLER, false);
		for (int i = 0; i <= 4; i++) {
			for (int j = 2; j <= 6; j++) {
				if (i > 0) {
					fac.setItem(new ItemStack(Material.AIR), i * 9 + j);
				} else {
					fac.setItem(new ItemStack(Material.AIR), j);
				}
			}
			if (i == 2) {
				fac.setItem(new ItemStack(Material.AIR), i * 9);
				fac.setItem(new ItemStack(Material.AIR), i * 9 + 8);
			}
			if (i == 4) {
				fac.setItem(new ItemStack(Material.AIR), (i + 1) * 9);
			}
		}
		Bukkit.getScheduler().runTaskTimer(Main.MAIN, () -> {
			if (fac.isOpen()) {
				CustomCrafting.list.forEach(cc -> {
					cc.check(fac);
				});
			}
		}, 0L, 2L);
		// for (int i = 2; i <= 5; i++) {
		//
		// }
	}

	public InventoryFactory getInventoryFactory() {
		return this.fac;
	}

	public Location getLocation() {
		return this.loc;
	}

	public Listener getListener() {
		return this.listener;
	}

	/*
	 * Static world
	 */

	public static List<CustomCraftingTable> list = new ArrayList<CustomCraftingTable>();

	public static CustomCraftingTable getCustomCraftingTable(Location loc) {
		for (CustomCraftingTable c : list) {
			if (c.getLocation().equals(loc)) {
				return c;
			}
		}
		return null;
	}
	
	public static void loadFromDatabase() {
		Main.STORAGE_MANAGER.CRAFTING_TABLE.getCraftingTables().forEach(l -> {
			new CustomCraftingTable(l, true);
		});
	}

}
