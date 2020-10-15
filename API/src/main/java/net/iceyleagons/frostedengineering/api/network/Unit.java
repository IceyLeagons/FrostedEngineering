/*
 *  Copyright (C) IceyLeagons(https://iceyleagons.net/)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package net.iceyleagons.frostedengineering.api.network;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface Unit extends Serializable {

    /**
     * @return the {@link Location} of this Unit
     */
    Location getLocation();

    /**
     * @return the {@link UUID} of the unit
     */
    UUID getUUID();

    /**
     * This will be used by our algorithms
     */
    void destroy();

    /**
     * This is the same as above, but here we have a player, so we can give items,
     * etc.
     *
     * @param p is the {@link Player} of the destruction
     */
    void destroy(Player p);


    /**
     * This is used to disable a Unit
     *
     * @param bool if true the unit won't work anymore
     */
    void setDestroyed(boolean bool);


    /**
     * @return true if the Unit is destroyed.
     */
    boolean isDestroyed();

    /**
     * @return the {@link Network} of this Unit
     */
    Network getNetwork();

    /**
     * This is used by our algorithms to set the Energy Network
     *
     * @param net is the {@link Network} to set to, make sure that the Network is appropriate for the Unit's type!
     */
    void setNetwork(Network net);

    /**
     * @return the list of {@link Unit}s around this Unit
     */
    List<Unit> getNeighbours();

    /**
     *
     * @return true if the unit has been visited by the {@link Tracer}
     */
    boolean isVisited();

    /**
     * sets the visited value for the unit to true
     */
    void visit();

    /**
     * sets the visited value for the unit to false
     */
    void unvisit();

}
