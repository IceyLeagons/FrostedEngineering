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

package net.iceyleagons.frostedengineering.hologram;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author Janhektor
 * @version 1.4 (December 30, 2015)
 */
public interface HoloAPI {

    /**
     * Shows this hologram to the given player
     * @param p The player who will see this hologram at the location specified by calling the constructor
     * @return true if the action was successful, else false
     */
    boolean display(Player player);

    /**
     * Removes this hologram from the players view
     * @param p The target player
     * @return true if the action was successful, else false (including the try to remove a non-existing hologram)
     */
    boolean destroy(Player player);

    /**
     * Create a new hologram
     * Note: The internal cache will be automatically initialized, it may take some millis
     * @param loc The location where this hologram is shown
     * @param lines The text-lines, from top to bottom, farbcodes are possible
     */
    public static HoloAPI newInstance(Location location, String... lines) {
        return newInstance(location, Arrays.asList(lines));
    }

    /**
     * Create a new hologram
     * Note: The internal cache will be automatically initialized, it may take some millis
     * @param loc The location where this hologram is shown
     * @param lines The text-lines, from top to bottom, farbcodes are possible
     */
    public static HoloAPI newInstance(Location location, List<String> lines) {
        return new DefaultHoloAPI(location, lines);
    }
}