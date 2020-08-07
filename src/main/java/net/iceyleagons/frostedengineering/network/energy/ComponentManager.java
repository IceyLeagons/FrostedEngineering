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

package net.iceyleagons.frostedengineering.network.energy;

import java.util.HashMap;
import java.util.Map;

/**
 * @author TOTHT
 * <p>
 * This class is used for saving/loading.
 */
public class ComponentManager {

    public static Map<String, Object> components;

    static {
        components = new HashMap<String, Object>();
    }

    /**
     * @param name           is the name for the component
     * @param texturedObject is the object of the textured component
     */
    public static void registerComponent(String name, Object texturedObject) {
        if (!components.containsKey(name)) components.put(name, texturedObject);
    }

    /**
     * @param name is the name of the component to unregister
     */
    public static void unregisterComponent(String name) {
        if (components.containsKey(name)) components.remove(name);
    }

    /**
     * @param name is the name of the component to search for
     * @return the textured component object of it.
     */
    public static Object getComponent(String name) {
        return components.get(name) != null ? components.get(name) : null;
    }

}
