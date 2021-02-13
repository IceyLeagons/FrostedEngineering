package net.iceyleagons.worldgenerator.generator;

import lombok.Getter;
import net.iceyleagons.worldgenerator.biome.BiomeManager;
import net.iceyleagons.worldgenerator.generator.populators.TreePopulator;
import net.iceyleagons.worldgenerator.generator.steps.CaveStep;
import net.iceyleagons.worldgenerator.generator.steps.OreStep;
import net.iceyleagons.worldgenerator.generator.steps.TerrainStep;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FrostedChunkGenerator extends ChunkGenerator {

    private int seed;
    @Getter
    private BiomeManager biomeManager;
    private GeneratorStep[] generators = new GeneratorStep[]{new TerrainStep(), new CaveStep(), new OreStep()};
    @Getter
    ArrayList<BlockPopulator> populatorList = new ArrayList<>();

    private void setup(World world) {
        this.seed = (int) (world.getSeed() / 2);
        this.biomeManager = new BiomeManager(world);
    }

    @Override
    public ChunkData generateChunkData(World world, Random random, int x, int y, BiomeGrid biomeGrid) {
        ChunkData chunkData = this.createChunkData(world);

        for (GeneratorStep generator : generators)
            generator.setX(x).setY(y).setSeed(seed).setBiomeManager(biomeManager)
                    .setRandom(random).setBiomeGrid(biomeGrid).setChunkData(chunkData).populate(world);

        for (int X = 0; X < 16; ++X)
            for (int z = 0; z < 16; ++z) {
                chunkData.setBlock(X, 0, z, Material.BEDROCK);
                chunkData.setBlock(X, 1, z, Material.STONE);
                chunkData.setBlock(X, 2, z, Material.STONE);
            }

        return chunkData;
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        int x = random.nextInt(200) - 100;
        int z = random.nextInt(200) - 100;
        int y = world.getHighestBlockYAt(x, z) + 1;
        return new Location(world, x, y, z);
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        this.setup(world);
        populatorList.add(new TreePopulator(seed, 1, biomeManager));
        return populatorList;
    }

    @Override
    public boolean shouldGenerateMobs() {
        return true;
    }

    @Override
    public boolean isParallelCapable() {
        return true;
    }

    @Override
    public boolean shouldGenerateCaves() {
        return true;
    }

    @Override
    public boolean shouldGenerateDecorations() {
        return false;
    }

    @Override
    public boolean shouldGenerateStructures() {
        return false;
    }

    public static int fillWithWater(int x, int y, int z, ChunkData chunkData) {
        for (int i = y; i > 0; --i)
            if (chunkData.getType(x, i, z) != Material.AIR)
                return i;
            else if (chunkData.getType(x + 1, i, z) == Material.LAVA)
                chunkData.setBlock(x, i, z, Material.OBSIDIAN);
            else if (chunkData.getType(x - 1, i, z) == Material.LAVA)
                chunkData.setBlock(x, i, z, Material.OBSIDIAN);
            else if (chunkData.getType(x, i, z + 1) == Material.LAVA)
                chunkData.setBlock(x, i, z, Material.OBSIDIAN);
            else if (chunkData.getType(x, i, z - 1) == Material.LAVA)
                chunkData.setBlock(x, i, z, Material.OBSIDIAN);
            else
                chunkData.setBlock(x, i, z, Material.WATER);

        return 1;
    }

    public static int getRiverDepth(int x, int y, int seed) {
        double noiseValue = 1.0 - Math.abs(((PerlinNoise.getNoise((x / 33.0 * 0.15)
                + seed, (y / 33.0 * 0.15) + seed, 4.0))
                + PerlinNoise.getNoise(x / 33.0 * 0.45 + seed, y / 33.0 * 0.45 + seed, 4.0) * 0.2) / 1.2);
        int riverDepth = (int) (noiseValue * 255.0);
        if (riverDepth > 250)
            return -247 + riverDepth;

        return 0;
    }

    public static boolean canCave(int x, int y, int z, int seed) {
        if (y > 120)
            return true;

        for (int i = 0; i < 4; ++i) {
            if (!(PerlinNoise.getNoise((double) (x + seed), y - i, (double) (z + seed), 1.0f) > 0.7)) continue;
            return false;
        }

        return true;
    }

    public static void fillWithStone(int x, int y, int z, ChunkData chunkData) {
        for (int i = y; i > 0; --i)
            chunkData.setBlock(x, i, z, Material.STONE);
    }
}
