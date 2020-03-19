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
package net.iceyleagons.frostedengineering.vegetation.genes;

import net.iceyleagons.frostedengineering.utils.math.Bounds;
import net.iceyleagons.frostedengineering.utils.math.Exponential;
import net.iceyleagons.frostedengineering.vegetation.genes.types.Genome;
import net.iceyleagons.frostedengineering.vegetation.genes.types.genome.AngleGene;
import net.iceyleagons.frostedengineering.vegetation.genes.types.genome.ClumpGene;
import net.iceyleagons.frostedengineering.vegetation.genes.types.genome.DecayGene;
import net.iceyleagons.frostedengineering.vegetation.genes.types.genome.LeafGene;
import net.iceyleagons.frostedengineering.vegetation.genes.types.genome.RootGene;
import net.iceyleagons.frostedengineering.vegetation.genes.types.genome.SplitGene;
import net.iceyleagons.frostedengineering.vegetation.genes.types.genome.TrunkGene;

public class HickoryGenome extends Genome {
	public HickoryGenome() {
		super(new ClumpGene(0.149), new SplitGene(3, 4), new AngleGene(new Bounds(0.39, 0.58)),
				new DecayGene(new Bounds(0.152, 0.533)),
				new TrunkGene(new Bounds(4.44, 5.55), new Bounds(0.66, 1.6), new Bounds(-0.11, 0.11)),
				new LeafGene(0.5, 2.7),
				new RootGene(2, 4, new Exponential(0.43, 1.3), new Bounds(1, 1), new Bounds(4, 5)));
	}
}
