package net.iceyleagons.frostedengineering.network.interfaces.guiimpl;

import net.iceyleagons.frostedengineering.gui.InventoryFactory;
import net.iceyleagons.frostedengineering.network.interfaces.GUIElement;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class ItemInputElement implements GUIElement {

    @Override
    public void render(InventoryFactory inventoryFactory, int startingSlot) {
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent event) {

    }

    @Override
    public void onUnitDestroyed(BlockBreakEvent event) {

    }
}
