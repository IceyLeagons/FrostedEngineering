package net.iceyleagons.frostedengineering.vegetation;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;

public class Passthrublocks {
	public static boolean isPassable(Material material) {
		return passableMaterials.contains(material);
	}

	public static Set<Material> passableMaterials = createPassableSet();

	public static HashSet<Material> createPassableSet() {
		HashSet<Material> passableSet = new HashSet<>();
		passableSet.add(Material.AIR);
		passableSet.add(Material.GRASS);
		passableSet.add(Material.OAK_SAPLING);
		passableSet.add(Material.SPRUCE_SAPLING);
		passableSet.add(Material.BIRCH_SAPLING);
		passableSet.add(Material.JUNGLE_SAPLING);
		passableSet.add(Material.ACACIA_SAPLING);
		passableSet.add(Material.DARK_OAK_SAPLING);
		passableSet.add(Material.GRASS);
		passableSet.add(Material.DANDELION);
		passableSet.add(Material.POPPY);
		passableSet.add(Material.BLUE_ORCHID);
		passableSet.add(Material.ALLIUM);
		passableSet.add(Material.AZURE_BLUET);
		passableSet.add(Material.RED_TULIP);
		passableSet.add(Material.ORANGE_TULIP);
		passableSet.add(Material.WHITE_TULIP);
		passableSet.add(Material.PINK_TULIP);
		passableSet.add(Material.OXEYE_DAISY);
		passableSet.add(Material.BROWN_MUSHROOM);
		passableSet.add(Material.RED_MUSHROOM);
		passableSet.add(Material.COBWEB);
		passableSet.add(Material.FERN);
		passableSet.add(Material.DEAD_BUSH);
		passableSet.add(Material.VINE);
		passableSet.add(Material.LILY_PAD);
		passableSet.add(Material.LILAC);
		passableSet.add(Material.TALL_GRASS);
		passableSet.add(Material.LARGE_FERN);
		passableSet.add(Material.ROSE_BUSH);
		passableSet.add(Material.PEONY);
		passableSet.add(Material.SWEET_BERRY_BUSH);
		passableSet.add(Material.SUGAR_CANE);
		passableSet.add(Material.SNOW);

		return passableSet;
	}

	public static Set<Material> trunkWhitelist = makeTrunkWhiteList();

	public static Set<Material> branchWhitelist = makeBranchWhiteList();

	public static Set<Material> makeTrunkWhiteList() {
		Set<Material> trunkWhitelist = new HashSet<>();
		trunkWhitelist.add(Material.DIRT);
		trunkWhitelist.add(Material.GRASS_BLOCK);
		trunkWhitelist.add(Material.COARSE_DIRT);
		trunkWhitelist.add(Material.SOUL_SAND);
		trunkWhitelist.add(Material.SAND);
		trunkWhitelist.add(Material.GRAVEL);
		trunkWhitelist.add(Material.GRASS_PATH);
		trunkWhitelist.add(Material.OAK_LEAVES);
		trunkWhitelist.add(Material.SPRUCE_LEAVES);
		trunkWhitelist.add(Material.BIRCH_LEAVES);
		trunkWhitelist.add(Material.JUNGLE_LEAVES);
		trunkWhitelist.add(Material.ACACIA_LEAVES);
		trunkWhitelist.add(Material.DARK_OAK_LEAVES);
		return trunkWhitelist;
	}

	public static Set<Material> makeBranchWhiteList() {
		Set<Material> leafWhiteList = new HashSet<>();
		leafWhiteList.add(Material.OAK_LEAVES);
		leafWhiteList.add(Material.SPRUCE_LEAVES);
		leafWhiteList.add(Material.BIRCH_LEAVES);
		leafWhiteList.add(Material.JUNGLE_LEAVES);
		leafWhiteList.add(Material.ACACIA_LEAVES);
		leafWhiteList.add(Material.DARK_OAK_LEAVES);
		return leafWhiteList;
	}

	public static Set<Material> rootWhiteList = makeRootWhiteList();

	public static Set<Material> makeRootWhiteList() {
		Set<Material> rootWhiteList = new HashSet<>();
		rootWhiteList.add(Material.STONE);
		rootWhiteList.add(Material.ANDESITE);
		rootWhiteList.add(Material.DIORITE);
		rootWhiteList.add(Material.GRANITE);
		return rootWhiteList;
	}
}
