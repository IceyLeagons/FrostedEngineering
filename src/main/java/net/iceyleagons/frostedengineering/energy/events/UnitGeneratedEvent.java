package net.iceyleagons.frostedengineering.energy.events;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.iceyleagons.frostedengineering.energy.network.Unit;
import net.iceyleagons.frostedengineering.energy.network.components.Consumer;

public class UnitGeneratedEvent extends Event {

	private static final HandlerList HANDLERS_LIST = new HandlerList();
	
	private Unit u;
	private Location loc;
	
	/**
	 * @param c is the {@link Consumer} which got created
	 */
	public UnitGeneratedEvent(Unit u) {
		this.u = u;
		this.loc = u.getLocation();
	}
	
	/**
	 * @return the generated {@link Consumer}
	 */
	public Unit getConsumer() {
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
