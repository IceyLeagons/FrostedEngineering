package net.iceyleagons.frostedengineering.vegetation.genes.types.interfaces;

public interface IGenome {

	IClumpGene getClump();

	ISplitGene getSplit();

	IAngleGene getAngle();

	IDecayGene getDecay();

	ITrunkGene getTrunk();

	ILeafGene getLeaf();

	IRootGene getRoot();

}
