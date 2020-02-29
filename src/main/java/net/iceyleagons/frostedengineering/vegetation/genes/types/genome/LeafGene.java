package net.iceyleagons.frostedengineering.vegetation.genes.types.genome;

import net.iceyleagons.frostedengineering.vegetation.genes.types.interfaces.ILeafGene;

public class LeafGene implements ILeafGene {

	private double maxBranchRadius;
	private double radiusMultiplier;

	public LeafGene(double maxBranchRadius, double radiusMultiplier) {
		this.maxBranchRadius = maxBranchRadius;
		this.radiusMultiplier = radiusMultiplier;
	}

	@Override
	public double getMaxBranchRadius() {
		return maxBranchRadius;
	}

	@Override
	public double getRadiusMultiplier() {
		return radiusMultiplier;
	}
}
