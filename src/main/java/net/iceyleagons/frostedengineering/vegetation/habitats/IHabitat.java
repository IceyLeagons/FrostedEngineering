package net.iceyleagons.frostedengineering.vegetation.habitats;

import org.bukkit.Location;
import org.bukkit.Material;

public interface IHabitat {

	boolean canPlantAt(Location location);

	boolean isSoil(Material material);

}
