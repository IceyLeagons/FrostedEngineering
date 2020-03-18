package net.iceyleagons.frostedengineering.energy.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.iceyleagons.frostedengineering.energy.network.EnergyNetwork;
import net.iceyleagons.frostedengineering.energy.network.components.Consumer;

public class FrostedPowerConsumedEvent extends Event implements Cancellable{

	private boolean cancelled = false;
	private static final HandlerList HANDLERS_LIST = new HandlerList();
	
	private EnergyNetwork network;
	private float amount;
	private Consumer source;
	
	
	
	/**
	 * @param network is the parent {@link EnergyNetwork}
	 * @param amount is the amount of FrostedPower consumed
	 * @param source is the {@link Consumer}
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
