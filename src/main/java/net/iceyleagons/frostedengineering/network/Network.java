package net.iceyleagons.frostedengineering.network;

import lombok.NonNull;
import net.iceyleagons.frostedengineering.network.energy.exceptions.UnsupportedUnitType;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public interface Network extends Serializable {

    /**
     * This method is used to add Units to the network
     *
     * @param unit is the {@link Unit} to add
     */
    public void addUnit(@NonNull Unit unit) throws UnsupportedUnitType;

    /**
     * This method is used to remove Units from the network
     *
     * @param unit is the {@link Unit} to delete
     */
    public void removeUnit(@NonNull Unit unit) throws UnsupportedUnitType;

    /**
     * @return the {@link Unit}s of this network
     */
    public List<Unit> getUnits();

    /**
     * @return the UUID of the network
     */
    public UUID getUUID();

    public Network generateSameType();
}
