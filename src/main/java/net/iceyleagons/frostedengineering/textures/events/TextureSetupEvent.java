package net.iceyleagons.frostedengineering.textures.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TextureSetupEvent extends Event {

	protected static HandlerList handlers = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

}
