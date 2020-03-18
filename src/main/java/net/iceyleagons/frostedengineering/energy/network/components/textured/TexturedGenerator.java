package net.iceyleagons.frostedengineering.energy.network.components.textured;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.energy.network.EnergyNetwork;
import net.iceyleagons.frostedengineering.energy.network.Unit;
import net.iceyleagons.frostedengineering.energy.network.components.Generator;
import net.iceyleagons.frostedengineering.textures.base.TexturedBlock;

public class TexturedGenerator extends TexturedBlock {

	Map<Location, Generator> generators = new HashMap<Location, Generator>();

	public TexturedGenerator() {
		super(Main.MAIN, "coal_generator", "block/coal_generator", "§rCoal Generator");
	}

	public Generator generateNewInstanceAtLocation(Location loc, EnergyNetwork net, float generates) {
		Generator g = new Generator(loc, net, generates);
		generators.put(loc, g);
		return g;
	}

	@Override
	public void onBroken(BlockBreakEvent e) {
		Unit u = Unit.getUnitAtLocation(e.getBlock().getLocation());
		if (u != null) {
			u.destroy();
		}
		updateMap();
	}

	@Override
	public void onPlacement(Block block, Player player) {
		generateNewInstanceAtLocation(block.getLocation(), new EnergyNetwork(), 2f);
	}

	@Override
	public void onInteract(PlayerInteractEvent event) {
		updateMap();

		if (generators.get(event.getClickedBlock().getLocation()) != null) {
			generators.get(event.getClickedBlock().getLocation()).open(event.getPlayer());
		}
	}

	private void updateMap() {
		Iterator<Location> it = generators.keySet().iterator();
		try {
			while (it.hasNext()) {
				Location loc = it.next();
				if (loc != null)
					if (Unit.getUnitAtLocation(loc) == null)
						generators.remove(loc);
			}
		} catch (ConcurrentModificationException ignore) {
		}
	}

}
