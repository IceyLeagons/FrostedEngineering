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

import fastnoise.MathUtils;
import old.vegetationold.genes.types.interfaces.ISplitGene;

public class SplitGene implements ISplitGene {

    private int max;
    private int min;

    public SplitGene(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public int next() {
        return MathUtils.randomInt(min, max);
    }

}
