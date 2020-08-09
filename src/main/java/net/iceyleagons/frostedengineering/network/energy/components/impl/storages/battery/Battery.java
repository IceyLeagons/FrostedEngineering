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

package net.iceyleagons.frostedengineering.network.energy.components.impl.storages.battery;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.gui.InventoryFactory;
import net.iceyleagons.frostedengineering.interfaces.ITick;
import net.iceyleagons.frostedengineering.items.FrostedItems;
import net.iceyleagons.frostedengineering.network.energy.UnitInformation;
import net.iceyleagons.frostedengineering.network.energy.components.ChargableComponent;
import net.iceyleagons.frostedengineering.network.energy.gui.PowerBar;
import net.iceyleagons.frostedengineering.network.interfaces.GUIElement;
import net.iceyleagons.frostedengineering.network.interfaces.InteractiveComponent;
import net.iceyleagons.frostedengineering.network.energy.EnergyNetwork;
import net.iceyleagons.frostedengineering.network.energy.EnergyUnit;
import net.iceyleagons.frostedengineering.network.energy.exceptions.UnsupportedUnitType;
import net.iceyleagons.frostedengineering.network.interfaces.guiimpl.CustomElement;
import net.iceyleagons.frostedengineering.utils.ItemFactory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.type.Comparator;
import org.bukkit.block.data.type.RedstoneWire;
import org.bukkit.block.data.type.Repeater;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@UnitInformation
        (name = "Battery",
        author = "TOTHTOMI",
        description = "Holds energy",
        usedFor = "Storage")
