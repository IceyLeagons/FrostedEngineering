package net.iceyleagons.frostedengineering.api.network;

import net.iceyleagons.frostedengineering.api.IFrostedEngineering;
import org.bukkit.Location;
import org.json.JSONObject;

import java.util.UUID;

/**
 * @author TOTHTOMI
 */
public interface Unit {

    UUID getId();
    Location getLocation();
    UnitManager getUnitManager();
    IFrostedEngineering getFrostedEngineering();
    Unit[] getNeighbors();
    JSONObject getMetadata();


}
