package net.iceyleagons.frostedengineering.vegetation.genes.types.genome;

import net.iceyleagons.frostedengineering.utils.math.Bounds;
import net.iceyleagons.frostedengineering.vegetation.genes.types.interfaces.IAngleGene;

public class AngleGene implements IAngleGene {
	
	private Bounds bounds;

	public AngleGene(Bounds bounds) {
		this.bounds = bounds;
	}

	@Override
	public double next() {
		return bounds.nextValue();
	}

	@Override
	public Bounds getBounds() {
		return bounds;
	}

}
