package net.iceyleagons.frostedengineering.network.energy;

import net.iceyleagons.frostedengineering.network.Unit;
import net.iceyleagons.frostedengineering.network.energy.exceptions.UnsupportedUnitType;

public abstract class Upgrade {

    public abstract void handle(Unit unit) throws UnsupportedUnitType;

}
