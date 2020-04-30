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
package net.iceyleagons.frostedengineering.vegetation.parts;

import java.time.LocalTime;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import fastnoise.MathUtils;
import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.gui.InventoryFactory;
import net.iceyleagons.frostedengineering.gui.InventoryFactory.ClickRunnable;
import net.iceyleagons.frostedengineering.vegetation.Genes;

public class Plant {
    public Location location;
    private Block block;
    private int stage;
    private Genes gene;
    private long currentlyAt;
    private boolean sound = false;

    public Inventory inventory;

    public ArrayList<Player> openedFor = new ArrayList<>();

    public Plant(Location location, int stage, Genes gene, boolean sound) {
        this.location = location;
        this.block = location.getBlock();
        this.stage = stage;
        this.gene = gene;
        this.currentlyAt = gene.growTime;
        this.inventory = Bukkit.createInventory(null, 27);
        this.sound = sound;

        Genes.plants.put(location, this);

        new BukkitRunnable() {

            @Override
            public void run() {
                if (location.getBlock().getType() == Material.AIR)
                    this.cancel();

                InventoryFactory inv = new InventoryFactory("§a" + gene.name(), 27,
                        new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE), true);

                LocalTime a = LocalTime.ofSecondOfDay(Math.round(currentlyAt / 20));

                inv.setItem(new ItemStack(Material.CLOCK), "§6§lTime left", 10, new ClickRunnable() {

                    @Override
                    public void run(InventoryClickEvent e) {
                    }

                }, String.format("§c%d hour(s), %d minutes and %d seconds", a.getHour(), a.getMinute(), a.getSecond()));

                LocalTime a2 = LocalTime.ofSecondOfDay(Math.round(gene.growTime / 20));

                inv.setItem(new ItemStack(Material.PAPER), "§lPlant details", 13, new ClickRunnable() {

                    @Override
                    public void run(InventoryClickEvent e) {
                    }

                }, "§7Plant name: §6§l" + gene.name(), "§7Growth duration: §6§l" + String
                        .format("%d hour(s), %d minutes and %d seconds", a2.getHour(), a2.getMinute(), a2.getSecond()));

                inv.setItem(new ItemStack(Material.BARRIER), "§4§lRemove plant", 16, new ClickRunnable() {

                    @Override
                    public void run(InventoryClickEvent e) {
                        block.setType(Material.AIR);
                        e.getWhoClicked().closeInventory();
                    }

                }, "");

                inventory = inv.getSourceInventory();

                openedFor.forEach((player) -> {
                    player.openInventory(inventory);
                });
            }

        }.runTaskTimer(Main.MAIN, 0L, 20L);
    }

    public void openInventory(Player player) {
        this.openedFor.add(player);
    }

    public void grow() {
        new BukkitRunnable() {

            @Override
            public void run() {
                currentlyAt -= 20L;
                if (currentlyAt <= 0L) {
                    currentlyAt = 0L;
                    this.cancel();
                }
            }

        }.runTaskTimerAsynchronously(Main.MAIN, 0L, 20L);
        new BukkitRunnable() {

            @Override
            public void run() {
                if (location.getBlock().getType() != Material.AIR) {
                    block = location.getBlock();

                    if (stage < gene.growthStages) {
                        BlockData data = block.getBlockData();
                        Ageable crop = (Ageable) data;

                        crop.setAge(stage);
                        block.setBlockData(crop);

                        if (sound)
                            location.getWorld().playSound(location, Sound.BLOCK_CROP_BREAK, 1.f, 0.5f);

                        stage++;

                        if (stage > gene.growthStages) {
                            Genes.plants.remove(location);
                            this.cancel();
                        }
                    } else {
                        Genes.plants.remove(location);
                        this.cancel();
                    }
                }
            }

        }.runTaskTimer(Main.MAIN, 0L, MathUtils.fastRound(gene.growTime / gene.growthStages));
    }
}
