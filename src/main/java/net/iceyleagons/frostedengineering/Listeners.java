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

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
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
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

import net.iceyleagons.frostedengineering.commands.cmds.SaplingCommand;
import net.iceyleagons.frostedengineering.commands.cmds.StickCommand;
import net.iceyleagons.frostedengineering.energy.helpers.BreakHandler;
import net.iceyleagons.frostedengineering.energy.helpers.PlacementHandler;
import net.iceyleagons.frostedengineering.energy.network.EnergyNetwork;
import net.iceyleagons.frostedengineering.energy.network.NetworkType;
import net.iceyleagons.frostedengineering.energy.network.Unit;
import net.iceyleagons.frostedengineering.energy.network.components.Cable;
import net.iceyleagons.frostedengineering.energy.network.components.Consumer;
import net.iceyleagons.frostedengineering.energy.network.components.Generator;
import net.iceyleagons.frostedengineering.energy.network.components.Storage;
import net.iceyleagons.frostedengineering.gui.CustomCraftingTable;
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
		/*
		 * try { e.getPlayer() .sendMessage(WebAPI .getResponseFromWeb(
		 * "https://api.iceyleagons.net/?function=update&plugin=frostedengineering&data=0.0.5")
		 * .getContent().toString()); } catch (IOException e1) { e1.printStackTrace(); }
		 */

	}

	@EventHandler
	public void onResourcepackStatusEvent(PlayerResourcePackStatusEvent e) {
		if (e.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED) {
			e.getPlayer().kickPlayer(
					"Unfortunately you can't play without our resource-pack :(. If your Minecraft can't handle the download, try downloading it here: ");
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
		if (e.getCursor() != null && e.getCurrentItem() != null) {
			if (e.getCurrentItem().getType().equals(Material.DIAMOND_HOE)
					&& e.getCursor().getType().equals(Material.DIAMOND_HOE)) {
				// if (e.getCurrentItem().getItemMeta().hasCustomModelData() &&
				// e.getCursor().getItemMeta().hasCustomModelData()) {
				e.getCurrentItem().setAmount(e.getCursor().getAmount() + e.getCurrentItem().getAmount());
				e.getCursor().setAmount(0);
				// }
			}
		}
		// }
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlace(BlockPlaceEvent e) {
		if (e.getBlock().getType() == Material.STONE) { //
			new Consumer(e.getBlock().getLocation(), new EnergyNetwork(), 1f, NetworkType.LOW_VOLTAGE);
		}
		if (e.getBlock().getType() == Material.OAK_PLANKS) {
			new Cable(e.getBlock().getLocation(), new EnergyNetwork(), NetworkType.LOW_VOLTAGE, false);
		}

		PlacementHandler.place(e);

		if (e.getBlock().getType() == Material.BONE_BLOCK) {
			// CreativeMode.open(e.getPlayer());// new Install(e.getPlayer()).start();
		}

		SaplingCommand.blockPlace(e);
	}

	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		SaplingCommand.rightClick(e);
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getItem()!=null)
			if (e.getItem().equals(StickCommand.stick)) {
				Unit u = Unit.getUnitAtLocation(e.getClickedBlock().getLocation());
				Player p = e.getPlayer();
				if (u == null) {
					p.sendMessage("§e§l[Energy Debug] §rThere is no unit at this location!");
					return;
				}
				p.sendMessage("§e§l[Energy Debug] §rUnit found!");
				p.sendMessage("§f - Neighbours: §b" + u.getNeighbours().size());
				if (u instanceof Storage) {
					p.sendMessage("§f - Unit type: §bStorage");
				} else if (u instanceof Generator) {
					p.sendMessage("§f - Unit type: §bGenerator");
				} else if (u instanceof Consumer) {
					p.sendMessage("§f - Unit type: §bConsumer");
				} else if (u instanceof Cable) {
					p.sendMessage("§f - Unit type: §bCable");
				} else {
					p.sendMessage("§f - Unit type: §bNot defined?? How this even happened?");
				}
				
				p.sendMessage("§f - Energy network: §b" + u.getNetwork().toString().split("@")[1]);
				p.sendMessage("§f    - Capacity: §b"+u.getNetwork().getCapacity());
				p.sendMessage("§f    - Stored: §b"+u.getNetwork().getFP());
				p.sendMessage("§f    - Storages: §b"+u.getNetwork().getStorages().size());
				p.sendMessage("§f    - Units: §b"+u.getNetwork().getUnits().size());
				p.sendMessage("§f    - NetworkType: §b"+u.getNetwork().getType().toString());
			}
			if (Main.CUSTOM_CRAFTING_TABLE.compare(e.getClickedBlock())) {
				CustomCraftingTable.openCraftingTable(e.getClickedBlock().getLocation(), e.getPlayer());
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
		if (e.getBlock().getType() == Material.OAK_PLANKS) {
			Unit u = Unit.getUnitAtLocation(e.getBlock().getLocation());
			if (u != null)
				u.destroy();
		}

		BreakHandler.breakk(e);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onExplode(BlockExplodeEvent e) {
		e.blockList().forEach(b -> {
			Unit u = Unit.getUnitAtLocation(b.getLocation());
			if (u != null) {
				Main.debug("§4§l" + u);
				u.destroy();
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
		});
	}
}
