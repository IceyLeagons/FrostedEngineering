package net.iceyleagons.frostedengineering.gui.installer;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.gui.InventoryFactory;
import net.iceyleagons.frostedengineering.gui.InventoryFactory.ClickRunnable;
import net.wesjd.anvilgui.AnvilGUI;
import net.wesjd.anvilgui.AnvilGUI.Builder;

public class Step {
	
	private StepType setType;
	private String title;
	private String[] description;
	private InventoryFactory inv;
	
	private Runnable yesRunnable, noRunnable;
	private AnvilGUIRunnable playerInputRunnable;
	private Builder builder;
	
	private Player p;
	
	public Step(StepType setType, String title, String[] description, Runnable yesRunnable, Runnable noRunnable, Player p) {
		this.setType = setType;
		this.title = title;
		this.description = description;
		this.yesRunnable = yesRunnable;
		this.noRunnable = noRunnable;
		this.p = p;
		builder = null;
		inv = null;
	}
	
	public Step(StepType setType, String title, String[] description, AnvilGUIRunnable runnable, Player p) {
		this.setType = setType;
		this.title = title;
		this.description = description;
		this.p = p;
		this.playerInputRunnable = runnable;
		builder = null;
		inv = null;
	}
	
	public Step build() {
		
		switch(setType) {
		case PLAYER_INPUT:
			buildPlayerInput();
			break;
		case YES_OR_NO:
			buildYesOrNo();
			break;
		default:
			break;
		}
		
		return this;
	}
	
	public void show() {
		switch(setType) {
		case PLAYER_INPUT:
			builder.open(p);
			break;
		case YES_OR_NO:
			inv.openInventory(p);
			break;
		default:
			break;
		
		}
	}
	
	private void buildPlayerInput() {
		builder= new AnvilGUI.Builder();
		builder.plugin(Main.MAIN);
		builder.title("Enter your text!");
		builder.item(new ItemStack(Material.PAPER));
		builder.text(title);
		builder.onComplete((plr,text) -> {
			p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
			playerInputRunnable.run(text);
			return AnvilGUI.Response.close();
		});
	}
	
	private void buildYesOrNo() {
		
		InventoryFactory inv = new InventoryFactory("Yes or No?", 27, new ItemStack(Material.AIR),true);
		
		inv.setItem(new ItemStack(Material.MUSIC_DISC_CAT),"§a§lYes", 10, new ClickRunnable() {

					@Override
					public void run(InventoryClickEvent e) {
						getYesRunnable().run();
						e.getWhoClicked().closeInventory();
					}
			
		}, "Left click to select option.");

		inv.setItem(new ItemStack(Material.PAPER), title, 13, new ClickRunnable() {

					@Override
					public void run(InventoryClickEvent e) {
					}
			
		}, description);
		
		inv.setItem(new ItemStack(Material.MUSIC_DISC_CHIRP), "§4§lNo", 16, new ClickRunnable() {

					@Override
					public void run(InventoryClickEvent e) {
						getNoRunnable().run();
						e.getWhoClicked().closeInventory();
					}
			
		}, "Left click to select option.");
		this.inv = inv;

		
	}
	
	private Runnable getYesRunnable() {
		return yesRunnable;
	}
	
	private Runnable getNoRunnable() {
		return noRunnable;
	}
	
	public StepType getSetType() {
		return setType;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String[] getDescription() {
		return description;
	}
}

