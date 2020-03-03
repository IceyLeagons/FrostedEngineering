package net.iceyleagons.frostedengineering.generator.frosted;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import fastnoise.MathUtils;
import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.generator.frosted.ChunkData.PointData;

public class FrostedDimension extends ChunkGenerator {
	@Override
	public List<BlockPopulator> getDefaultPopulators(World world) {
		return Arrays.asList(new VegetationPopulator());
	}

	@Override
	public ChunkData generateChunkData(World world, Random random, int chunkx, int chunkz,
			ChunkGenerator.BiomeGrid biomes) {
		@SuppressWarnings("unused")
		long timeNow = System.currentTimeMillis();
		org.bukkit.generator.ChunkGenerator.ChunkData chunk = this.createChunkData(world);

		net.iceyleagons.frostedengineering.generator.frosted.ChunkData chunkBase = new net.iceyleagons.frostedengineering.generator.frosted.ChunkData(
				chunkx * 16, chunkz * 16, world.getSeed());
		for (int i = 0; i < net.iceyleagons.frostedengineering.generator.frosted.ChunkData.typeSize; i++) {
			PointData noiseData = chunkBase.pointMap[i];

			x: for (int x = 0; x < net.iceyleagons.frostedengineering.generator.frosted.ChunkData.mapSize; x++) {
				z: for (int z = 0; z < net.iceyleagons.frostedengineering.generator.frosted.ChunkData.mapSize; z++) {
					float noiseValue = noiseData.height[x][z];

					if (noiseValue == 0f)
						noiseValue = Float.MIN_NORMAL;

					switch (noiseData.pointType) {
					case TERRAIN: {
						Biome biome = Biome.getDominantBiome(noiseValue);

						noiseValue *= noiseData.pointType.multiplier;
						noiseValue += noiseData.pointType.offset;

						int y;
						for (y = 0; y < noiseValue; y++) {
							if (y == 0)
								chunk.setBlock(x, y, z, Material.BEDROCK);
							else if (y > noiseValue - chunkBase.iceHeight
									&& noiseData.pointType.blocks.heightDependentBlock != null)
								chunk.setBlock(x, y, z, noiseData.pointType.blocks.heightDependentBlock);
							else
								chunk.setBlock(x, y, z, noiseData.pointType.blocks.stoneBlock[random
										.nextInt(noiseData.pointType.blocks.stoneBlock.length)]);
						}

						if (chunk.getType(x, y + 1, z) != Material.WATER)
							chunk.setBlock(x, y, z, noiseData.pointType.blocks.surfaceBlock);

						if ((noiseData.pointType.doWater && chunk.getType(x,
								noiseData.pointType.offset + chunkBase.waterHeight, z) == Material.AIR)
								|| (noiseData.pointType.doWater && chunk.getType(x,
										noiseData.pointType.offset + chunkBase.waterHeight, z) == Material.SNOW)) {
							for (int i2 = noiseData.pointType.offset
									+ chunkBase.waterHeight; i2 >= noiseData.pointType.offset; i2--) {
								Material mat = chunk.getType(x, i2, z);
								if (mat == Material.AIR || mat == Material.SNOW || mat == Material.SNOW_BLOCK) {
									if (i2 == noiseData.pointType.offset + chunkBase.waterHeight)
										chunk.setBlock(x, i2, z, Material.ICE);
									else {
										setNeighboringToStone(chunk, x, i2, z);
										chunk.setBlock(x, i2, z, Material.WATER);
									}
								} else if (mat == Material.ICE) {
									chunk.setBlock(x, i2 + 1, z, Material.STONE);
									i2 = noiseData.pointType.offset - 1;
								}
								mat = null;
							}
						}

						if (biome != null)
							biomes.setBiome(x, z, biome.biome);

						continue z;
					}
					case CLOUD: {
						if (noiseValue > noiseData.pointType.threshold) {
							noiseValue = MathUtils.fastPow(noiseValue, 3.D);
							noiseValue *= noiseData.pointType.multiplier;

							if (noiseValue >= chunkBase.cloudNoiseLimit)
								noiseValue = chunkBase.cloudNoiseLimit;

							int yMax = MathUtils.toInteger(noiseData.pointType.offset + noiseValue);
							int yMin = MathUtils.toInteger(noiseData.pointType.offset - noiseValue);

							for (int y = yMin; yMax > y; y++)
								chunk.setBlock(x, y, z, noiseData.pointType.blocks.stoneBlock[random
										.nextInt(noiseData.pointType.blocks.stoneBlock.length)]);
						}

						continue z;
					}
					case ISLAND: {
						noiseValue /= 3.f;
						float downwardNoise = MathUtils.fastPow(noiseValue, 6.D);
						if (downwardNoise > noiseData.pointType.threshold / 3.f) {
							noiseValue *= noiseData.pointType.multiplier;
							downwardNoise *= noiseData.pointType.multiplier / 5.75f;
							downwardNoise /= 2.f;

							int yMin = MathUtils.toInteger(noiseData.pointType.offset - downwardNoise);
							int yMax = MathUtils.toInteger(noiseData.pointType.offset + noiseValue);

							int y;
							for (y = yMin; yMax > y; y++)
								chunk.setBlock(x, y, z, noiseData.pointType.blocks.stoneBlock[random
										.nextInt(noiseData.pointType.blocks.stoneBlock.length)]);

							chunk.setBlock(yMax, y, z, noiseData.pointType.blocks.surfaceBlock);
						}

						continue z;
					}
					default: {
						// Do nothing... this isn't supposed to happen.
						Main.debug("Impossible path reached. / New terrain type added, and not yet used.");
						Main.debug("Either way, please report.");
						break x;
					}
					}
				}
			}
		}

		Main.debug("Time spent generating new chunk: " + (chunkBase.timeTookModifying + chunkBase.timeTookNoising)
				+ "ms.");

		return chunk;
	}

	public void setNeighboringToStone(ChunkData chunk, int x, int y, int z) {
		if (chunk.getType(x + 1, y, z) == Material.ICE)
			chunk.setBlock(x + 1, y, z, Material.STONE);
		else if (chunk.getType(x - 1, y, z) == Material.ICE)
			chunk.setBlock(x - 1, y, z, Material.STONE);
		else if (chunk.getType(x, y, z + 1) == Material.ICE)
			chunk.setBlock(x, y, z + 1, Material.STONE);
		else if (chunk.getType(x, y, z - 1) == Material.ICE)
			chunk.setBlock(x, y, z - 1, Material.STONE);
	}
}
