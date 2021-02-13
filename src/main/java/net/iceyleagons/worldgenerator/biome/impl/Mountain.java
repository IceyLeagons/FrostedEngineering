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

public class Mountain extends Biome implements ICommons {
    WrappedBiomeBase river = BiomeManager.biomeBaseMap.get("fe:river");
    WrappedBiomeBase mountain = BiomeManager.biomeBaseMap.get("fe:mountain");

    public Mountain(int seed, double centerX, double centerY) {
        super(seed, centerX, centerY);
    }

    @Override
    public WrappedBiomeBase.Precipitation getPrecipitation() {
        return WrappedBiomeBase.Precipitation.SNOW;
    }

    @Override
    public WrappedBiomeBase.TemperatureModifier getTemperatureModifier() {
        return WrappedBiomeBase.TemperatureModifier.FROZEN;
    }

    @Override
    public WrappedBiomeBase.Geography getGeography() {
        return WrappedBiomeBase.Geography.EXTREME_HILLS;
    }

    @Override
    public String getBiomeName() {
        return "mountain";
    }

    @Override
    public double getHeight(int x, int z) {
        double firstTerrace = PerlinNoise.getNoise(x + seed, z + seed, 4, 0.3f) * 0.5;
        double secondTerrace = PerlinNoise.getNoise(x + seed, z + seed, 4, 2.0f) * 0.05;
        return (int) Math.round(((firstTerrace + secondTerrace) / 0.75) * 255.0) + 60;
    }

    BlockData[] groundMaterials = new BlockData[]{Material.GRASS_BLOCK.createBlockData(), Material.COARSE_DIRT.createBlockData()};

    @Override
    public void populateChunkGenerator(BiomeManager manager, ChunkGenerator.ChunkData chunkData, int chunkX, int chunkY, int x, int z, Random random, WrappedBiomeStorage biomeStorage) {
        int height = manager.getHeight(chunkX, chunkY);
        if (isTaller(x, height, z, chunkData, biomeStorage, river)) {
            int riverDepth = FrostedChunkGenerator.getRiverDepth(chunkX, chunkY, seed);
            int randomHeight = height + random.nextInt(20) - 10;
            if (randomHeight > 150) {
                // If the mountain is taller than 185 blocks, then start adding snow, otherwise add grass blocks with snow on top.
                if (height < 185)
                    chunkData.setBlock(x, height, z, Material.GRASS_BLOCK);
                else
                    chunkData.setBlock(x, height, z, random.nextDouble() + ((height - 185) * .0333) > 1 ? Material.SNOW_BLOCK : Material.GRASS_BLOCK);


                if (riverDepth == 0 && FrostedChunkGenerator.canCave(chunkX, height + 1, chunkY, seed))
                    if (height >= 165 && random.nextDouble() + Math.abs(height - 165) * .02 > 1) {
                        chunkData.setBlock(x, height + 1, z, Material.SNOW);
                    } else if (random.nextDouble() < 0.5)
                        chunkData.setBlock(x, height + 1, z, Material.GRASS);
            } else {
                fillWithEither(random, chunkData, x, height, z, groundMaterials);

                if (riverDepth == 0 && random.nextDouble() < 0.05 && FrostedChunkGenerator.canCave(chunkX, height + 1, chunkY, seed))
                    chunkData.setBlock(x, height + 1, z, Material.GRASS);
            }

            chunkData.setBlock(x, height - 1, z, Material.DIRT);
            chunkData.setBlock(x, height - 2, z, Material.DIRT);
            chunkData.setBlock(x, height - 3, z, Material.DIRT);
            chunkData.setBlock(x, height - 4, z, Material.STONE);
            if (riverDepth != 0) {
                for (int i = height; i > height - riverDepth && i >= 94; --i)
                    chunkData.setBlock(x, i, z, Material.AIR);

                FrostedChunkGenerator.fillWithWater(x, Math.max(height - 2, 94), z, chunkData);
                for (int i = 0; i < 256; i++)
                    biomeStorage.setBiome(x >> 2, i >> 2, z >> 2, river);
            } else
                for (int i = 0; i < 256; i++)
                    biomeStorage.setBiome(x >> 2, i >> 2, z >> 2, mountain);
        }

        FrostedChunkGenerator.fillWithStone(x, height - 5, z, chunkData);
    }
}
