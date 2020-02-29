package net.iceyleagons.frostedengineering.gui.installer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.iceyleagons.frostedengineering.Main;

public class Install {
	
	List<Step> steps = new ArrayList<Step>();
	Iterator<Step> stepIterator;
	int i = 0;
	Player p;
	
	public Install(Player p) {
		this.p = p;
		
		
		steps.add(new Step(StepType.PLAYER_INPUT, "Language (2 letter country code)", new String[] {}, new AnvilGUIRunnable() {
			
			@Override
			public void run(String text) {
				Main.getConfig("config").getConfig().set("lang", text);
				Main.getConfig("config").saveConfig();
				nextStep();
			}
		}, p).build());
		
		steps.add(new Step(StepType.YES_OR_NO, "Auto update?", new String[] {"Would you like to enable auto update?"},()->{
			Main.getConfig("config").getConfig().set("auto-update", true);
			Main.getConfig("config").saveConfig();
			nextStep();
		},()->{
			Main.getConfig("config").getConfig().set("auto-update", false);
			Main.getConfig("config").saveConfig();
			nextStep();
		},p).build());
		steps.add(new Step(StepType.YES_OR_NO, "Cheating?", new String[] {"Would you like to enable cheating?"},()->{
			Main.getConfig("config").getConfig().set("allow-cheating", true);
			Main.getConfig("config").saveConfig();
			nextStep();
		},()->{
			Main.getConfig("config").getConfig().set("allow-cheating", false);
			Main.getConfig("config").saveConfig();
			nextStep();
		},p).build());
		
		stepIterator = steps.listIterator();
	}
	
	public void start() {
		p.sendTitle("§b§lPlease wait...", "§aThe installer will walk you through the steps", 20, 60, 20);
		Bukkit.getScheduler().runTaskLater(Main.MAIN, () -> {nextStep();}, 40L);
	}
	
	public void nextStep() {
		if (stepIterator.hasNext()) {
			if (p.getOpenInventory() != null) p.closeInventory();
			Bukkit.getScheduler().runTaskLater(Main.MAIN, () -> {
				stepIterator.next().show();
			}, 20L);
		}
	}
}
