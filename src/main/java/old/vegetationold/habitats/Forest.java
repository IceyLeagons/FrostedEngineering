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
package old.vegetationold.habitats;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import com.google.common.collect.ImmutableSet;

import old.vegetationold.Passthrublocks;

public class Forest implements IHabitat {

    @Override
    public boolean canPlantAt(Location location) {
        Block above = location.clone().add(0, 1, 0).getBlock();
        return soilMaterials.contains(location.getBlock().getType()) && Passthrublocks.isPassable(above.getType())
                && !liquid.contains(above.getType());
    }

    @Override
    public boolean isSoil(Material material) {
        return soilMaterials.contains(material);
    }

    private Set<Material> soilMaterials = new ImmutableSet.Builder<Material>().add(Material.SNOW_BLOCK).build();

    private Set<Material> liquid = new ImmutableSet.Builder<Material>().add(Material.WATER).add(Material.ICE).build();
}
