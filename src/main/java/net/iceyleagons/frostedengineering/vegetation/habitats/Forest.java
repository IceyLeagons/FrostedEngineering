package net.iceyleagons.frostedengineering.vegetation.habitats;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import com.google.common.collect.ImmutableSet;

import net.iceyleagons.frostedengineering.vegetation.Passthrublocks;

public class Forest implements IHabitat {

	@Override
	public boolean canPlantAt(Location location) {
		Block above = location.clone().add(0, 1, 0).getBlock();
		return soilMaterials.contains(location.getBlock().getType()) && Passthrublocks.isPassable(above.getType())
				&& !liquid.contains(above.getType());
	}

	@Override
	public boolean isSoil(Material material) {
		return soilMaterials.contains(material);
	}

	private Set<Material> soilMaterials = new ImmutableSet.Builder<Material>().add(Material.SNOW_BLOCK).build();

	private Set<Material> liquid = new ImmutableSet.Builder<Material>().add(Material.WATER).add(Material.ICE).build();
}