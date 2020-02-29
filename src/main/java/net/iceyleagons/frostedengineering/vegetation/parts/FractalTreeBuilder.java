package net.iceyleagons.frostedengineering.vegetation.parts;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.vegetation.FunctionIterator;
import net.iceyleagons.frostedengineering.vegetation.Passthrublocks;
import net.iceyleagons.frostedengineering.vegetation.SegmentIterator;

public class FractalTreeBuilder extends Tree {

	private Set<Material> selfMaterial;

	private Branch trunk;
	private List<Root> roots;
	private List<List<Branch>> branches;
	private List<List<LeafCluster>> leafClusters;

	private long delay = 0L;
	private long delay2 = 1L;

	private Material rootMaterial;
	private Material leafMaterial;

	public FractalTreeBuilder(Player planter, Location seed, FractalBlueprint blueprint) {
		super(planter, seed);

		this.selfMaterial = theme.getSelfMaterials();
		this.rootMaterial = theme.getRoot();
		this.leafMaterial = theme.getLeaf();

		this.trunk = blueprint.getTrunk();
		this.roots = blueprint.getRoots();
		this.branches = blueprint.getBranches();
		this.leafClusters = blueprint.getLeafClusters();
	}

	@Override
	public void growPhased(int phaseTicks) {
		// Nothing lmao
	}

	private List<Branch> getBranches(int n) {
		return this.branches.get(n);
	}

	private List<LeafCluster> getLeafCluster(int n) {
		return this.leafClusters.get(n);
	}

	@Override
	public void growInstant() {
		int phase = 0;
		boolean finished = false;
		while (!finished) {
			finished = true;

			if (phase == 0) {
				finished = false;
				buildTrunk(trunk);
			}
			if (phase == 1) {
				finished = false;
				for (Root root : roots) {
					buildRoot(root);
				}
			}

			if (branches.size() > phase) {
				finished = false;
				List<Branch> branches = getBranches(phase);
				if (branches != null) {
					for (Branch branch : branches) {
						buildBranch(branch);
					}
				}
			}

			if (leafClusters.size() > phase) {
				finished = false;
				List<LeafCluster> leafClusters = getLeafCluster(phase);
				if (leafClusters != null) {
					for (LeafCluster leafCluster : leafClusters) {
						buildLeafCluster(leafCluster);
					}
				}
			}

			phase++;
		}

	}

	private Set<Location> buildTrunk(Branch branch) {
		Set<Location> branchLocations = new HashSet<>();
		Material materialFactory = branch.getThickness() == Branch.Thickness.THICK ? theme.getThickBranch()
				: theme.getThinBranch();
		for (Block block : new SegmentIterator(world, branch.getBegin().add(origin), branch.getEnd().add(origin),
				branch.getRadius())) {
			Material material = block.getType();
			if (Passthrublocks.isPassable(material) || Passthrublocks.trunkWhitelist.contains(material)) {
				placeBranch(materialFactory, block);
				branchLocations.add(block.getLocation());
			}
		}
		return branchLocations;
	}

	private Set<Location> buildBranch(Branch branch) {
		Set<Location> branchLocations = new HashSet<>();
		Material materialFactory = branch.getThickness() == Branch.Thickness.THICK ? theme.getThickBranch()
				: theme.getThinBranch();
		for (Block block : new SegmentIterator(world, branch.getBegin().add(origin), branch.getEnd().add(origin),
				branch.getRadius())) {
			Material material = block.getType();
			if (Passthrublocks.isPassable(material) || Passthrublocks.branchWhitelist.contains(material)) {
				placeBranch(materialFactory, block);
				branchLocations.add(block.getLocation());
			}
		}
		return branchLocations;
	}

	private void placeBranch(Material material, Block block) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.MAIN, new Runnable() {

			@Override
			public void run() {
				block.setType(material);
			}

		}, delay += delay2);
	}

	private Set<Location> buildRoot(Root root) {
		Set<Location> rootLocations = new HashSet<>();
		for (Block block : new FunctionIterator(world, root.getOrigin().add(origin), root.getPlane(), root.getLength(),
				root.getRadius(), root.getFunction())) {
			if (Passthrublocks.isPassable(block.getType()) || Passthrublocks.rootWhiteList.contains(block.getType())) {
				placeRoot(block);
				rootLocations.add(block.getLocation());
			}
		}
		return rootLocations;
	}

	private void placeRoot(Block target) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.MAIN, new Runnable() {

			@Override
			public void run() {
				target.setType(rootMaterial);
			}

		}, delay += delay2);
	}

	private Set<Location> buildLeafCluster(LeafCluster leafCluster) {
		Set<Location> leafLocations = new HashSet<>();
		double radius = leafCluster.getRadius();
		Vector center = leafCluster.getCenter().add(origin);
		for (int x = (int) radius * -1; x < radius + 0.5; x++) {
			for (int y = (int) (radius * -0.5); y < radius + 0.5; y++) {
				for (int z = (int) radius * -1; z < radius + 0.5; z++) {
					Vector current = center.clone().add(new Vector(x, y, z));
					if (center.distance(current) <= radius + 0.5) {
						attemptLeaf(current);
						leafLocations.add(current.toLocation(world));
					}
				}
			}
		}
		return leafLocations;
	}

	private void attemptLeaf(Vector current) {
		Location location = current.toLocation(world);
		Block target = location.getBlock();
		if (target.isEmpty() || selfMaterial.contains(target.getType()))
			placeLeaf(target);
	}

	private void placeLeaf(Block target) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.MAIN, new Runnable() {

			@Override
			public void run() {
				target.setType(leafMaterial);
			}

		}, delay += delay2);
	}

}