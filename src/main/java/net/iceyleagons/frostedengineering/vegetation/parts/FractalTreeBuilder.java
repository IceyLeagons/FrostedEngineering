package net.iceyleagons.frostedengineering.vegetation.parts;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.gui.InventoryFactory;
import net.iceyleagons.frostedengineering.gui.InventoryFactory.ClickRunnable;
import net.iceyleagons.frostedengineering.utils.math.FunctionIterator;
import net.iceyleagons.frostedengineering.vegetation.Genes;
import net.iceyleagons.frostedengineering.vegetation.Passthrublocks;
import net.iceyleagons.frostedengineering.vegetation.SegmentIterator;
import net.iceyleagons.frostedengineering.vegetation.parts.Branch.Thickness;

public class FractalTreeBuilder extends Tree {

	private Set<Material> selfMaterial;

	private Branch trunk;
	private List<Root> roots;
	private List<List<Branch>> branches;
	private List<List<LeafCluster>> leafClusters;

	private long delay = 0L;
	private long delay2 = 1L;
	private long currentlyAt = 0L;

	private Location al;

	public Inventory inventory;
	public ArrayList<Player> openedFor = new ArrayList<>();

	private boolean sound = false;

	private Material rootMaterial;
	private Material leafMaterial;

	public FractalTreeBuilder(Genes gene, Player planter, Location seed, FractalBlueprint blueprint) {
		super(gene, planter, seed);

		new BukkitRunnable() {

			@Override
			public void run() {
				Main.debug(al.getBlock().getType().toString());
				if (seed.getBlock().getType() != Material.OAK_SAPLING)
					this.cancel();

				InventoryFactory inv = new InventoryFactory("§a" + gene.name(), 27,
						new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE), true);

				LocalTime a = LocalTime.ofSecondOfDay(Math.round(currentlyAt / 20));

				inv.setItem(new ItemStack(Material.CLOCK), "§6§lTime left", 10, new ClickRunnable() {

					@Override
					public void run(InventoryClickEvent e) {
					}

				}, String.format("§c%d hour(s), %d minutes and %d seconds", a.getHour(), a.getMinute(), a.getSecond()));

				LocalTime a2 = LocalTime.ofSecondOfDay(Math.round(gene.growTime / 20));

				inv.setItem(new ItemStack(Material.PAPER), "§lPlant details", 13, new ClickRunnable() {

					@Override
					public void run(InventoryClickEvent e) {
					}

				}, "§7Plant name: §6§l" + gene.name(), "§7Growth duration: §6§l" + String
						.format("%d hour(s), %d minutes and %d seconds", a2.getHour(), a2.getMinute(), a2.getSecond()));

				inv.setItem(new ItemStack(Material.BARRIER), "§4§lRemove plant", 16, new ClickRunnable() {

					@Override
					public void run(InventoryClickEvent e) {
						new BukkitRunnable() {

							@Override
							public void run() {
								seed.getBlock().setType(Material.AIR);
								e.getWhoClicked().closeInventory();
							}

						}.runTask(Main.MAIN);
					}

				}, "");

				inventory = inv.getSourceInventory();

				openedFor.forEach((player) -> {
					new BukkitRunnable() {

						@Override
						public void run() {
							player.openInventory(inventory);
						}

					}.runTask(Main.MAIN);
				});
			}
		}.runTaskTimer(Main.MAIN, 0L, 20L);

		this.selfMaterial = theme.getSelfMaterials();
		this.rootMaterial = theme.getRoot();
		this.leafMaterial = theme.getLeaf();

		this.inventory = Bukkit.createInventory(null, 27);

		this.trunk = blueprint.getTrunk();
		this.roots = blueprint.getRoots();
		this.branches = blueprint.getBranches();
		this.leafClusters = blueprint.getLeafClusters();

