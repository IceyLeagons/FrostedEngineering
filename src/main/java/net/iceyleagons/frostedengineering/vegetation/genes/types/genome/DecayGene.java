package net.iceyleagons.frostedengineering.vegetation.genes.types.genome;

import fastnoise.MathUtils;
import net.iceyleagons.frostedengineering.vegetation.Bounds;
import net.iceyleagons.frostedengineering.vegetation.genes.types.interfaces.IDecayGene;

public class DecayGene implements IDecayGene {

	private Bounds bounds;

	public DecayGene(Bounds bounds) {
		this.bounds = bounds;
	}

	@Override
	public double next() {
		return bounds.nextValue();
	}

	@Override
	public double next(double angle, Bounds angleBounds) {
		return MathUtils.map(Math.abs(angle), angleBounds, bounds);
	}

	@Override
	public double next(double previous) {
		return bounds.nextValue();
	}

}
