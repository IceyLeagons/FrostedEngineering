package net.iceyleagons.worldgenerator.generator.steps;

import net.iceyleagons.worldgenerator.biome.BiomeManager;
import net.iceyleagons.worldgenerator.generator.GeneratorStep;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class OreStep implements GeneratorStep {
    int rx, ry, seed;
    Random random;
    ChunkGenerator.ChunkData chunkData;
    ChunkGenerator.BiomeGrid biomeGrid;
    BiomeManager biomeManager;

    double diamond_min_height = 3;
    double diamond_max_height = 18;
    double diamond_probability = .0001;

    double iron_min_height = 3;
    double iron_max_height = 90;
    double iron_probability = .0015;

    double coal_min_height = 10;
    double coal_max_height = 150;
    double coal_probability = .003;

    double lapis_min_height = 6;
    double lapis_max_height = 25;
    double lapis_probability = .0006;

    double gold_min_height = 5;
    double gold_max_height = 50;
    double gold_probability = .000275;

    double redstone_min_height = 3;
    double redstone_max_height = 40;
    double redstone_probability = .0002;

    double emerald_min_height = 3;
    double emerald_max_height = 30;
    double emerald_probability = .00005;

    @Override
    public void populate(World world) {
        for (int x = 0; x < 16; ++x)
            for (int z = 0; z < 16; ++z)
                for (int y = 0; y < 255; ++y) {
                    if (y > coal_min_height && y < coal_max_height && random.nextDouble() < coal_probability) {
                        placeVein(x, y, z, chunkData, random.nextInt(3), Material.COAL_ORE);
                        continue;
                    }
                    if (y > iron_min_height && y < this.iron_max_height && random.nextDouble() < iron_probability) {
                        placeVein(x, y, z, chunkData, random.nextInt(3), Material.IRON_ORE);
                        continue;
                    }
                    if (y > gold_min_height && y < gold_max_height && random.nextDouble() < gold_probability) {
                        placeVein(x, y, z, chunkData, random.nextInt(3), Material.GOLD_ORE);
                        continue;
                    }
                    if (y > redstone_min_height && y < redstone_max_height && random.nextDouble() < redstone_probability) {
                        placeVein(x, y, z, chunkData, random.nextInt(3), Material.REDSTONE_ORE);
                        continue;
                    }
                    if (y > lapis_min_height && y < lapis_max_height && random.nextDouble() < lapis_probability) {
                        placeVein(x, y, z, chunkData, random.nextInt(3), Material.LAPIS_ORE);
                        continue;
                    }
                    if (y > diamond_min_height && y < diamond_max_height && random.nextDouble() < diamond_probability) {
                        placeVein(x, y, z, chunkData, random.nextInt(3), Material.DIAMOND_ORE);
                        continue;
                    }
                    if (!(y > emerald_min_height) || !(y < emerald_max_height) || !(random.nextDouble() < emerald_probability))
                        continue;
                    this.placeVein(x, y, z, chunkData, random.nextInt(3), Material.EMERALD_ORE);
                }
    }

    private void placeVein(int x, int y, int z, ChunkGenerator.ChunkData chunkData, int size, Material ore) {
        switch (size) {
            default:
            case 0:
                setIfStone(chunkData, x, y, z, ore);
                setIfStone(chunkData, x, y + 1, z, ore);
                break;
            case 1:
                setIfStone(chunkData, x, y, z, ore);
                setIfStone(chunkData, x, y + 1, z, ore);
                setIfStone(chunkData, x + 1, y, z, ore);
                setIfStone(chunkData, x + 1, y + 1, z, ore);
                break;
            case 2:
                setIfStone(chunkData, x, y, z, ore);
                setIfStone(chunkData, x, y + 1, z, ore);
                setIfStone(chunkData, x + 1, y, z, ore);
                setIfStone(chunkData, x + 1, y + 1, z, ore);
                setIfStone(chunkData, x, y, z + 1, ore);
                setIfStone(chunkData, x, y + 1, z + 1, ore);
                setIfStone(chunkData, x + 1, y, z + 1, ore);
                setIfStone(chunkData, x + 1, y + 1, z + 1, ore);
        }
    }

    private void setIfStone(ChunkGenerator.ChunkData chunkData, int x, int y, int z, Material material) {
        if (this.chunkData.getType(x, y, z) == Material.STONE)
            chunkData.setBlock(x, y, z, material);
    }

    @Override
    public GeneratorStep setX(int x) {
        this.rx = x;
        return this;
    }

    @Override
    public GeneratorStep setY(int y) {
        this.ry = y;
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
    public GeneratorStep setChunkData(ChunkGenerator.ChunkData chunkData) {
        this.chunkData = chunkData;
        return this;
    }
}