		this.currentlyAt = gene.growTime;
		this.al = seed.add(new Vector(0, 1, 0));
	}

	public void openInventory(Player player) {
		openedFor.add(player);
	}

	private void placeSync(Block block, Material mat) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.MAIN, new Runnable() {

			@Override
			public void run() {
				block.setType(mat);
				if (sound)
					switch (mat) {
					case SPRUCE_LEAVES: {
						block.getWorld().playSound(block.getLocation(), Sound.BLOCK_GRASS_BREAK, .5f,
								random.nextFloat() * .5f);
						break;
					}
					default: {
						block.getWorld().playSound(block.getLocation(), Sound.BLOCK_WOOD_PLACE, .5f,
								random.nextFloat() * .75f);
						break;
					}
					}
				block.getWorld().playSound(block.getLocation(), Sound.BLOCK_GRASS_BREAK, 1f, random.nextFloat() * .5f);
			}

		}, delay += delay2);

		currentlyAt -= delay2;
	}

	@Override
	public void growPhased(long delay) {
		this.delay2 = delay;
		grow();
	}

	private List<Branch> getBranches(int n) {
		return this.branches.get(n);
	}

	private List<LeafCluster> getLeafCluster(int n) {
		return this.leafClusters.get(n);
	}

	private void grow() {
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

			if (leafMaterial != null) {
				if (leafClusters.size() > phase) {
					finished = false;
					List<LeafCluster> leafClusters = getLeafCluster(phase);
					if (leafClusters != null) {
						for (LeafCluster leafCluster : leafClusters) {
							buildLeafCluster(leafCluster);
						}
					}
				}
			}

			phase++;
		}
	}

	@Override
	public void growInstant() {
		this.delay2 = 0L;
		grow();
	}

	private Set<Location> buildTrunk(Branch branch) {
		Set<Location> branchLocations = new HashSet<>();
		Material trunkMaterial = branch.getThickness() == Branch.Thickness.THICK ? theme.getThickBranch()
				: theme.getThinBranch();
		for (Block block : new SegmentIterator(world, branch.getBegin().add(origin), branch.getEnd().add(origin),
				branch.getRadius())) {
			Material blockMaterial = block.getType();
			if (Passthrublocks.isPassable(blockMaterial) || Passthrublocks.trunkWhitelist.contains(blockMaterial)) {
				placeSync(block, trunkMaterial);
				branchLocations.add(block.getLocation());
			}
		}
		return branchLocations;
	}

	private Set<Location> buildBranch(Branch branch) {
		Set<Location> branchLocations = new HashSet<>();
		Material branchMaterial = branch.getThickness() == Branch.Thickness.THICK ? theme.getThickBranch()
				: theme.getThinBranch();
		if ((branch.getThickness() == Thickness.THIN && theme.getThinBranch() != null)
				|| branch.getThickness() == Thickness.THICK)
			for (Block block : new SegmentIterator(world, branch.getBegin().add(origin), branch.getEnd().add(origin),
					branch.getRadius())) {
				Material blockMaterial = block.getType();
				if (Passthrublocks.isPassable(blockMaterial)
						|| Passthrublocks.branchWhitelist.contains(blockMaterial)) {
					placeSync(block, branchMaterial);
					branchLocations.add(block.getLocation());
				}
			}
		return branchLocations;
	}

	private Set<Location> buildRoot(Root root) {
		Set<Location> rootLocations = new HashSet<>();
		if (rootMaterial != null) {
			for (Block block : new FunctionIterator(world, root.getOrigin().add(origin), root.getPlane(),
					root.getLength(), root.getRadius(), root.getFunction())) {
				if (Passthrublocks.isPassable(block.getType())
						|| Passthrublocks.rootWhiteList.contains(block.getType())) {
					placeSync(block, rootMaterial);
					rootLocations.add(block.getLocation());
				}
			}
		}
		return rootLocations;
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
			placeSync(target, leafMaterial);
	}

	public FractalTreeBuilder setSound(boolean sound) {
		this.sound = sound;
		return this;
	}

}