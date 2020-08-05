/*******************************************************************************
 * Copyright (C) IceyLeagons(https://iceyleagons.net/) 
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iceyleagons.frostedengineering.network.energyold.events;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.iceyleagons.frostedengineering.network.energyold.EnergyUnit;
import net.iceyleagons.frostedengineering.network.energyold.components.Consumer;

public class UnitGeneratedEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private EnergyUnit u;
    private Location loc;

    /**
     * @param c is the {@link Consumer} which got created
     */
    public UnitGeneratedEvent(EnergyUnit u) {
        this.u = u;
        this.loc = u.getLocation();
    }

    /**
     * @return the generated {@link Consumer}
     */
    public EnergyUnit getConsumer() {
        return this.u;
    }

    /**
     * @return the location of the generated {@link Consumer}
     */
    public Location getLocation() {
        return this.loc;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

}
