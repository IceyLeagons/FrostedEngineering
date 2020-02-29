package net.iceyleagons.frostedengineering.vegetation.genes.types.genome;

import java.util.Random;

import net.iceyleagons.frostedengineering.vegetation.Bounds;
import net.iceyleagons.frostedengineering.vegetation.genes.types.interfaces.IAngleGene;

public class AngleGene implements IAngleGene {

	private static Random random = new Random();
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
