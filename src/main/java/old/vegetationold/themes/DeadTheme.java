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
package old.vegetationold.themes;

import java.util.Collections;
import java.util.Set;

import org.bukkit.Material;

public class DeadTheme implements ITheme {

    @Override
    public Material getLeaf() {
        return Material.AIR;
    }

    @Override
    public Material getThickBranch() {
        return Material.STRIPPED_SPRUCE_WOOD;
    }

    @Override
    public Material getThinBranch() {
        return Material.OAK_FENCE;
    }

    @Override
    public Material getRoot() {
        return Material.STRIPPED_SPRUCE_WOOD;
    }

    @Override
    public Set<Material> getSelfMaterials() {
        return Collections.emptySet();
    }

}
