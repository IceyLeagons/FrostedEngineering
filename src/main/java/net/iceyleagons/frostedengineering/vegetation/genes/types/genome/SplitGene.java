package net.iceyleagons.frostedengineering.vegetation.genes.types.genome;

import fastnoise.MathUtils;
import net.iceyleagons.frostedengineering.vegetation.genes.types.interfaces.ISplitGene;

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
