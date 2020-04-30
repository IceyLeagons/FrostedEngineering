package net.iceyleagons.frostedengineering.network.energy.components.sub.transformers.lowvoltage;

import java.util.UUID;

import org.bukkit.Location;

import net.iceyleagons.frostedengineering.network.energy.EnergyNetworkType;
import net.iceyleagons.frostedengineering.network.energy.components.Transformator;

public class LowVoltageTransformer extends Transformator {

    public LowVoltageTransformer(Location loc, UUID uuid, EnergyNetworkType capable) {
        super(loc, uuid, capable);
    }

    public LowVoltageTransformer(Location loc, EnergyNetworkType capable) {
        super(loc, capable);
    }

}
