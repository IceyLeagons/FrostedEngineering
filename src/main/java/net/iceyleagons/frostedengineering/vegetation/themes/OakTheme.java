package net.iceyleagons.frostedengineering.vegetation.themes;

import java.util.Collections;
import java.util.Set;

import org.bukkit.Material;

public class OakTheme implements ITheme {

	@Override
	public Material getLeaf() {
		return Material.SPRUCE_LEAVES;
	}

	@Override
	public Material getThickBranch() {
		return Material.STRIPPED_SPRUCE_WOOD;
	}

	@Override
	public Material getThinBranch() {
		return Material.OAK_FENCE;
	}

	@Override
	public Material getRoot() {
		return Material.STRIPPED_SPRUCE_WOOD;
	}

	@Override
	public Set<Material> getSelfMaterials() {
		return Collections.emptySet();
	}
}