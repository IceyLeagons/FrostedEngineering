/*
 *  Copyright (C) IceyLeagons(https://iceyleagons.net/)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package net.iceyleagons.frostedengineering.common;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.world.WorldLoadEvent;

import net.iceyleagons.frostedengineering.commands.cmds.SaplingCommand;
import net.iceyleagons.frostedengineering.commands.cmds.StickCommand;
import net.iceyleagons.frostedengineering.common.gui.CustomCraftingTable;
import net.iceyleagons.frostedengineering.network.energy.EnergyUnit;
/*import net.iceyleagons.frostedengineering.network.energyold.components.Cable;
import net.iceyleagons.frostedengineering.network.energyold.components.Consumer;
import net.iceyleagons.frostedengineering.network.energyold.components.Generator;
import net.iceyleagons.frostedengineering.network.energyold.components.Storage;
import net.iceyleagons.frostedengineering.quests.QuestNPC;*/
import net.md_5.bungee.api.ChatColor;

public class Listeners implements Listener {

    public Listeners(Main main) {
        Bukkit.getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void inventoryClose(InventoryCloseEvent e) {
        SaplingCommand.inventoryCloseEvent(e);
    }

    public static boolean energyLoaded = false;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (!energyLoaded) {
            energyLoaded = true;
            //EnergySaver.loadEnergyNetworks();
        }
        /*
         * try { e.getPlayer() .sendMessage(WebAPI .getResponseFromWeb(
         * "https://api.iceyleagons.net/?function=update&plugin=frostedengineering&data=0.0.5")
         * .getContent().toString()); } catch (IOException e1) { e1.printStackTrace(); }
         */

    }

    @EventHandler
    public void onLoaded(WorldLoadEvent e) {
        if (e.getWorld().getName().equals("island")) {
            //Bukkit.getScheduler().runTaskLater(Main.MAIN, QuestNPC::spawn, 100L);
        }
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
        // if (e.getBlock().getType() == Material.STONE) { //
        // new Consumer(e.getBlock().getLocation(), new EnergyNetwork(), 1f,
        // NetworkType.LOW_VOLTAGE);
        // }
        // if (e.getBlock().getType() == Material.OAK_PLANKS) {
        // new Cable(e.getBlock().getLocation(), new EnergyNetwork(),
        // NetworkType.LOW_VOLTAGE, false);
        // }

        // lacementHandler.place(e);

        // if (e.getBlock().getType() == Material.BONE_BLOCK) {
        // CreativeMode.open(e.getPlayer());// new Install(e.getPlayer()).start();
        // }

        SaplingCommand.blockPlace(e);
    }

    ///private boolean debounce = false;

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {

    }

    //int i = 0;

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(PlayerInteractEvent e) {

		/*
		if (debounce == false && e.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR) {
			debounce = true;
			Bukkit.getScheduler().runTaskLater(Main.MAIN, () -> {
				debounce = false;
			}, 5L);
			
			Player p = e.getPlayer();
			Location tloc = null;
			Location hloc = null;
			if (i == 0)
			tloc = e.getClickedBlock().getLocation();
			if (i == 1) {
				hloc = e.getClickedBlock().getLocation();
				Zombie holder = (Zombie) p.getWorld().spawnEntity(hloc, EntityType.ZOMBIE);
				Slime target = (Slime) p.getWorld().spawnEntity(tloc, EntityType.SLIME);
		
				holder.getEquipment().setItemInMainHand(new ItemStack(Material.LEAD));
				
				tloc.getBlock().setType(Material.ACACIA_FENCE);
				hloc.getBlock().setType(Material.ACACIA_FENCE);
				
				holder.teleportAsync(hloc.add(0,1.5,1));
				target.teleportAsync(tloc.add(0,0.3,-1));
				
				holder.setAI(false);
				target.setAI(false);
				
				holder.setInvulnerable(true);
				target.setInvulnerable(true);
				
				
				
				target.setSize(1);
		
				ItemStack mh = p.getInventory().getItemInMainHand();
		
				if (mh != null && mh.getType() == Material.LEAD) {
					e.setCancelled(true);
					Bukkit.getScheduler().runTaskLater(Main.MAIN, () -> {
						target.setLeashHolder(holder);
						i = 0;
					}, 1L);
				}
			}
			i++;
		}
		
		*/
        SaplingCommand.rightClick(e);
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getItem() != null)
                if (e.getItem().equals(StickCommand.stick)) {
                    EnergyUnit u = EnergyUnit.getEnergyUnitAtLocation(e.getClickedBlock().getLocation());
                    Player p = e.getPlayer();
                    if (u == null) {
                        p.sendMessage("§e§l[Energy Debug] §rThere is no unit at this location!");
                        return;
                    }
                    p.sendMessage("§e§l[Energy Debug] §rUnit found!");
                    p.sendMessage("§f - Neighbours: §b" + u.getEnergyNeighbours().size());
                    /*if (u instanceof Storage) {
                        p.sendMessage("§f - Unit type: §bStorage");
                    } else if (u instanceof Generator) {
                        p.sendMessage("§f - Unit type: §bGenerator");
                    } else if (u instanceof Consumer) {
                        p.sendMessage("§f - Unit type: §bConsumer");
                    } else if (u instanceof Cable) {
                        p.sendMessage("§f - Unit type: §bCable");
                    } else {
                        p.sendMessage("§f - Unit type: §bNot defined?? How does this even happen?");
                    }*/

                    p.sendMessage("§f - Energy network: §b" + u.getEnergyNetwork().toString().split("@")[1]);
                    //p.sendMessage("§f    - Capacity: §b" + u.getEnergyNetwork().getCapacity());
                    //p.sendMessage("§f    - Stored: §b" + u.getEnergyNetwork().getFP());
                    //p.sendMessage("§f    - Storages: §b" + u.getEnergyNetwork().getStorages().size());
                    p.sendMessage("§f    - Units: §b" + u.getEnergyNetwork().getUnits().size());
                    p.sendMessage("§f    - NetworkType: §b" + u.getEnergyNetwork().getType().toString());
                    e.setCancelled(true);
                }
            if (Main.CUSTOM_CRAFTING_TABLE.compare(e.getClickedBlock())) {
                CustomCraftingTable.openCraftingTable(e.getClickedBlock().getLocation(), e.getPlayer());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExplode(BlockExplodeEvent e) {
        e.blockList().forEach(b -> {
            EnergyUnit u = EnergyUnit.getEnergyUnitAtLocation(b.getLocation());
            if (u != null) {
                Main.debug("§4§l" + u);
            }
        });

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExplode(EntityExplodeEvent e) {
        e.blockList().forEach(b -> {
            EnergyUnit u = EnergyUnit.getEnergyUnitAtLocation(b.getLocation());
            if (u != null) {
                Main.debug("§4§l" + u);
            }
        });
    }
}
