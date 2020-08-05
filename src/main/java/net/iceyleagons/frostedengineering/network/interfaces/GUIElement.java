package net.iceyleagons.frostedengineering.network.interfaces;

import net.iceyleagons.frostedengineering.gui.InventoryFactory;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public interface GUIElement {

    void render(InventoryFactory inventoryFactory, int startingSlot);
    void onInventoryClose(InventoryCloseEvent event);
    void onUnitDestroyed(BlockBreakEvent event);

}