public class Battery extends EnergyUnit implements ChargableComponent, InteractiveComponent,
         ITick, Serializable {


    private float maxStores = 1.0f;
    private float stores = 0.0f;
    private boolean visited = false;

    private float zero = 0f;
    private float twentyfive;
    private float half;
    private float seventyfive;
    private float fifteen;

    private InventoryFactory inventoryFactory;

    public Battery(Location loc, EnergyNetwork network, float maxStores) throws UnsupportedUnitType {
        super(loc, network);
        calculatePercentages();
        this.maxStores = maxStores;
        Main.registerITick(this);
    }

    public Battery(Location loc, EnergyNetwork network, UUID uuid, float maxStores, List<ItemStack> itemsInside) throws UnsupportedUnitType {
        super(loc, network, uuid, itemsInside);
        calculatePercentages();
        Main.registerITick(this);
    }

    private void calculatePercentages() {
        twentyfive = maxStores * 0.25f;
        half = maxStores * 0.5f;
        seventyfive = maxStores * 0.75f;
        fifteen = maxStores * 0.15f;
    }

    @Override
    public List<ItemStack> getItemsInside() {
        return null;
    }

    @Override
    public void updateInventory() {
        if (inventoryFactory == null) initInventory();
/*
        DecimalFormat df = new DecimalFormat("###,###.##");


        String text = "§f" + df.format(stores) + "/" + df.format(maxStores) + " FP";

        for (int i = 10; i < 15; i++)
            inventoryFactory.setItem(new ItemFactory(inventoryFactory.getSourceInventory().getItem(i).getType()).hideAttributes().setDisplayName(text).build(), i);

        if (stores >= zero)
            inventoryFactory.setItem(new ItemFactory(Material.GREEN_STAINED_GLASS_PANE).hideAttributes().setDisplayName(text).build(), 10);
        if (stores >= twentyfive)
            inventoryFactory.setItem(new ItemFactory(Material.GREEN_STAINED_GLASS_PANE).hideAttributes().setDisplayName(text).build(), 11);
        if (stores >= half)
            inventoryFactory.setItem(new ItemFactory(Material.GREEN_STAINED_GLASS_PANE).hideAttributes().setDisplayName(text).build(), 12);
        if (stores >= seventyfive)
            inventoryFactory.setItem(new ItemFactory(Material.GREEN_STAINED_GLASS_PANE).hideAttributes().setDisplayName(text).build(), 13);
        if (stores == maxStores)
            inventoryFactory.setItem(new ItemFactory(Material.GREEN_STAINED_GLASS_PANE).hideAttributes().setDisplayName(text).build(), 14);

        if (stores <= zero)
            inventoryFactory.setItem(new ItemFactory(Material.WHITE_STAINED_GLASS_PANE).hideAttributes().setDisplayName(text).build(), 10);
        if (stores <= twentyfive)
            inventoryFactory.setItem(new ItemFactory(Material.WHITE_STAINED_GLASS_PANE).hideAttributes().setDisplayName(text).build(), 11);
        if (stores <= half)
            inventoryFactory.setItem(new ItemFactory(Material.WHITE_STAINED_GLASS_PANE).hideAttributes().setDisplayName(text).build(), 12);
        if (stores <= seventyfive)
            inventoryFactory.setItem(new ItemFactory(Material.WHITE_STAINED_GLASS_PANE).hideAttributes().setDisplayName(text).build(), 13);
        if (stores < maxStores)
            inventoryFactory.setItem(new ItemFactory(Material.WHITE_STAINED_GLASS_PANE).hideAttributes().setDisplayName(text).build(), 14);
*/
        EnergyUnit.render(this);
    }

    @Override
    public void initInventory() {
        inventoryFactory = new InventoryFactory("Storage", 27, FrostedItems.INVENTORY_FILLER, true);
        //for (int i = 10; i < 15; i++)
        //    inventoryFactory.setItem(new ItemFactory(Material.WHITE_STAINED_GLASS_PANE).hideAttributes().setDisplayName("§f" + stores + "/" + maxStores + " FP").build(), i);
        EnergyUnit.render(this);
        updateInventory();
    }

    @Override
    public InventoryFactory getInventoryFactory() {
        if (inventoryFactory == null) initInventory();
        return inventoryFactory;
    }

    @Override
    public void open(Player player) {
        if (inventoryFactory == null) initInventory();
        inventoryFactory.openInventory(player);
    }

    @Override
    public List<GUIElement> getGUIElements() {
        return Collections.singletonList(
                new PowerBar(10,5,this)
        );
    }

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

    @Override
    public float getMaxStorage() {
        return this.maxStores;
    }

    @Override
    public float getStored() {
        return this.stores;
    }

    @Override
    public float addEnergy(float fp) {
        if ((stores + fp) <= maxStores) {
            stores += fp;
            updateInventory();
            return 0f;
        } else {
            stores = maxStores;
            updateInventory();
            return maxStores - (stores + fp);
        }
    }

    @Override
    public float consumeEnergy(float fp) {
        if ((stores - fp) < 0) {
            stores -= maxStores;
            return fp - maxStores;
        }

        if ((stores - fp) >= 0) {
            stores -= fp;
            return 0f;
        }
        return 0f;
    }

    @Override
    public boolean isFull() {
        return stores == maxStores;
    }

    @Override
    public void onTick() {
        if (stores == maxStores) {
            for (Location loc : super.getLocationsAroundBlock()) {
                if (loc.getBlock().getType() == Material.REDSTONE_WIRE) {
                    RedstoneWire rw = (RedstoneWire) loc.getBlock().getBlockData();
                    rw.setPower(15);
                    loc.getBlock().setBlockData(rw, true);
                } else if (loc.getBlock().getType() == Material.REPEATER) {
                    Repeater r = (Repeater) loc.getBlock().getBlockData();
                    r.setPowered(true);
                    loc.getBlock().setBlockData(r, true);
                } else if (loc.getBlock().getType() == Material.COMPARATOR) {
                    Comparator c = (Comparator) loc.getBlock().getBlockData();
                    c.setPowered(true);
                    loc.getBlock().setBlockData(c, true);
                }
            }
        }
    }
}
