package net.iceyleagons.frostedengineering.vegetation.parts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.util.Vector;

import fastnoise.MathUtils;
import net.iceyleagons.frostedengineering.vegetation.genes.types.IGenome;
import net.iceyleagons.frostedengineering.vegetation.genes.types.interfaces.IAngleGene;
import net.iceyleagons.frostedengineering.vegetation.genes.types.interfaces.IClumpGene;
import net.iceyleagons.frostedengineering.vegetation.genes.types.interfaces.IDecayGene;
import net.iceyleagons.frostedengineering.vegetation.genes.types.interfaces.ILeafGene;
import net.iceyleagons.frostedengineering.vegetation.genes.types.interfaces.IRootGene;
import net.iceyleagons.frostedengineering.vegetation.genes.types.interfaces.ISplitGene;
import net.iceyleagons.frostedengineering.vegetation.genes.types.interfaces.ITrunkGene;

public class FractalGrowthPattern {

	private ISplitGene splitGene;
	private IClumpGene clumpGene;
	private IAngleGene angleGene;
	private IDecayGene decayGene;
	private ITrunkGene trunkGene;
	private ILeafGene leafGene;
	private IRootGene rootGene;
	@SuppressWarnings("unused")
	private int iterations = 0;

	public FractalGrowthPattern(IGenome genome) {
		this.splitGene = genome.getSplit();
		this.clumpGene = genome.getClump();
		this.angleGene = genome.getAngle();
		this.decayGene = genome.getDecay();
		this.trunkGene = genome.getTrunk();
		this.leafGene = genome.getLeaf();
		this.rootGene = genome.getRoot();
	}

	public FractalBlueprint generateBlueprint() {

		Branch trunk;
		List<Root> roots = new ArrayList<>();
		List<List<Branch>> branches = new ArrayList<>();
		List<List<LeafCluster>> leafClusters = new ArrayList<>();

		trunk = trunkGene.next();
		LeafCluster trunkLeafCluster = getCluster(trunk);
		List<LeafCluster> phaseLeafCluster = new ArrayList<>();
		if (trunkLeafCluster != null) {
			phaseLeafCluster.add(trunkLeafCluster);
		}
		leafClusters.add(phaseLeafCluster);
		List<Branch> prevLayer = Collections.singletonList(trunk);
		branches.add(null);
		for (int i = 0; i < 6; i++) {
			prevLayer = iterate(prevLayer);
			branches.add(prevLayer);
			List<LeafCluster> phaseLeafClusters = new ArrayList<>();
			for (Branch branch : prevLayer) {
				LeafCluster branchLeafCluster = getCluster(branch);
				if (branchLeafCluster != null) {
					phaseLeafClusters.add(branchLeafCluster);
				}
			}
			leafClusters.add(phaseLeafClusters);
		}
		for (int i = 0; i < rootGene.nextAmount(); i++) {
			roots.add(rootGene.next(trunk));
		}

		return new FractalBlueprint(trunk, roots, branches, leafClusters);

	}

	public List<Branch> iterate(List<Branch> branchLayer) {
		List<Branch> outerLayer = new ArrayList<>();
		for (Branch branch : branchLayer) {
			outerLayer.addAll(splitBranch(branch, splitGene.next()));
		}
		iterations++;
		return outerLayer;
	}

	private List<Branch> splitBranch(Branch branch, int numBranches) {
		List<Branch> childBranches = new ArrayList<>();
		Vector randomPerp = MathUtils.getRandomPerpVector(branch.getDirection());
		double totalRadians = Math.PI * 2 * (1 - clumpGene.next());
		for (int i = 0; i < numBranches; i++) {
			double angle = angleGene.next();
			Vector starting = MathUtils.getRotatedVector(randomPerp, branch.getDirection(),
					i * totalRadians / numBranches);
			Vector axis = MathUtils.getRotatedVector(starting, branch.getDirection(), totalRadians / numBranches);
			double decay = decayGene.next(angle, angleGene.getBounds());
			Branch child = branch.split(axis, angle, decay);
			if (child != null) {
				childBranches.add(child);
			}
		}
		return childBranches;
	}

	private LeafCluster getCluster(Branch branch) {
		if (branch.getRadius() <= leafGene.getMaxBranchRadius()) {
			return new LeafCluster(branch.getEnd(), branch.getRadius() * leafGene.getRadiusMultiplier());
		} else {
			return null;
		}
	}

}
