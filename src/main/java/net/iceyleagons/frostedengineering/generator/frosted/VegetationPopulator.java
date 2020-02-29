package net.iceyleagons.frostedengineering.generator.frosted;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

import net.iceyleagons.frostedengineering.vegetation.Genes;
import net.iceyleagons.frostedengineering.vegetation.habitats.Forest;
import net.iceyleagons.frostedengineering.vegetation.habitats.IHabitat;

public class VegetationPopulator extends BlockPopulator {

	@Override
	public void populate(World world, Random random, Chunk chunk) {
		int chunkX = chunk.getX() * 16;
		int chunkZ = chunk.getZ() * 16;
		if (random.nextDouble() < ChunkData.vegetationChance) {
			Genes species = Genes.randomGene();
			IHabitat habitat = new Forest();
			for (int i = 0; i < random.nextInt(ChunkData.vegetationNumber) + 1; i++) {
				int overflow = ChunkData.vegetationOverflow;
				int x = chunkX + random.nextInt(15 + overflow * 2) - overflow;
				int z = chunkZ + random.nextInt(15 + overflow * 2) - overflow;
				if (Biome.fromBiome(world.getBiome(x, z)) == Biome.MOUNTAINS)
					return;
				Location location = getHighestValid(habitat, world, x, z);
				if (location != null)
					species.growPlant(location);
			}
		}

	}

	private Location getHighestValid(IHabitat habitat, World world, int x, int z) {
		Location location = world.getHighestBlockAt(x, z).getLocation();
		int count = 0;
		while (!habitat.canPlantAt(location) && count < 65) {
			location.add(0, -1, 0);
			count++;
		}
		if (habitat.canPlantAt(location)) {
			return location;
		} else {
			return null;
		}
	}

}
