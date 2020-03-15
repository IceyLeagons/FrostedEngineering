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
package net.iceyleagons.frostedengineering;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import net.iceyleagons.frostedengineering.commands.cmds.SaplingCommand;
import net.iceyleagons.frostedengineering.energy.helpers.BreakHandler;
import net.iceyleagons.frostedengineering.energy.helpers.PlacementHandler;
import net.iceyleagons.frostedengineering.energy.network.EnergyNetwork;
import net.iceyleagons.frostedengineering.energy.network.Unit;
import net.iceyleagons.frostedengineering.energy.network.components.Cable;
import net.iceyleagons.frostedengineering.energy.network.components.Consumer;
import net.iceyleagons.frostedengineering.energy.network.components.Generator;
import net.iceyleagons.frostedengineering.gui.CustomCraftingTable;
import net.iceyleagons.frostedengineering.items.FrostedItems;
import net.iceyleagons.frostedengineering.other.Changelog;
import net.iceyleagons.frostedengineering.other.WebAPI;
import net.md_5.bungee.api.ChatColor;

public class Listeners implements Listener {

	public Listeners(Main main) {
		Bukkit.getPluginManager().registerEvents(this, main);
	}

	@EventHandler
	public void inventoryClose(InventoryCloseEvent e) {
		SaplingCommand.inventoryCloseEvent(e);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Changelog.giveToPlayer(e.getPlayer());
		try {
			e.getPlayer()
					.sendMessage(WebAPI
							.getResponseFromWeb(
									"https://api.iceyleagons.net/?function=update&plugin=frostedengineering&data=0.0.5")
							.getContent().toString());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInteractSapling(PlayerInteractEvent e) {
		SaplingCommand.rightClick(e);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Unit u = Unit.getUnitAtLocation(e.getClickedBlock().getLocation());
			if (u != null) {
				u.getInventoryFactory().openInventory(e.getPlayer());
			}
		}
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		e.setMessage(ChatColor.translateAlternateColorCodes('&', e.getMessage()));
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		// if (e.getCursor().hasItemMeta() && e.getCurrentItem().hasItemMeta()) {
		// if (e.getCursor().getType().equals(e.getCurrentItem().getType())
		// && e.getCursor().getItemMeta().equals(e.getCurrentItem().getItemMeta())) {
		if (e.getCursor() != null)
			if (e.getCurrentItem().getType().equals(Material.DIAMOND_HOE)
					&& e.getCursor().getType().equals(Material.DIAMOND_HOE)) {
				// if (e.getCurrentItem().getItemMeta().hasCustomModelData() &&
				// e.getCursor().getItemMeta().hasCustomModelData()) {
				e.getCurrentItem().setAmount(e.getCursor().getAmount() + e.getCurrentItem().getAmount());
				e.getCursor().setAmount(0);
				// }
			}
		// }
		// }
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlace(BlockPlaceEvent e) {
		if (e.getBlock().getType() == Material.STONE) { //
			new Consumer(e.getBlock().getLocation(), new EnergyNetwork(), 4f);
		}
		if (e.getBlock().getType() == Material.DIRT) {
			new Generator(e.getBlock().getLocation(), new EnergyNetwork(), 2f);
		}
		if (e.getBlock().getType() == Material.OAK_PLANKS) {
			new Cable(e.getBlock().getLocation(), new EnergyNetwork(), 100f);
		}

		PlacementHandler.place(e);

		if (e.getItemInHand().getItemMeta().equals(FrostedItems.CRAFTING_TABLE.getItemMeta())) {
			e.getBlockPlaced().setType(Material.BEDROCK);
			new CustomCraftingTable(e.getBlock().getLocation());
		}

		if (e.getBlock().getType() == Material.BONE_BLOCK) {
			// CreativeMode.open(e.getPlayer());// new Install(e.getPlayer()).start();
		}

		SaplingCommand.blockPlace(e);
	}

	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			CustomCraftingTable c = CustomCraftingTable.getCustomCraftingTable(e.getClickedBlock().getLocation());
			if (c != null) {
				c.getInventoryFactory().openInventory(e.getPlayer());
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBreak(BlockBreakEvent e) {
		if (e.getBlock().getType() == Material.STONE) {
			Unit u = Unit.getUnitAtLocation(e.getBlock().getLocation());
			if (u != null)
				u.destroy();
		}
		if (e.getBlock().getType() == Material.DIRT) {
			Unit u = Unit.getUnitAtLocation(e.getBlock().getLocation());
			if (u != null)
				u.destroy();
		}
		if (e.getBlock().getType() == Material.OAK_PLANKS) {
			Unit u = Unit.getUnitAtLocation(e.getBlock().getLocation());
			if (u != null)
				u.destroy();
		}

		BreakHandler.breakk(e);

		CustomCraftingTable c = CustomCraftingTable.getCustomCraftingTable(e.getBlock().getLocation());
		if (c != null) {
			e.getBlock().getLocation().getWorld().dropItemNaturally(e.getBlock().getLocation(),
					FrostedItems.CRAFTING_TABLE.clone());
			CustomCraftingTable.list.remove(c);
			//Main.CTD.removeCraftingTable(e.getBlock().getLocation());

		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onExplode(BlockExplodeEvent e) {
		e.blockList().forEach(b -> {
			Unit u = Unit.getUnitAtLocation(b.getLocation());
			if (u != null) {
				Main.debug("§4§l" + u);
				u.destroy();
			}
			CustomCraftingTable c = CustomCraftingTable.getCustomCraftingTable(b.getLocation());
			if (c != null) {
				CustomCraftingTable.list.remove(c);
				//Main.CTD.removeCraftingTable(c.getLocation());
			}
		});

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onExplode(EntityExplodeEvent e) {
		e.blockList().forEach(b -> {
			Unit u = Unit.getUnitAtLocation(b.getLocation());
			if (u != null) {
				Main.debug("§4§l" + u);
				u.destroy();
			}
			CustomCraftingTable c = CustomCraftingTable.getCustomCraftingTable(b.getLocation());
			if (c != null) {
				CustomCraftingTable.list.remove(c);
				//Main.CTD.removeCraftingTable(c.getLocation());
			}
		});
	}
}
