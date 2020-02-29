package net.iceyleagons.frostedengineering.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import dev.arantes.inventorymenulib.PaginatedGUIBuilder;
import dev.arantes.inventorymenulib.buttons.ClickAction;
import dev.arantes.inventorymenulib.buttons.ItemButton;
import dev.arantes.inventorymenulib.menus.PaginatedGUI;
import net.iceyleagons.frostedengineering.Main;

public class CreativeMode {

	public static void open(Player p) {
		PaginatedGUI gui = new PaginatedGUIBuilder("§8Menu | page: {page}",
				"xxxxxxxxx" + "x#######x" + "<#######>" + "x#######x" + "xxxxxxxxx")

						// Define the material of the border and the name.
						// You can also define actions if you want the border to be clickable
						.setBorder(new ItemButton(Material.GRAY_STAINED_GLASS_PANE, 1, "", ""))

						// Set an item in the 4 position of the hotbar (lastrow)
						.setHotbarButton((byte) 4,
								new ItemButton(Material.COMPASS, 1, "§cClose").addAction(ClickType.LEFT,
										(InventoryClickEvent e) -> e.getWhoClicked().closeInventory()))

						// Set the item for the next page button.
						.setNextPageItem(Material.ARROW, 1, "§6Next page")

						// Set the item for the previous page buttom.
						.setPreviousPageItem(Material.ARROW, 1, "§6Previous page")
						.setContent(new ItemButton(Material.DIAMOND, 1, "Item"),
								new ItemButton(FrostedItems.STORAGE),
								new ItemButton(Material.DIAMOND, 1, "Item"),
								new ItemButton(Material.DIAMOND, 1, "Item"),new ItemButton(Material.DIAMOND, 1, "Item"),new ItemButton(Material.DIAMOND, 1, "Item"),new ItemButton(Material.DIAMOND, 1, "Item"),
								new ItemButton(Material.DIAMOND, 1, "Item"),new ItemButton(Material.DIAMOND, 1, "Item"),new ItemButton(Material.DIAMOND, 1, "Item"),new ItemButton(Material.DIAMOND, 1, "Item"),
								new ItemButton(Material.DIAMOND, 1, "Item"),new ItemButton(Material.DIAMOND, 1, "Item"),new ItemButton(Material.DIAMOND, 1, "Item"),new ItemButton(Material.DIAMOND, 1, "Item"),
								new ItemButton(Material.DIAMOND, 1, "Item"),new ItemButton(Material.DIAMOND, 1, "Item"),new ItemButton(Material.DIAMOND, 1, "Item"),new ItemButton(Material.DIAMOND, 1, "Item"),
								new ItemButton(Material.DIAMOND, 1, "Item"),new ItemButton(Material.DIAMOND, 1, "Item"),new ItemButton(Material.DIAMOND, 1, "Item"),new ItemButton(Material.DIAMOND, 1, "Item"))
						.setContentDefaultAction(new ClickAction() {
							
							@Override
							public void run(InventoryClickEvent ev) {
								ev.setCancelled(false);
								Bukkit.getScheduler().runTaskLater(Main.MAIN, () -> {
									ev.getClickedInventory().setItem(ev.getSlot(), new ItemStack(Material.DIRT));
								}, 1L); 
							}
						})
						// Build and return the PaginatedGUI
						.build();

		gui.show(p);
	}

}
