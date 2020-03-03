package net.iceyleagons.frostedengineering.vegetation.genes.types;

import java.util.HashMap;

import net.iceyleagons.frostedengineering.vegetation.Genes;
import net.iceyleagons.frostedengineering.vegetation.genes.types.interfaces.IAngleGene;
import net.iceyleagons.frostedengineering.vegetation.genes.types.interfaces.IClumpGene;
import net.iceyleagons.frostedengineering.vegetation.genes.types.interfaces.IDecayGene;
import net.iceyleagons.frostedengineering.vegetation.genes.types.interfaces.ILeafGene;
import net.iceyleagons.frostedengineering.vegetation.genes.types.interfaces.IRootGene;
import net.iceyleagons.frostedengineering.vegetation.genes.types.interfaces.ISplitGene;
import net.iceyleagons.frostedengineering.vegetation.genes.types.interfaces.ITrunkGene;

public interface IGenome {

	IClumpGene getClump();

	ISplitGene getSplit();

	IAngleGene getAngle();

	IDecayGene getDecay();

	ITrunkGene getTrunk();

	ILeafGene getLeaf();

	IRootGene getRoot();

	HashMap<Double, Genes> getGenes();

}
