package net.iceyleagons.frostedengineering.network;

import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface Unit {

    /**
     * @return the {@link Location} of this Unit
     */
    public Location getLocation();

    /**
     * @return the {@link UUID} of the unit
     */
    public UUID getUUID();

    /**
     * This will be used by our algorithms
     */
    public void destroy();

    /**
     * This is the same as above, but here we have a player, so we can give items,
     * etc.
     *
     * @param p is the {@link Player} of the destruction
     */
    public void destroy(Player p);


    /**
     * This is used to disable a Unit
     *
     * @param bool if true the unit won't work anymore
     */
    public void setDestroyed(boolean bool);


    /**
     * @return true if the Unit is destroyed.
     */
    public boolean isDestroyed();

    /**
     * @return the {@link Network} of this Unit
     */
    public Network getNetwork();

    /**
     * This is used by our algorithms to set the Energy Network
     *
     * @param net is the {@link Network} to set to, make sure that the Network is appropriate for the Unit's type!
     */
    public void setNetwork(Network net);

    /**
     * @return the list of {@link Unit}s around this Unit
     */
    public List<Unit> getNeighbours();

}
