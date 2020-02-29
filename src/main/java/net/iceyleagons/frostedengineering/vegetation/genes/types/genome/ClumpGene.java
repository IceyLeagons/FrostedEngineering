package net.iceyleagons.frostedengineering.vegetation.genes.types.genome;

import net.iceyleagons.frostedengineering.vegetation.genes.types.interfaces.IClumpGene;

public class ClumpGene implements IClumpGene {

	private double clumpFactor;

	public ClumpGene(double clumpFactor) {
		this.clumpFactor = clumpFactor;
	}

	@Override
	public double next() {
		return clumpFactor;
	}
}
