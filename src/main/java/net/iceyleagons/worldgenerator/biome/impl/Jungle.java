package net.iceyleagons.worldgenerator.biome.impl;

import net.iceyleagons.icicle.wrapped.biome.WrappedBiomeBase;
import net.iceyleagons.icicle.wrapped.biome.WrappedBiomeStorage;
import net.iceyleagons.worldgenerator.WorldGenerator;
import net.iceyleagons.worldgenerator.biome.Biome;
import net.iceyleagons.worldgenerator.biome.BiomeManager;
import net.iceyleagons.worldgenerator.generator.FrostedChunkGenerator;
import net.iceyleagons.worldgenerator.generator.PerlinNoise;
import org.bukkit.Material;
import org.bukkit.block.data.Bisected;
import org.bukkit.generator.ChunkGenerator;

import java.awt.*;
import java.util.Random;

public class Jungle extends Biome implements ICommons {
    WrappedBiomeBase river = BiomeManager.biomeBaseMap.get("fe:river");
    WrappedBiomeBase jungle = BiomeManager.biomeBaseMap.get("fe:jungle");

    public Jungle(int seed, double centerX, double centerY) {
        super(seed, centerX, centerY);
    }

    @Override
    public WrappedBiomeBase.Geography getGeography() {
        return WrappedBiomeBase.Geography.JUNGLE;
    }

    @Override
    public String getBiomeName() {
        return "jungle";
    }

    @Override
    public Color getWaterColor() {
        return new Color(0, 187, 255);
    }

    @Override
    public Color getWaterFogColor() {
        return new Color(0, 79, 107);
    }

    @Override
    public double getTreeChance() {
        return 0.9;
    }

    @Override
    public double getHeight(int x, int z) {
        return (int) (Math.round((PerlinNoise.getNoise(x + seed, z + seed, 4, 0.3f) * 0.4) * 255.0) + 63);
    }

    @Override
    public void populateChunkGenerator(BiomeManager manager, ChunkGenerator.ChunkData chunkData,
                                       int chunkX, int chunkZ, int x, int z, Random random, WrappedBiomeStorage biomeStorage) {
        int height = manager.getHeight(chunkX, chunkZ);

        if (!isTaller(x, height, z, chunkData, biomeStorage, river)) {
            // Lilypads in rivers.
            if (random.nextDouble() < 0.05)
                chunkData.setBlock(x, 94 + 1, z, Material.LILY_PAD);
        } else {
            int riverDepth = FrostedChunkGenerator.getRiverDepth(chunkX, chunkZ, seed);
            double groundFactor = random.nextDouble();

            if (groundFactor <= 0.72) {
                chunkData.setBlock(x, height, z, Material.GRASS_BLOCK);

                if (riverDepth == 0 && FrostedChunkGenerator.canCave(chunkX, height + 1, chunkZ, seed))
                    spreadPlants(chunkData, x, height, z, random);
            } else if (groundFactor <= 0.9)
                chunkData.setBlock(x, height, z, Material.GRASS_PATH);
            else {
                chunkData.setBlock(x, height, z, Material.PODZOL);

                if (riverDepth == 0 && FrostedChunkGenerator.canCave(chunkX, height + 1, chunkZ, seed))
                    spreadPlants(chunkData, x, height, z, random);
            }

            if (random.nextDouble() < 0.07 && riverDepth == 0 && FrostedChunkGenerator.canCave(chunkX, height + 1, chunkZ, seed)) {
                chunkData.setBlock(x, height + 1, z, WorldGenerator.permanentLeaves);
                chunkData.setBlock(x, height + 2, z, Material.AIR);
            }

            chunkData.setBlock(x, height - 1, z, Material.DIRT);
            chunkData.setBlock(x, height - 2, z, Material.DIRT);
            chunkData.setBlock(x, height - 3, z, Material.DIRT);
            chunkData.setBlock(x, height - 4, z, Material.STONE);

            if (riverDepth != 0) {
                for (int i = height; i > height - riverDepth && i >= 94; --i)
                    chunkData.setBlock(x, i, z, Material.AIR);

                FrostedChunkGenerator.fillWithWater(x, Math.max(height - 2, 94), z, chunkData);
                // Lilypads in ponds.
                if (random.nextDouble() < 0.1)
                    chunkData.setBlock(x, Math.max(height - 2, 94) + 1, z, Material.LILY_PAD);

                for (int i = 0; i < 256; i++)
                    biomeStorage.setBiome(x >> 2, i, z >> 2, river);
            } else
                for (int i = 0; i < 256; i++)
                    biomeStorage.setBiome(x >> 2, i, z >> 2, jungle);
        }
        FrostedChunkGenerator.fillWithStone(x, height - 5, z, chunkData);
    }

    public void spreadPlants(ChunkGenerator.ChunkData chunkData, int x, int y, int z, Random random) {
        if (random.nextDouble() < 0.8) {
            if (random.nextDouble() < 0.75)
                chunkData.setBlock(x, y + 1, z, Material.GRASS);
            else {
                Bisected bisected = (Bisected) Material.LARGE_FERN.createBlockData();
                bisected.setHalf(Bisected.Half.TOP);
                Bisected bisected2 = (Bisected) Material.LARGE_FERN.createBlockData();
                bisected2.setHalf(Bisected.Half.BOTTOM);
                chunkData.setBlock(x, y + 1, z, bisected2);
                chunkData.setBlock(x, y + 2, z, bisected);
            }
        }
    }
}
