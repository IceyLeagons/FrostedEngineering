package net.iceyleagons.worldgenerator.biome.impl;

import net.iceyleagons.icicle.wrapped.biome.WrappedBiomeBase;
import net.iceyleagons.icicle.wrapped.biome.WrappedBiomeStorage;
import net.iceyleagons.worldgenerator.WorldGenerator;
import net.iceyleagons.worldgenerator.biome.Biome;
import net.iceyleagons.worldgenerator.biome.BiomeManager;
import net.iceyleagons.worldgenerator.generator.FrostedChunkGenerator;
import net.iceyleagons.worldgenerator.generator.PerlinNoise;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class Island extends Biome implements ICommons {
    WrappedBiomeBase river = BiomeManager.biomeBaseMap.get("fe:river");
    WrappedBiomeBase island = BiomeManager.biomeBaseMap.get("fe:island");
WrappedBiomeBase mountain = BiomeManager.biomeBaseMap.get("fe:mountain");

    BlockData[] datas = new BlockData[]{Material.SAND.createBlockData(), Material.SANDSTONE.createBlockData()};

    public Island(int seed, double centerX, double centerY) {
        super(seed, centerX, centerY);
    }

    @Override
    public String getBiomeName() {
        return "island";
    }

    @Override
    public double getHeight(int n, int n2) {
        return (int) Math.round((PerlinNoise.getNoise(n + seed, n2 + seed, 4, 0.5f) * 0.15) * 255.0) + 70;
    }

    @Override
    public void populateChunkGenerator(BiomeManager manager, ChunkGenerator.ChunkData chunkData, int chunkX, int chunkY, int x, int z, Random random, WrappedBiomeStorage biomeStorage) {
        int height = manager.getHeight(chunkX, chunkY);
        if (height < 94) {
            fillWithEither(random, chunkData, x, height, z, datas);
            chunkData.setBlock(x, height - 1, z, Material.SAND);
            chunkData.setBlock(x, height - 2, z, Material.SAND);
            chunkData.setBlock(x, height - 3, z, Material.SAND);
            chunkData.setBlock(x, height - 4, z, Material.STONE);
            FrostedChunkGenerator.fillWithWater(x, 94, z, chunkData);

            for (int i = 0; i < 256; i++)
                biomeStorage.setBiome(x >> 2, i >> 2, z >> 2, river);
        } else if (height < 98) {
            int riverDepth = FrostedChunkGenerator.getRiverDepth(chunkX, chunkY, seed);
            if (random.nextDouble() > 0.5) {
                chunkData.setBlock(x, height, z, Material.GRASS_BLOCK);
                grassOrLeaves(chunkData, random, riverDepth, x, z, chunkX, chunkY, height + 1, 0.4);
            } else
                fillWithEither(random, chunkData, x, height, z, datas);

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
                    biomeStorage.setBiome(x >> 2, i >> 2, z >> 2, island);
        } else {
            chunkData.setBlock(x, height, z, Material.GRASS_BLOCK);
            chunkData.setBlock(x, height - 1, z, Material.DIRT);
            chunkData.setBlock(x, height - 2, z, Material.SAND);
            chunkData.setBlock(x, height - 3, z, Material.SAND);
            chunkData.setBlock(x, height - 4, z, Material.STONE);
            int riverDepth = FrostedChunkGenerator.getRiverDepth(chunkX, chunkY, seed);
            grassOrLeaves(chunkData, random, riverDepth, x, z, chunkX, chunkY, height + 1, 0.3);
            if (riverDepth != 0) {
                for (int i = height; i > height - riverDepth && i >= 94; --i)
                    chunkData.setBlock(x, i, z, Material.AIR);

                FrostedChunkGenerator.fillWithWater(x, Math.max(height - 2, 94), z, chunkData);
                for (int i = 0; i < 256; i++)
                    biomeStorage.setBiome(x >> 2, i >> 2, z >> 2, river);
            } else if (height > 225)
                for (int i = 0; i < 256; i++)
                    biomeStorage.setBiome(x >> 2, i >> 2, z >> 2, mountain);
            else
                for (int i = 0; i < 256; i++)
                    biomeStorage.setBiome(x >> 2, i >> 2, z >> 2, island);
        }

        FrostedChunkGenerator.fillWithStone(x, height - 5, z, chunkData);
    }

    private void grassOrLeaves(ChunkGenerator.ChunkData chunkData, Random random, int riverDepth, int x, int z, int chunkX, int chunkY, int height, double minimum) {
        if (riverDepth == 0 && random.nextDouble() < minimum && FrostedChunkGenerator.canCave(chunkX, height + 1, chunkY, this.seed))
            if (random.nextDouble() < 0.8) {
                chunkData.setBlock(x, height, z, Material.GRASS);
            } else
                chunkData.setBlock(x, height, z, WorldGenerator.permanentLeaves);
    }
}
