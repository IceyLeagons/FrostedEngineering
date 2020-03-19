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

public class OakGenome extends Genome {
	public OakGenome() {
		super(new ClumpGene(0.05), new SplitGene(3, 4), new AngleGene(new Bounds(0, Math.PI / 3)),
				new DecayGene(new Bounds(0.23, 0.34)),
				new TrunkGene(new Bounds(6, 9), new Bounds(1.5, 2), new Bounds(-1, 1)), new LeafGene(1, 2.2),
				new RootGene(4, 6, new Exponential(1, 1.2), new Bounds(1, 1), new Bounds(4, 5)));
	}
}
