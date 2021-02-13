package net.iceyleagons.worldgenerator.generator;

import net.iceyleagons.worldgenerator.biome.BiomeManager;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public interface GeneratorStep {

    GeneratorStep setX(int x);

    GeneratorStep setY(int y);

    GeneratorStep setSeed(int seed);

    GeneratorStep setBiomeManager(BiomeManager biomeManager);

    GeneratorStep setRandom(Random random);

    GeneratorStep setBiomeGrid(ChunkGenerator.BiomeGrid biomeGrid);

    GeneratorStep setChunkData(ChunkGenerator.ChunkData chunkData);

    void populate(World world);

}
