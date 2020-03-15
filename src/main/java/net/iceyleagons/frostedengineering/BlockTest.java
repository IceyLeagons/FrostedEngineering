package net.iceyleagons.frostedengineering;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.iceyleagons.frostedengineering.textures.base.TexturedBlock;

public class BlockTest extends TexturedBlock {

	public BlockTest(JavaPlugin plugin) {
		super(plugin, "chest", "block/chestecske", "Chest Test");
	}

	@Override
	public void onInteract(PlayerInteractEvent e) {
		e.getPlayer().sendMessage("Interaktoltál vélem.");
	}

}
