package net.iceyleagons.frostedengineering.api.network;

import net.iceyleagons.frostedengineering.api.IFrostedEngineering;
import net.iceyleagons.frostedengineering.api.other.registry.Registry;
import org.json.JSONObject;

import java.util.UUID;

/**
 * @author TOTHTOMI
 */
public interface Network {

    Registry<Unit> getUnits();
    UnitManager getUnitManager();
    IFrostedEngineering getFrostedEngineering();
    UUID getId();
    JSONObject getMetadata();

}
