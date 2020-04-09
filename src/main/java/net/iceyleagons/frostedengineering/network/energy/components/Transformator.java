package net.iceyleagons.frostedengineering.network.energy.components;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.interfaces.ITick;
import net.iceyleagons.frostedengineering.network.energy.EnergyNetwork;
import net.iceyleagons.frostedengineering.network.energy.EnergyNetworkType;
import net.iceyleagons.frostedengineering.network.energy.EnergyUnit;

public abstract class Transformator implements ITick {
	
	

	protected UUID uuid;
	protected Location location;
	@SuppressWarnings("unused")
	private static BlockFace largerVoltageSide = BlockFace.NORTH;
	@SuppressWarnings("unused")
	private static BlockFace lowVoltageSide = BlockFace.SOUTH;
	protected EnergyNetworkType capable;
	private boolean destroy = false;

	public Transformator(Location loc, EnergyNetworkType capable) {
		Main.registerITick(this);
		location = loc;
		this.capable = capable;
		Main.debug("Creating transformator...");
		this.uuid = UUID.randomUUID();
	}

	public Transformator(Location loc, UUID uuid, EnergyNetworkType capable) {
		this.uuid = uuid;
		location = loc;
		this.capable = capable;
		Main.registerITick(this);
		Main.debug("Creating transformator...");
	}

	public void destroy() {
		destroy = true;
	}

	public void explode() {
		destroy();
		location.getBlock().setType(Material.AIR);
		location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1L, 1L);
	}
	
	@Override
	public void onTick() {
		if (destroy == false) {
			EnergyUnit larger = EnergyUnit
					.getEnergyUnitAtLocation(this.location.clone().add(0, 0, 1));
			EnergyUnit lower = EnergyUnit
					.getEnergyUnitAtLocation(this.location.clone().add(0, 0, -1));
			if (lower != null && larger != null) {
				EnergyNetwork largern = (EnergyNetwork) larger.getNetwork();
				EnergyNetwork lowern = (EnergyNetwork) lower.getNetwork();
				if (largern.getType().getID() > capable.getID()) explode();
				
				if (lowern.getCapacity() > lowern.getFP()) {
					System.out.println("transforming");
					float amount = largern.getFP() / 4;
					lowern.generateFP(amount);
					largern.consumeFP(amount);

				}
			}
		}
	}
}
