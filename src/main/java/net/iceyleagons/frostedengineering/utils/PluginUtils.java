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
package net.iceyleagons.frostedengineering.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

public class PluginUtils {
    public static List<EntityType> spawnerEntityTypes;
    public static Random reusableRandom;

    static {
        PluginUtils.spawnerEntityTypes = new ArrayList<>();
    }

    public static void initialize() {
        PluginUtils.reusableRandom = new Random();
        PluginUtils.spawnerEntityTypes.add(EntityType.BLAZE);
        PluginUtils.spawnerEntityTypes.add(EntityType.GHAST);
        PluginUtils.spawnerEntityTypes.add(EntityType.PIG_ZOMBIE);
        PluginUtils.spawnerEntityTypes.add(EntityType.MAGMA_CUBE);
    }

    public static EntityType randomEntity() {
        return PluginUtils.spawnerEntityTypes
                .get(PluginUtils.reusableRandom.nextInt(PluginUtils.spawnerEntityTypes.size()));
    }

    public static String serializeLocation(Location loc) {
        return loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getWorld().getName();
    }

    public static Location deserializeLocation(String str) {
        String[] data = str.split(";");
        double x = Double.parseDouble(data[0]);
        double y = Double.parseDouble(data[1]);
        double z = Double.parseDouble(data[2]);
        World w = Bukkit.getServer().getWorld((data[3]));
        return new Location(w, x, y, z);
    }
}
