package net.iceyleagons.frostedengineering.network;

import java.util.List;
import java.util.UUID;

public interface Network {

    /**
     * This method is used to add Units to the network
     *
     * @param unit is the {@link Unit} to add
     */
    public void addUnit(Unit unit);

    /**
     * This method is used to remove Units from the network
     *
     * @param unit is the {@link Unit} to delete
     */
    public void removeUnit(Unit unit);

    /**
     * @return the {@link Unit}s of this network
     */
    public List<Unit> getUnits();

    /**
     * @return the UUID of the network
     */
    public UUID getUUID();
}
