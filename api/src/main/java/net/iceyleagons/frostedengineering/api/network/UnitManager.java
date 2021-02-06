package net.iceyleagons.frostedengineering.api.network;

import net.iceyleagons.frostedengineering.api.other.EngineeringLocation;

import java.util.Map;

/**
 * @author TOTHTOMI
 */
public interface UnitManager {

    void registerUnit(Unit unit);
    void unregisterUnit(Unit unit);
    Map<EngineeringLocation, Unit> getUnits();
    Unit getUnitAtLocation(EngineeringLocation location);


}
