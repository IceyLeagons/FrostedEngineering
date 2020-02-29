package net.iceyleagons.frostedengineering.vegetation.genes.types;

import net.iceyleagons.frostedengineering.vegetation.Bounds;
import net.iceyleagons.frostedengineering.vegetation.Exponential;
import net.iceyleagons.frostedengineering.vegetation.genes.types.genome.AngleGene;
import net.iceyleagons.frostedengineering.vegetation.genes.types.genome.ClumpGene;
import net.iceyleagons.frostedengineering.vegetation.genes.types.genome.DecayGene;
import net.iceyleagons.frostedengineering.vegetation.genes.types.genome.Genome;
import net.iceyleagons.frostedengineering.vegetation.genes.types.genome.LeafGene;
import net.iceyleagons.frostedengineering.vegetation.genes.types.genome.RootGene;
import net.iceyleagons.frostedengineering.vegetation.genes.types.genome.SplitGene;
import net.iceyleagons.frostedengineering.vegetation.genes.types.genome.TrunkGene;

public class MagnoliaGenome extends Genome {

	public MagnoliaGenome() {
		super(new ClumpGene(0), new SplitGene(3, 4), new AngleGene(new Bounds(Math.PI / 4, Math.PI / 2)),
				new DecayGene(new Bounds(0.55, 0.66)),
				new TrunkGene(new Bounds(3, 4), new Bounds(0.5, 0.5), new Bounds(-0.5, 0.5)), new LeafGene(1, 3),
				new RootGene(0, 0, new Exponential(1, 2), new Bounds(0.4, 0.5), new Bounds(1, 2)));
	}

}
