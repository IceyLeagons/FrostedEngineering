/*******************************************************************************
 * Copyright (C) 2019 Tóth Tamás and Márton Kissik (https://www.iceyleagons.net/)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package net.iceyleagons.frostedengineering.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.iceyleagons.frostedengineering.Main;

public class InventoryFactory {

    private Inventory inv;
    private HashMap<Inventory, InventoryFactory> inventories = new HashMap<Inventory, InventoryFactory>();
    private HashMap<Integer, ClickRunnable> runs = new HashMap<Integer, ClickRunnable>();
    private int currentOpen = 0;
    private boolean registered = false;
    private Listener listener;
    private List<Player> opened = new ArrayList<Player>();

    public InventoryFactory(String name, int size) {
        new InventoryFactory(name, size, null, true);
    }


    public InventoryFactory(String name, int size, ItemStack placeholder, boolean deny) {
        if (size == 0) {
            return;
        }
        debounce = false;
        listener = new Listener() {
            @EventHandler
            public void onClick(InventoryClickEvent e) {
                if (e.getWhoClicked() instanceof Player) {
                    if (e.getCurrentItem() == null) {
                        return;
                    }
                    if (inventories.containsKey(e.getInventory())) {
                        InventoryFactory current = inventories.get(e.getInventory());
                        if (deny)
                            e.setCancelled(true);
                        if (e.getCurrentItem().equals(placeholder)) {
                            e.setCancelled(true);
                        }
                        Player p = (Player) e.getWhoClicked();
                        if (current.runs.get(e.getSlot()) == null) {
                            if (deny)
                                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                            if (e.getCurrentItem().equals(placeholder)) {
                                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                            }
                        } else {
                            p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
                            if (current.runs.get(e.getSlot()) != null && debounce == false) {
                                debounce = true;
                                current.runs.get(e.getSlot()).run(e);
                                try {
                                    Thread.sleep(10);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                debounce = false;
                            }
                        }
                    }
                }
            }

            @EventHandler
            public void onClose(InventoryCloseEvent e) {
                if (e.getPlayer() instanceof Player) {
                    opened.remove(e.getPlayer());
                    if (inventories.containsKey(e.getInventory())) {
                        InventoryFactory current = inventories.get(e.getInventory());

                        current.currentOpen--;
                        if (current.currentOpen == 0) {
                            current.unRegister();
                        }
                    }
                }
            }
        };
        Bukkit.getServer().getPluginManager().registerEvents(getListener(), Main.MAIN);
        if (size == 5) {
            inv = Bukkit.createInventory(null, InventoryType.HOPPER, ChatColor.BLUE + name);
        } else if (size == 9) {
            inv = Bukkit.createInventory(null, InventoryType.DROPPER, ChatColor.BLUE + name);
        } else {
            inv = Bukkit.createInventory(null, size, ChatColor.BLUE + name);
        }
        if (placeholder != null) {
            for (int i = 0; i < size; i++) {
                inv.setItem(i, placeholder);
            }
        }
        register();
    }

    public boolean isOpen() {
        return currentOpen > 0 ? true : false;
    }

    public Inventory getSourceInventory() {
        return inv;
    }

    public List<Player> getOpened() {
        return opened;
    }

    public void updateInventory() {
    }

    public int getSize() {
        return inv.getSize();
    }

    public void setItem(ItemStack itemstack, Integer slot, ClickRunnable executeOnClick) {
        setItem(itemstack, null, slot, executeOnClick);
    }

    public void setItem(ItemStack itemstack, String displayname, Integer slot, ClickRunnable executeOnClick, String... description) {
        ItemStack is = itemstack;
        ItemMeta im = is.getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_DESTROYS,
                ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_PLACED_ON,
                ItemFlag.HIDE_POTION_EFFECTS,
                ItemFlag.HIDE_UNBREAKABLE);
        if (displayname != null) {
            im.setDisplayName(ChatColor.BLUE + displayname);
        }
        if (description != null) {
            List<String> lore = new ArrayList<String>();
            for (String s : description) {
                lore.add(ChatColor.GRAY + s);
            }
            im.setLore(lore);
        }
        is.setItemMeta(im);
        inv.setItem(slot, is);
        runs.put(slot, executeOnClick);
    }

    public void removeItem(int slot) {
        inv.setItem(slot, new ItemStack(Material.AIR));
    }

    public void setItem(ItemStack itemstack, Integer slot) {
        inv.setItem(slot, itemstack);
    }

    public static boolean debounce = false;

    public Listener getListener() {
        return listener;
    }

    public void openInventory(Player player) {
        currentOpen++;
        register();
        opened.add(player);
        player.openInventory(getSourceInventory());
    }

    private void register() {
        if (!registered) {
            inventories.put(inv, this);
            registered = true;
        }
    }

    private void unRegister() {
        if (registered) {
            inventories.remove(inv);
            registered = false;
        }
    }

    public static abstract class ClickRunnable {
        public abstract void run(InventoryClickEvent e);
    }

    public static abstract class CloseRunnable {
        public abstract void run(InventoryCloseEvent e);
    }
}