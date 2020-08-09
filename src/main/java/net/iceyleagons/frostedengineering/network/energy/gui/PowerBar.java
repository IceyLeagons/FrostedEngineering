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

package net.iceyleagons.frostedengineering.network.energy.gui;

import lombok.NonNull;
import net.iceyleagons.frostedengineering.gui.InventoryFactory;
import net.iceyleagons.frostedengineering.network.energy.components.ChargableComponent;
import net.iceyleagons.frostedengineering.network.interfaces.GUIElement;
import net.iceyleagons.frostedengineering.utils.ItemFactory;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.text.DecimalFormat;


/**
 * @author TOTHTOMI
 */
public class PowerBar implements GUIElement {

    private final ChargableComponent chargableComponent;
    private final int startingSlot;
    private final int numOfSlots;


    public PowerBar(@NonNull int startingSlot, @NonNull int numOfSlots, @NonNull ChargableComponent chargableComponent) {
        this.startingSlot = startingSlot;
        this.numOfSlots = numOfSlots;
        this.chargableComponent = chargableComponent;
    }

    @Override
    public void render(InventoryFactory inventoryFactory) {
        int powered = map(chargableComponent.getStored(),
                chargableComponent.getMaxStorage(), chargableComponent.getMaxStorage());

        DecimalFormat df = new DecimalFormat("###,###.##");


        String text = "§f" + df.format(chargableComponent.getStored()) + "/" + df.format(chargableComponent.getMaxStorage()) + " FP";

        for (int i = startingSlot; i < startingSlot+numOfSlots; i++) {
            if (i <= powered)
                inventoryFactory.setItem(
                        ItemFactory.newFactory(Material.RED_STAINED_GLASS_PANE)
                                .hideAttributes().setDisplayName(text).build(),
                        i);
            else
                inventoryFactory.setItem(
                        ItemFactory.newFactory(Material.WHITE_STAINED_GLASS_PANE)
                                .hideAttributes().setDisplayName(text).build(),
                        i);
        }
    }

    private int map(float input, float in_max, float out_max) {
        return Math.round((input * out_max / in_max));
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent event) {

    }

    @Override
    public void onUnitDestroyed(BlockBreakEvent event) {

    }
}

