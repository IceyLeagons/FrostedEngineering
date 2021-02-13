package net.iceyleagons.worldgenerator.generator.steps;

import net.iceyleagons.worldgenerator.WorldGenerator;
import net.iceyleagons.worldgenerator.biome.BiomeManager;
import net.iceyleagons.worldgenerator.generator.FrostedChunkGenerator;
import net.iceyleagons.worldgenerator.generator.GeneratorStep;
import net.iceyleagons.worldgenerator.generator.PerlinNoise;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.ChunkGenerator.ChunkData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CaveStep implements GeneratorStep {
    int rx, ry, seed;
    Random random;
    ChunkData chunkData;
    ChunkGenerator.BiomeGrid biomeGrid;
    BiomeManager biomeManager;

    Cave cave = new Cave();

    List<Material> blackList = new ArrayList<Material>() {{
        add(Material.WATER);
        add(Material.OAK_LEAVES);
        add(Material.OAK_LOG);
    }};

    @Override
    public void populate(World world) {
        for (int x = 0; x < 16; x++)
            for (int z = 0; z < 16; z++) {
                int rX = x + 16 * rx + seed;
                int rZ = z + 16 * ry + seed;

                for (int y = 0; y < 120; y++) {
                    if (!(PerlinNoise.getNoise(rX, y, (double) rZ, 1.0f) > 0.7) || !FrostedChunkGenerator.canCave(x, y, z, seed))
                        continue;

                    if (!blackList.contains(chunkData.getType(x, y, z)) && !blackList.contains(chunkData.getType(x, y + 1, z))
                            && !blackList.contains(chunkData.getType(x, y + 2, z)) && !blackList.contains(chunkData.getType(x, y + 3, z))
                            && !blackList.contains(chunkData.getType(x + 1, y, z)) && !blackList.contains(chunkData.getType(x - 1, y, z))
                            && !blackList.contains(chunkData.getType(x, y, z + 1)) && !blackList.contains(chunkData.getType(x, y, z - 1)))
                        chunkData.setBlock(x, y, z, Material.AIR);

                    if (this.isBelowCaveCeiling(rX, y, rZ)) {
                        cave.carve(chunkData, x, y, z, random);
                        continue;
                    }

                    if (!this.isAboveCaveFloor(rX, y, rZ)) continue;

                    cave.decorate(chunkData, x, y, z, random);
                }
            }
    }

    private boolean isAboveCaveFloor(int x, int y, int z) {
        return PerlinNoise.getNoise(x, y - 1, (double) z, 1.0f) <= 0.7;
    }

    private boolean isBelowCaveCeiling(int x, int y, int z) {
        return PerlinNoise.getNoise(x, y + 1, (double) z, 1.0f) <= 0.7;
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
    public GeneratorStep setChunkData(ChunkData chunkData) {
        this.chunkData = chunkData;
        return this;
    }

    public class Cave {
        public void carve(ChunkData chunkData, int x, int y, int z, Random random) {
            double randomDouble = random.nextDouble();
            if (randomDouble < 0.5D) {
                if (this.isStone(chunkData, y + 1, z, x)) {
                    chunkData.setBlock(x, y, z, Material.COBBLESTONE);
                    this.addHangingLeaves(chunkData, x, y, z, random);
                }
            } else if (this.isStone(chunkData, y + 1, z, x))
                chunkData.setBlock(x, y, z, Material.STONE);
        }

        public void decorate(ChunkData chunkData, int x, int y, int z, Random random) {
            if (this.isStone(chunkData, y - 1, z, x)) {
                if (random.nextDouble() < 0.45D)
                    chunkData.setBlock(x, y - 1, z, Material.COBBLESTONE);
                else
                    chunkData.setBlock(x, y - 1, z, Material.STONE);

                this.randomLeaves(chunkData, x, y, z, random);
            }
        }

        private void addHangingLeaves(ChunkData chunkData, int x, int y, int z, Random random) {
            if (random.nextDouble() < 0.05D)
                for (int i = 1; i < random.nextInt(5); i++)
                    chunkData.setBlock(x, y - i, z, WorldGenerator.permanentLeaves);
        }

        private void randomLeaves(ChunkData chunkData, int x, int y, int z, Random random) {
            if (random.nextDouble() < 0.1D)
                chunkData.setBlock(x, y, z, WorldGenerator.permanentLeaves);
        }

        private boolean isStone(ChunkData chunkData, int x, int y, int z) {
            return chunkData.getType(x, y, z) == Material.STONE;
        }
    }
}
