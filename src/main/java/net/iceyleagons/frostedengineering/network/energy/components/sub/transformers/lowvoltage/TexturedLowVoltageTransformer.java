package net.iceyleagons.frostedengineering.network.energy.components.sub.transformers.lowvoltage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.network.energy.EnergyNetworkType;
import net.iceyleagons.frostedengineering.network.energy.components.sub.ComponentManager;
import net.iceyleagons.frostedengineering.textures.base.TexturedBlock;

public class TexturedLowVoltageTransformer extends TexturedBlock {

	public static Map<Location, LowVoltageTransformer> transformators;
	
	static {
		transformators = new HashMap<Location, LowVoltageTransformer>();
	}
	
	public TexturedLowVoltageTransformer() {
		super(Main.MAIN, "low_transformer", "block/transformer", "§rLow voltage transformer");
		ComponentManager.registerComponent("fe:lowvoltagetransformer", this);
	}
	
	public LowVoltageTransformer generateNewInstanceAtLocation(Location loc, EnergyNetworkType capable) {
		LowVoltageTransformer lvt = new LowVoltageTransformer(loc, capable);
		transformators.put(loc, lvt);
		return lvt;
	}
	
	public LowVoltageTransformer generateNewInstanceAtLocation(Location loc, UUID uuid, EnergyNetworkType capable) {
		LowVoltageTransformer lvt = new LowVoltageTransformer(loc, uuid, capable);
		transformators.put(loc, lvt);
		return lvt;
	}
	
	@Override
	public void onBroken(BlockBreakEvent e) {
		if (transformators.containsKey(e.getBlock().getLocation())) {
			transformators.get(e.getBlock().getLocation()).destroy();
			transformators.remove(e.getBlock().getLocation());
		}
	}

	@Override
	public void onPlacement(Block block, Player player) {
		generateNewInstanceAtLocation(block.getLocation(), EnergyNetworkType.MEDIUM_VOLTAGE);
	}

}
