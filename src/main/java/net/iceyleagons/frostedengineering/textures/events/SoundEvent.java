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