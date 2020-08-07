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

package net.iceyleagons.frostedengineering.textures.base;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.interfaces.ITick;
import net.iceyleagons.frostedengineering.textures.events.SoundEvent;
import net.iceyleagons.frostedengineering.textures.events.SoundEvent.EventType;

public class TexturedSound extends TexturedBase implements ITick {

    private short length = 0;
    private static HashMap<Location, SoundData> playing = new HashMap<>();
    private float volume, pitch;

    /**
     * @param plugin    the plugin to register this sound at
     * @param name      the name the sound will have
     * @param modelPath the path to the sound
     * @param length    is the sound's length in ticks
     * @param volume    is the desired volume of the sound
     * @param pitch     is the desired pitch of the sound
     */
    public TexturedSound(JavaPlugin plugin, String name, String modelPath, short length, float volume, float pitch) {
        super(plugin, name, modelPath, null);
        this.length = length;
        this.volume = volume;
        this.pitch = pitch;
        Main.registerITick(this);
    }

    public static SoundData getPlayingAtLocation(Location l) {
        return playing.get(l);
    }

    /**
     * This will start playing the sound at the location
     * <p>
     * Throws a SoundEvent upon calling
     *
     * @param loc  the location to play the sound
     * @param loop a boolean, if true the song will continue to play until stopped
     */
    public void play(Location loc, boolean loop) {
        SoundEvent event = new SoundEvent(loc, this, EventType.PLAY);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            SoundData sd = getPlayingAtLocation(loc);
            if (sd == null) {
                sd = new SoundData(this, loc, loop);
                sd.setCurrentTime((short) 0);
                sd.getLoc().getWorld().playSound(sd.getLoc(), super.getName(), volume, pitch);
                playing.put(loc, sd);
            } else if (sd.isLooped()) {
                sd.getLoc().getWorld().playSound(sd.getLoc(), super.getName(), volume, pitch);
                sd.setCurrentTime((short) 0);
            }
        }
    }

    /**
     * This will stop the sound at the specified location
     * <p>
     * Throws a SoundEvent upon calling
     *
     * @param loc the location to stop the sound at
     */
    public void stop(Location loc) {
        SoundEvent event = new SoundEvent(loc, this, EventType.STOP);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            SoundData sd = getPlayingAtLocation(loc);
            if (sd != null) {
                sd.setCurrentTime((short) 0);
                sd.setLooping(false);
                loc.getNearbyPlayers(32).forEach(player -> player.stopSound(super.getName()));
                playing.remove(loc);
            }
        }
    }

    /**
     * @return the length of the sound.
     */
    public short getLength() {
        return length;
    }

    /**
     * @deprecated this function always returns null.
     */
    @Deprecated
    @Override
    public ItemStack getItem() {
        return null;
    }

    /**
     * @deprecated this function always returns null.
     */
    @Override
    @Deprecated
    public Material getBaseMaterial() {
        return null;
    }

    /**
     * @deprecated this function does nothing.
     */
    @Override
    @Deprecated
    public void setId(int id) {
        // Nothing. Useless function.
    }

    /**
     * @deprecated this function always returns null.
     */
    @Override
    @Deprecated
    public int getId() {
        return 0;
    }

    /**
     * @deprecated this function always returns null.
     */
    @Override
    @Deprecated
    public Recipe getRecipe() {
        return null;
    }

    /**
     * This will run on every single game tick.
     *
     * @see {@link Main}
     */
    @Override
    public void onTick() {
        playing.forEach((loc, sd) -> {
            if (sd.getCurrentTime() >= sd.getParent().getLength())
                sd.getParent().play(loc, sd.isLooped());

            sd.setCurrentTime((short) (sd.getCurrentTime() + 1));
        });
    }
}
