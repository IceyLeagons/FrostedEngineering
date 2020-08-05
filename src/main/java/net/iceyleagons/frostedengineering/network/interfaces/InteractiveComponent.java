package net.iceyleagons.frostedengineering.network.interfaces;

import net.iceyleagons.frostedengineering.gui.InventoryFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface InteractiveComponent {

    List<ItemStack> getItemsInside();
    void updateInventory();
    void initInventory();
    InventoryFactory getInventoryFactory();
    void open(Player player);
    List<GUIElement> getGUIElements();

}
