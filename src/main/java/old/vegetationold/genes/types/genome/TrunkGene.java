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
package old.vegetationold.genes.types.genome;

import org.bukkit.util.Vector;

import fastnoise.MathUtils;
import net.iceyleagons.frostedengineering.utils.math.Bounds;
import old.vegetationold.genes.types.interfaces.ITrunkGene;
import old.vegetationold.parts.Branch;

public class TrunkGene implements ITrunkGene {

    private Bounds heightBounds;
    private Bounds radiusBounds;
    private Bounds offSetBounds;

    public TrunkGene(Bounds heightBounds, Bounds radiusBounds, Bounds offSetBounds) {
        this.heightBounds = heightBounds;
        this.radiusBounds = radiusBounds;
        this.offSetBounds = offSetBounds;
    }

    @Override
    public Branch next() {
        double height = heightBounds.nextValue();
        double radius = MathUtils.map(height, heightBounds, radiusBounds);
        Vector begin = new Vector(0, 0, 0);
        Vector end = new Vector(offSetBounds.nextValue(), height, offSetBounds.nextValue());
        return new Branch(begin, end, radius);
    }

}
