package net.iceyleagons.frostedengineering.vegetation.themes;

import java.util.Set;

import org.bukkit.Material;

public interface ITheme {

	Material getLeaf();

	Material getThickBranch();

	Material getThinBranch();

	Material getRoot();

	Set<Material> getSelfMaterials();

}