package net.iceyleagons.frostedengineering.vegetation.parts;

import java.util.List;

public class FractalBlueprint {

	private Branch trunk;
	private List<Root> roots;
	private List<List<Branch>> branches;
	private List<List<LeafCluster>> leafClusters;

	public FractalBlueprint(Branch trunk, List<Root> roots, List<List<Branch>> branches,
			List<List<LeafCluster>> leafClusters) {
		this.trunk = trunk;
		this.roots = roots;
		this.branches = branches;
		this.leafClusters = leafClusters;
	}

	public Branch getTrunk() {
		return trunk;
	}

	public List<Root> getRoots() {
		return roots;
	}

	public List<List<Branch>> getBranches() {
		return branches;
	}

	public List<List<LeafCluster>> getLeafClusters() {
		return leafClusters;
	}

}