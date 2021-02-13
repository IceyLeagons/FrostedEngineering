package net.iceyleagons.worldgenerator.biome.impl;

import net.iceyleagons.icicle.wrapped.biome.WrappedBiomeBase;
import net.iceyleagons.icicle.wrapped.biome.WrappedBiomeStorage;
import net.iceyleagons.worldgenerator.biome.Biome;
import net.iceyleagons.worldgenerator.biome.BiomeManager;
import net.iceyleagons.worldgenerator.generator.FrostedChunkGenerator;
import net.iceyleagons.worldgenerator.generator.PerlinNoise;
import org.bukkit.Material;
import org.bukkit.block.data.Bisected;
import org.bukkit.generator.ChunkGenerator;

import java.awt.*;
import java.util.Random;

public class Sea extends Biome {
    WrappedBiomeBase sea = BiomeManager.biomeBaseMap.get("fe:sea");
    WrappedBiomeBase island = BiomeManager.biomeBaseMap.get("fe:island");
    WrappedBiomeBase plains = BiomeManager.biomeBaseMap.get("fe:plains");
    WrappedBiomeBase river = BiomeManager.biomeBaseMap.get("fe:river");

    public Sea(int seed, double centerX, double centerY) {
        super(seed, centerX, centerY);
    }

    @Override
    public WrappedBiomeBase.Geography getGeography() {
        return WrappedBiomeBase.Geography.OCEAN;
    }

    @Override
    public String getBiomeName() {
        return "sea";
    }

    @Override
    public Color getWaterColor() {
        return new Color(0, 79, 107);
    }

    @Override
    public double getHeight(int x, int z) {
        double firstTerrace = PerlinNoise.getNoise(x + seed, z + seed, 4, 5.0, 2.0, 4) * 0.2;
        double secondTerrace = PerlinNoise.getNoise(x + seed, z + seed, 4, 2.0f) * 0.05;
        return (int) (((firstTerrace + secondTerrace) / 1.05) * 255.0) + 40;
    }

    @Override
    public void populateChunkGenerator(BiomeManager manager, ChunkGenerator.ChunkData chunkData, int chunkX, int chunkY, int x, int z, Random random, WrappedBiomeStorage biomeStorage) {
        int terrainHeight = manager.getHeight(chunkX, chunkY);
        int riverDepth = FrostedChunkGenerator.getRiverDepth(chunkX, chunkY, seed);
        if (terrainHeight < 95) {
            chunkData.setBlock(x, terrainHeight, z, Material.SAND);
            chunkData.setBlock(x, terrainHeight - 1, z, Material.SAND);
            chunkData.setBlock(x, terrainHeight - 2, z, Material.SAND);
            chunkData.setBlock(x, terrainHeight - 3, z, Material.SAND);
            chunkData.setBlock(x, terrainHeight - 4, z, Material.STONE);
            int waterHeight = FrostedChunkGenerator.fillWithWater(x, 94, z, chunkData);
            if (80 - waterHeight > 0 && random.nextDouble() < 0.15) {
                double d = random.nextDouble();
                if (d < 0.25)
                    for (int i = waterHeight + 1; i <= waterHeight + random.nextInt(15); ++i)
                        chunkData.setBlock(x, i, z, Material.KELP_PLANT);
                else {
                    Bisected bisected = (Bisected) Material.TALL_SEAGRASS.createBlockData();
                    bisected.setHalf(Bisected.Half.TOP);
                    Bisected bisected2 = (Bisected) Material.TALL_SEAGRASS.createBlockData();
                    bisected2.setHalf(Bisected.Half.BOTTOM);
                    chunkData.setBlock(x, waterHeight + 1, z, bisected2);
                    chunkData.setBlock(x, waterHeight + 2, z, bisected);
                }
            }

            for (int i = 0; i < 256; i++)
                biomeStorage.setBiome(x >> 2, i >> 2, z >> 2, sea);
        } else if (terrainHeight < 97) {
            chunkData.setBlock(x, terrainHeight, z, Material.SAND);
            chunkData.setBlock(x, terrainHeight - 1, z, Material.DIRT);
            chunkData.setBlock(x, terrainHeight - 2, z, Material.DIRT);
            chunkData.setBlock(x, terrainHeight - 3, z, Material.DIRT);
            chunkData.setBlock(x, terrainHeight - 4, z, Material.STONE);
        } else if (terrainHeight < 101) {
            if (random.nextDouble() < 0.7) {
                chunkData.setBlock(x, terrainHeight, z, Material.GRASS_BLOCK);
                if (FrostedChunkGenerator.canCave(chunkX, terrainHeight + 1, chunkY, seed) && riverDepth == 0)
                    if (random.nextDouble() < 0.05)
                        chunkData.setBlock(x, terrainHeight + 1, z, Material.GRASS);
            } else
                chunkData.setBlock(x, terrainHeight, z, Material.SAND);
            chunkData.setBlock(x, terrainHeight - 1, z, Material.DIRT);
            chunkData.setBlock(x, terrainHeight - 2, z, Material.DIRT);
            chunkData.setBlock(x, terrainHeight - 3, z, Material.DIRT);
            chunkData.setBlock(x, terrainHeight - 4, z, Material.STONE);
        } else {
            if (random.nextDouble() < 0.9)
                chunkData.setBlock(x, terrainHeight, z, Material.GRASS_BLOCK);
            else
                chunkData.setBlock(x, terrainHeight, z, Material.PODZOL);

            if (FrostedChunkGenerator.canCave(chunkX, terrainHeight + 1, chunkY, seed) && riverDepth == 0)
                if (random.nextDouble() < 0.05)
                    chunkData.setBlock(x, terrainHeight + 1, z, Material.GRASS);

            chunkData.setBlock(x, terrainHeight - 1, z, Material.DIRT);
            chunkData.setBlock(x, terrainHeight - 2, z, Material.DIRT);
            chunkData.setBlock(x, terrainHeight - 3, z, Material.DIRT);
            chunkData.setBlock(x, terrainHeight - 4, z, Material.STONE);
        }
        if (riverDepth != 0) {
            for (int waterHeight = terrainHeight; waterHeight > terrainHeight - riverDepth && waterHeight >= 94; --waterHeight)
                chunkData.setBlock(x, waterHeight, z, Material.AIR);

            FrostedChunkGenerator.fillWithWater(x, Math.max(terrainHeight - 2, 94), z, chunkData);
            if (terrainHeight < 95)
                for (int i = 0; i < 256; i++)
                    biomeStorage.setBiome(x >> 2, i >> 2, z >> 2, sea);
            else
                for (int i = 0; i < 256; i++)
                    biomeStorage.setBiome(x >> 2, i >> 2, z >> 2, river);
        } else if (terrainHeight < 95)
            for (int i = 0; i < 256; i++)
                biomeStorage.setBiome(x >> 2, i >> 2, z >> 2, sea);
        else {
            int i = 255;
            while (chunkData.getType(x, i, z) == Material.AIR)
                i--;

            if (i < 126)
                for (int j = 0; j < 256; j++)
                    biomeStorage.setBiome(x >> 2, j >> 2, z >> 2, island);
            else
                for (int j = 0; j < 256; j++)
                    biomeStorage.setBiome(x >> 2, j >> 2, z >> 2, plains);
        }
        FrostedChunkGenerator.fillWithStone(x, terrainHeight - 5, z, chunkData);
    }
}
