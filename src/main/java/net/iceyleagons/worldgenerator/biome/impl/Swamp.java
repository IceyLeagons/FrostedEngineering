package net.iceyleagons.worldgenerator.biome.impl;

import net.iceyleagons.icicle.wrapped.biome.WrappedBiomeBase;
import net.iceyleagons.icicle.wrapped.biome.WrappedBiomeStorage;
import net.iceyleagons.worldgenerator.biome.Biome;
import net.iceyleagons.worldgenerator.biome.BiomeManager;
import net.iceyleagons.worldgenerator.generator.FrostedChunkGenerator;
import net.iceyleagons.worldgenerator.generator.PerlinNoise;
import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;

import java.awt.*;
import java.util.Random;

public class Swamp extends Biome implements ICommons {
    WrappedBiomeBase river = BiomeManager.biomeBaseMap.get("fe:river");
    WrappedBiomeBase swamp = BiomeManager.biomeBaseMap.get("fe:swamp");

    public Swamp(int seed, double centerX, double centerY) {
        super(seed, centerX, centerY);
    }

    @Override
    public String getBiomeName() {
        return "swamp";
    }

    @Override
    public Color getWaterColor() {
        return new Color(0, 255, 170);
    }

    @Override
    public Color getWaterFogColor() {
        return new Color(0, 107, 87);
    }

    @Override
    public WrappedBiomeBase.Precipitation getPrecipitation() {
        return WrappedBiomeBase.Precipitation.RAIN;
    }

    @Override
    public WrappedBiomeBase.Geography getGeography() {
        return WrappedBiomeBase.Geography.SWAMP;
    }

    @Override
    public double getTreeChance() {
        return 0.75;
    }

    @Override
    public double getHeight(int x, int z) {
        double firstTerrace = PerlinNoise.getNoise(x + seed, z + seed, 4, 1.0f);
        double secondTerrace = PerlinNoise.getNoise(x + seed, z + seed, 4, 2.0f) * 0.2;
        double thirdTerrace = PerlinNoise.getNoise(x + seed, z + seed, 4, 5.0f) * 0.3;

        return (int) (Math.round(((firstTerrace + secondTerrace + thirdTerrace) / 14.0) * 255.0) + 80);
    }

    @Override
    public void populateChunkGenerator(BiomeManager manager, ChunkGenerator.ChunkData chunkData, int chunkX, int chunkY, int x, int z, Random random, WrappedBiomeStorage biomeStorage) {
        int height = manager.getHeight(chunkX, chunkY);
        if (!isTaller(x, height, z, chunkData, biomeStorage, river)) {
            if (random.nextDouble() < 0.1)
                chunkData.setBlock(x, 94 + 1, z, Material.LILY_PAD);
        } else {
            int riverDepth = FrostedChunkGenerator.getRiverDepth(chunkX, chunkY, seed);
            if (random.nextDouble() < 0.1)
                chunkData.setBlock(x, height, z, Material.PODZOL);
            else {
                chunkData.setBlock(x, height, z, Material.GRASS_BLOCK);
                if (riverDepth == 0 && random.nextDouble() < 0.2 && FrostedChunkGenerator.canCave(chunkX, height + 1, chunkY, seed))
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
                if (random.nextDouble() < 0.2)
                    chunkData.setBlock(x, Math.max(height - 2, 94) + 1, z, Material.LILY_PAD);

                for (int i = 0; i < 256; i++)
                    biomeStorage.setBiome(x >> 2, i >> 2, z >> 2, river);
            } else
                for (int i = 0; i < 256; i++)
                    biomeStorage.setBiome(x >> 2, i >> 2, z >> 2, swamp);
        }
        FrostedChunkGenerator.fillWithStone(x, height - 5, z, chunkData);
    }
}
