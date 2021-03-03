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

package net.iceyleagons.frostedengineering.network.energy.components.impl.generators.coal;

import lombok.Getter;
import lombok.Setter;
import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.gui.InventoryFactory;
import net.iceyleagons.frostedengineering.interfaces.ISecond;
import net.iceyleagons.frostedengineering.interfaces.ITick;
import net.iceyleagons.frostedengineering.items.FrostedItems;
import net.iceyleagons.frostedengineering.network.energy.EnergyNetwork;
import net.iceyleagons.frostedengineering.network.energy.EnergyUnit;
import net.iceyleagons.frostedengineering.network.energy.exceptions.UnsupportedUnitType;
import net.iceyleagons.frostedengineering.network.interfaces.GUIElement;
import net.iceyleagons.frostedengineering.network.interfaces.InteractiveComponent;
import net.iceyleagons.frostedengineering.utils.ItemFactory;
import net.iceyleagons.frostedengineering.utils.ItemUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CoalGenerator extends EnergyUnit implements ITick, ISecond, InteractiveComponent {

    @Getter
    protected float generates = 1.0f;
    @Getter
    @Setter
    protected boolean enabled = false;
    protected int remainingBurn = 0;

    private InventoryFactory fac;

    public CoalGenerator(Location loc, EnergyNetwork network) throws UnsupportedUnitType {
        super(loc, network);
        initInventory();
        Main.registerITick(this);
    }

    public CoalGenerator(Location loc, EnergyNetwork network, UUID uuid, List<ItemStack> itemsInside) throws UnsupportedUnitType {
        super(loc, network, uuid, itemsInside);
        initInventory();
        Main.registerITick(this);
    }


    @Override
    public void onSecond() {
        //TODO Sound
    }

    @Override
    public void onTick() {
        updateInventory();
        enabled = remainingBurn != 0;


        if (super.getLocation().getBlock().isBlockPowered()) enabled = false;
        if (enabled && !super.isDestroyed()) {
            ((EnergyNetwork)getNetwork()).depositEnergy(generates);
            remainingBurn--;
        }
    }

    @Override
    public List<ItemStack> getItemsInside() {
        return Collections.singletonList(fac.getSourceInventory().getItem(14));
    }

    @Override
    public void updateInventory() {
        if (!super.isDestroyed()) {
            if (enabled)
                fac.setItem(new ItemFactory(Material.RED_STAINED_GLASS_PANE).hideAttributes()
                        .setDisplayName("§fGenerating: §b" + generates + " FP§7/§ftick").build(), 12);
            else
                fac.setItem(new ItemFactory(Material.GRAY_STAINED_GLASS_PANE).hideAttributes()
                        .setDisplayName("§fGenerating: §b0FP§7/§ftick").build(), 12, new InventoryFactory.ClickRunnable() {

                    @Override
                    public void run(InventoryClickEvent e) {
                        e.setCancelled(true);
                    }
                });

            int slot = 14;
            if (remainingBurn < 1) {
                ItemStack item = fac.getSourceInventory().getItem(slot);
                if (item != null) {
                    if (item.getType().isFuel()) {
                        remainingBurn = ItemUtils.getBurnTime(item) / 32;
                        if (item.getType() != Material.LAVA_BUCKET) {
                            if ((item.getAmount() - 1) > 0) {
                                fac.setItem(item.asQuantity(item.getAmount() - 1), slot);
                            } else {
                                fac.setItem(new ItemStack(Material.AIR), slot);
                            }
                        } else {
                            fac.setItem(new ItemStack(Material.BUCKET), slot);
                        }
                    }
                    fac.updateInventory();
                }
            }
        }
    }

    @Override
    public void initInventory() {
        fac = new InventoryFactory("Generator", 27, FrostedItems.INVENTORY_FILLER.asOne(), false);
        fac.setItem(new ItemFactory(Material.LIGHT_GRAY_STAINED_GLASS_PANE).hideAttributes()
                .setDisplayName("§fGenerating: §b0FP§7/§ftick").build(), 12);
        fac.setItem(new ItemStack(Material.AIR), 14);
        if (super.getLoadedItems() != null) {
            List<ItemStack> items = super.getLoadedItems();
            if (!items.isEmpty()) {
                for (int i = 0; i < items.size(); i++) {
                    if (i == 0) {
                        fac.setItem(items.get(i), 14);
                    }
                }
            }
        }
        updateInventory();
    }

    @Override
    public InventoryFactory getInventoryFactory() {
        return this.fac;
    }

    @Override
    public void open(Player player) {
        fac.openInventory(player);
    }

    @Override
    public List<GUIElement> getGUIElements() {
        return null;
    }

    private boolean visited = false;

    @Override
    public boolean isVisited() {
        return this.visited;
    }

    @Override
    public void visit() {
        this.visited = true;
    }

    @Override
    public void unvisit() {
        this.visited = false;
    }
}
