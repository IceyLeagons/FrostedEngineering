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
package net.iceyleagons.frostedengineering.commands.cmds;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.commands.CommandManager.Arg.ArgString;
import net.iceyleagons.frostedengineering.commands.CommandManager.Cmd;
import net.iceyleagons.frostedengineering.commands.CommandManager.CommandFinished;
import old.vegetationold.Genes;
import old.vegetationold.Genes.GeneType;

public class SaplingCommand {
    @Cmd(cmd = "sapling", args = "[sapling_type]", argTypes = {
            ArgString.class}, help = "Gives you a sapling.", longhelp = "Gives you a sapling of the specified tree.", permission = net.iceyleagons.frostedengineering.common.other.permission.Permissions.COMMAND_DEBUG)
    public static CommandFinished cmdTest(CommandSender sender, Object[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            player.getInventory().addItem(Genes.valueOf((String) args[0]).getItem());
            return CommandFinished.DONE;
        }
        return CommandFinished.NOCONSOLE;
    }

    public static void blockPlace(BlockPlaceEvent e) {
        Location loc = e.getBlock().getLocation();
        Genes gene = Genes.isSaplingItem(e.getPlayer().getInventory().getItemInMainHand());
        if (gene != null) {
            if (gene.geneType == GeneType.BUSH || gene.geneType == GeneType.TREE) {
                Random random = new Random(1234567L);

                BukkitTask task = new BukkitRunnable() {

                    @Override
                    public void run() {
                        if (loc.getBlock().getType() != Material.OAK_SAPLING)
                            this.cancel();
                        else {
                            loc.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc.getX() + .5D, loc.getY() + .5D,
                                    loc.getZ() + .5D, 20, 0.5D, 0.5D, 0.5D, 0, Material.OAK_SAPLING.createBlockData());

                            loc.getWorld().playSound(loc, Sound.BLOCK_GRASS_BREAK, 1.f, random.nextFloat() * .5f);
                        }

                    }
                }.runTaskTimer(Main.MAIN, gene.growTime - 200L, 20L);

                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.MAIN, new Runnable() {

                    @Override
                    public void run() {
                        task.cancel();
                        if (loc.getBlock().getType() == Material.OAK_SAPLING)
                            gene.growPlantPhased(loc.add(new Vector(0, -1, 0)), 2L, true);
                    }

                }, gene.growTime);
            } else {
                Main.debug("Plant#grow().");
                gene.growPlantPhased(loc, 0L, true);
            }
        }
    }

    public static void rightClick(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();

        if (block != null) {
            Main.debug("NOT NULL!");
            if (block.getType() == Material.OAK_SAPLING) {
                Main.debug("SAPLING!");
                if (Genes.saplings.containsKey(block.getLocation())) {
                    Main.debug("ADDED");
                    Genes.saplings.get(block.getLocation()).openInventory(e.getPlayer());
                }
            }

            if (block.getType() == Material.WHEAT) {
                Main.debug("WHEAT!");
                if (Genes.plants.containsKey(block.getLocation())) {
                    Main.debug("ADDED");
                    Genes.plants.get(block.getLocation()).openInventory(e.getPlayer());
                }
            }
        }
    }

    public static void inventoryCloseEvent(InventoryCloseEvent e) {
        Inventory inv = e.getInventory();
        Genes.saplings.forEach((location, treeBuilder) -> {
            if (treeBuilder.inventory == inv) {
                treeBuilder.openedFor.remove(e.getPlayer());
            }
        });

        Genes.plants.forEach((location, plant) -> {
            if (plant.inventory == inv) {
                plant.openedFor.remove(e.getPlayer());
            }
        });
    }

}
