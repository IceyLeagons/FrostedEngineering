package net.iceyleagons.frostedengineering.textures.events;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.iceyleagons.frostedengineering.textures.base.TexturedSound;

public class SoundEvent extends Event implements Cancellable {

    private boolean cancelled;
    private TexturedSound sound;
    private Location location;
    private EventType type;
    protected static HandlerList handlers = new HandlerList();

    public SoundEvent(Location location, TexturedSound sound, EventType type) {
        this.sound = sound;
        this.location = location;
        this.type = type;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
    
    public EventType getType() {
        return type;
    }

    public Location getLocation() {
        return location;
    }

    public TexturedSound getSound() {
        return sound;
    }
    
    public static enum EventType {
        PLAY, STOP;
    }

}