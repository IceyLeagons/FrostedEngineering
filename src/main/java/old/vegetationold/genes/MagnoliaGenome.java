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
package old.vegetationold.genes;

import net.iceyleagons.frostedengineering.utils.math.Bounds;
import net.iceyleagons.frostedengineering.utils.math.Exponential;
import old.vegetationold.genes.types.Genome;
import old.vegetationold.genes.types.genome.AngleGene;
import old.vegetationold.genes.types.genome.ClumpGene;
import old.vegetationold.genes.types.genome.DecayGene;
import old.vegetationold.genes.types.genome.LeafGene;
import old.vegetationold.genes.types.genome.RootGene;
import old.vegetationold.genes.types.genome.SplitGene;
import old.vegetationold.genes.types.genome.TrunkGene;

public class MagnoliaGenome extends Genome {

    public MagnoliaGenome() {
        super(new ClumpGene(0), new SplitGene(3, 4), new AngleGene(new Bounds(Math.PI / 4, Math.PI / 2)),
                new DecayGene(new Bounds(0.55, 0.66)),
                new TrunkGene(new Bounds(3, 4), new Bounds(0.5, 0.5), new Bounds(-0.5, 0.5)), new LeafGene(1, 3),
                new RootGene(0, 0, new Exponential(1, 2), new Bounds(0.4, 0.5), new Bounds(1, 2)));
    }

}
