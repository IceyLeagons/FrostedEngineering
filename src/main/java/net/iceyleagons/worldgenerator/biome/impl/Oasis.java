package net.iceyleagons.worldgenerator.biome.impl;

import net.iceyleagons.icicle.wrapped.biome.WrappedBiomeBase;
import net.iceyleagons.icicle.wrapped.biome.WrappedBiomeStorage;
import net.iceyleagons.worldgenerator.biome.Biome;
import net.iceyleagons.worldgenerator.biome.BiomeManager;
import net.iceyleagons.worldgenerator.generator.FrostedChunkGenerator;
import net.iceyleagons.worldgenerator.generator.PerlinNoise;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class Oasis extends Biome implements ICommons {
    WrappedBiomeBase river = BiomeManager.biomeBaseMap.get("fe:river");
    WrappedBiomeBase oasis = BiomeManager.biomeBaseMap.get("fe:oasis");

    BlockData[] datas = new BlockData[]{Material.SAND.createBlockData(), Material.SANDSTONE.createBlockData(), Material.SANDSTONE_STAIRS.createBlockData("[half=top]")};

    public Oasis(int seed, double centerX, double centerY) {
        super(seed, centerX, centerY);
    }

    @Override
    public WrappedBiomeBase.Precipitation getPrecipitation() {
        return WrappedBiomeBase.Precipitation.NONE;
    }

    @Override
    public WrappedBiomeBase.Geography getGeography() {
        return WrappedBiomeBase.Geography.DESERT;
    }

    @Override
    public String getBiomeName() {
        return "oasis";
    }

    @Override
    public double getHeight(int x, int z) {
        double firstTerrace = PerlinNoise.getNoise(x + seed, z + seed, 4, 0.2f);
        double secondTerrace = PerlinNoise.getNoise(x + seed, z + seed, 4, 0.4f) * 0.25;
        double thirdTerrace = PerlinNoise.getNoise(x + seed, z + seed, 4, 0.8f) * 0.125;
        double fourthTerrace = PerlinNoise.getNoise(x + seed, z + seed, 4, 1.6f) * 0.0625;
        return (int) (((firstTerrace + secondTerrace + thirdTerrace + fourthTerrace) / 4.0) * 255.0) + 60;
    }

    @Override
    public void populateChunkGenerator(BiomeManager manager, ChunkGenerator.ChunkData chunkData, int chunkX, int chunkZ, int x, int z, Random random, WrappedBiomeStorage biomeStorage) {
        int height = manager.getHeight(chunkX, chunkZ);
        int riverDepth = FrostedChunkGenerator.getRiverDepth(chunkX, chunkZ, seed);
        if (height < 94) {
            fillWithEither(random, chunkData, x, height, z, datas);
            chunkData.setBlock(x, height - 1, z, Material.SAND);
            chunkData.setBlock(x, height - 2, z, Material.SAND);
            chunkData.setBlock(x, height - 3, z, Material.SAND);
            chunkData.setBlock(x, height - 4, z, Material.STONE);
            FrostedChunkGenerator.fillWithWater(x, 94, z, chunkData);
            for (int i = 0; i < 256; i++)
                biomeStorage.setBiome(x >> 2, i >> 2, z >> 2, river);
        } else if (height < 109) {
            if (random.nextDouble() > 1.5 - 0.05 * (double) height - 94) {
                chunkData.setBlock(x, height, z, Material.SAND);
                if (random.nextDouble() < 0.005 && riverDepth == 0 && FrostedChunkGenerator.canCave(chunkX, height + 1, chunkZ, seed))
                    for (int i = 1; i < random.nextInt(5); i++)
                        chunkData.setBlock(x, height + i, z, Material.CACTUS);
            } else {
                chunkData.setBlock(x, height, z, Material.GRASS_BLOCK);
                if (random.nextDouble() < 0.1 && riverDepth == 0 && FrostedChunkGenerator.canCave(chunkX, height + 1, chunkZ, seed))
                    chunkData.setBlock(x, height + 1, z, Material.GRASS);
            }
            chunkData.setBlock(x, height - 1, z, Material.SAND);
            chunkData.setBlock(x, height - 2, z, Material.SAND);
            chunkData.setBlock(x, height - 3, z, Material.SAND);
            chunkData.setBlock(x, height - 4, z, Material.STONE);
            if (riverDepth != 0) {
                for (int i = height; i > height - riverDepth && i >= 94; --i)
                    chunkData.setBlock(x, i, z, Material.AIR);

                FrostedChunkGenerator.fillWithWater(x, Math.max(height - 2, 94), z, chunkData);
                for (int i = 0; i < 256; i++)
                    biomeStorage.setBiome(x >> 2, i >> 2, z >> 2, river);
            } else for (int i = 0; i < 256; i++)
                biomeStorage.setBiome(x >> 2, i >> 2, z >> 2, oasis);
        } else {
            fillWithEither(random, chunkData, x, height, z, datas);

            if (random.nextDouble() < 0.005 && riverDepth == 0 && FrostedChunkGenerator.canCave(chunkX, height + 1, chunkZ, seed))
                for (int i = 1; i < random.nextInt(5); i++)
                    chunkData.setBlock(x, height + i, z, Material.CACTUS);

            chunkData.setBlock(x, height - 1, z, Material.SAND);
            chunkData.setBlock(x, height - 2, z, Material.SAND);
            chunkData.setBlock(x, height - 3, z, Material.SAND);
            chunkData.setBlock(x, height - 4, z, Material.STONE);
            if (riverDepth != 0) {
                for (int i = height; i > height - riverDepth && i >= 94; --i)
                    chunkData.setBlock(x, i, z, Material.AIR);

                FrostedChunkGenerator.fillWithWater(x, Math.max(height - 2, 94), z, chunkData);
                for (int i = 0; i < 256; i++)
                    biomeStorage.setBiome(x >> 2, i >> 2, z >> 2, river);
            } else
                for (int i = 0; i < 256; i++)
                    biomeStorage.setBiome(x >> 2, i >> 2, z >> 2, oasis);
        }
        FrostedChunkGenerator.fillWithStone(x, height - 5, z, chunkData);
    }
}
