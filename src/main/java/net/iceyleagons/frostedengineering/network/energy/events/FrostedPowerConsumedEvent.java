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
package net.iceyleagons.frostedengineering.network.energy.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.iceyleagons.frostedengineering.network.energy.EnergyNetwork;
import net.iceyleagons.frostedengineering.network.energy.components.Consumer;

public class FrostedPowerConsumedEvent extends Event implements Cancellable {

    private boolean cancelled = false;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private EnergyNetwork network;
    private float amount;
    private Consumer source;


    /**
     * @param network is the parent {@link EnergyNetwork}
     * @param amount  is the amount of FrostedPower consumed
     * @param source  is the {@link Consumer}
     */
    public FrostedPowerConsumedEvent(EnergyNetwork network, float amount, Consumer source) {
        this.network = network;
        this.amount = amount;
        this.source = source;
    }

    /**
     * @return the parent {@link EnergyNetwork}
     */
    public EnergyNetwork getNetwork() {
        return network;
    }

    /**
     * @return the amount of FrostedPower consumed
     */
    public float getAmount() {
        return amount;
    }

    /**
     * @return the {@link Consumer}
     */
    public Consumer getSource() {
        return source;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

}
