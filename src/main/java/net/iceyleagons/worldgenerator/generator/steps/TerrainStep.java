package net.iceyleagons.worldgenerator.generator.steps;

import net.iceyleagons.icicle.wrapped.biome.WrappedBiomeStorage;
import net.iceyleagons.worldgenerator.biome.BiomeManager;
import net.iceyleagons.worldgenerator.biome.BiomeReflections;
import net.iceyleagons.worldgenerator.generator.GeneratorStep;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.ChunkGenerator.ChunkData;

import java.util.Random;

public class TerrainStep implements GeneratorStep {

    private int x, y, seed;
    private BiomeManager biomeManager;
    private Random random;
    private ChunkGenerator.BiomeGrid biomeGrid;
    private ChunkData chunkData;

    @Override
    public void populate(World world) {
        WrappedBiomeStorage biomeStorage = BiomeReflections.getBiomeStorage(biomeGrid);

        for (int i = 0; i < 16; ++i)
            for (int j = 0; j < 16; ++j) {
                int rX = i + 16 * x;
                int rZ = j + 16 * y;
                biomeManager.getBiome(rX, rZ, random).populateChunkGenerator(biomeManager, chunkData, rX, rZ, i, j, random, biomeStorage);
            }
    }

    @Override
    public GeneratorStep setX(int x) {
        this.x = x;
        return this;
    }

    @Override
    public GeneratorStep setY(int y) {
        this.y = y;
        return this;
    }

    @Override
    public GeneratorStep setSeed(int seed) {
        this.seed = seed;
        return this;
    }

    @Override
    public GeneratorStep setBiomeManager(BiomeManager biomeManager) {
        this.biomeManager = biomeManager;
        return this;
    }

    @Override
    public GeneratorStep setRandom(Random random) {
        this.random = random;
        return this;
    }

    @Override
    public GeneratorStep setBiomeGrid(ChunkGenerator.BiomeGrid biomeGrid) {
        this.biomeGrid = biomeGrid;
        return this;
    }

    @Override
    public GeneratorStep setChunkData(ChunkData chunkData) {
        this.chunkData = chunkData;
        return this;
    }
}
