package net.iceyleagons.frostedengineering.textures.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.iceyleagons.frostedengineering.textures.base.TexturedBase;

public class TextureInitializationEvent extends Event {

    private TexturedBase texturedBase;
    protected static HandlerList handlers = new HandlerList();

    public TextureInitializationEvent(TexturedBase base) {
        this.texturedBase = base;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public TexturedBase getTexturedBase() {
        return texturedBase;
    }

}
