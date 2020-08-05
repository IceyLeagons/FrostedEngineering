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

import old.vegetationold.genes.types.interfaces.ILeafGene;

public class LeafGene implements ILeafGene {

    private double maxBranchRadius;
    private double radiusMultiplier;

    public LeafGene(double maxBranchRadius, double radiusMultiplier) {
        this.maxBranchRadius = maxBranchRadius;
        this.radiusMultiplier = radiusMultiplier;
    }

    @Override
    public double getMaxBranchRadius() {
        return maxBranchRadius;
    }

    @Override
    public double getRadiusMultiplier() {
        return radiusMultiplier;
    }
}
