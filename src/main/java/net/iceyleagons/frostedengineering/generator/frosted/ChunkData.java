/*
 *  Copyright (C) IceyLeagons(https://iceyleagons.net/)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package net.iceyleagons.frostedengineering.generator.frosted;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Material;

import fastnoise.MathUtils;
import fastnoise.MathUtils.Erosion;
import fastnoise.MathUtils.FastNoise;
import net.iceyleagons.frostedengineering.generator.frosted.ChunkData.PointData.PointType;

// 7/30/2020 Added masks.
// 2/26/2020 General optimizations
// 2/26/2020 Replaced reflections with direct calling (ModificationType)
public class ChunkData {
    // Options
    static int erosionIterations = 2048;
    static int mapSize = 16;
    static int vegetationNumber = 1; // add + 1 for max possible trees.
    static int vegetationOverflow = 2; // this is in blocks.
    static int amplitudePower = 1;
    static int maskLength = 287;
    // static double vegetationChance = .2D;
    final boolean caves;
    final float cloudNoiseLimit = 0.75f;
    int iceHeight = 16;
    int waterHeight = 6;

    static float caveDecidingFactor = .5f;
    float secondaryNoiseMultiplier = .25f;

    static Map.Entry<FastNoise, Optional<FastNoise>> noise;
    static Map.Entry<FastNoise, Optional<FastNoise>> caveNoise;
    static Erosion erosion;
    static MathUtils utils = MathUtils.getInstance(1337);

    static {
        // Terrain noise
        ChunkData.noise = utils.getNoise(MathUtils.NoiseClass.TERRAIN);

        // Chunk noise
        ChunkData.caveNoise = utils.getNoise(MathUtils.NoiseClass.CAVE);

        ChunkData.erosion = utils.getErosion() != null ? utils.getErosion()
                : utils.getErosion(mapSize, 1f, 1f, 128, 10f, .01f, .8f, .8f, 01f, 4f, .3f, 4, erosionIterations);
    }

    static int typeSize = PointData.PointType.values().length;

    PointData[] pointMap;

    public long timeTookModifying = 0L;
    public long timeTookNoising = 0L;

    int x, z;

    public ChunkData(PointData[] map, long seed) {
        this.caves = false;
        utils.updateSeed(seed);
        this.pointMap = map;
    }

    public ChunkData(boolean caves, PointData[] map, long seed) {
        this.caves = caves;
        utils.updateSeed(seed);
        this.pointMap = map;
    }

    public ChunkData(int baseX, int baseZ, long seed) {
        this.caves = false;
        utils.updateSeed(seed);
        this.x = baseX;
        this.z = baseZ;
    }

    public ChunkData(boolean caves, int baseX, int baseZ, long seed) {
        this.caves = caves;
        utils.updateSeed(seed);
        this.x = baseX;
        this.z = baseZ;
    }

    public PointData[] generate() {
        pointMap = createNoiseMap(x, z);
        return modify(pointMap).join();
    }

    public PointData[] createNoiseMap(int rx, int rz) {
        long timeNow = System.currentTimeMillis();
        PointData[] pointMap = new PointData[typeSize];

        for (int i = 0; i < typeSize; i++) {
            float[][] heightMap = new float[mapSize][mapSize];

            PointType type = PointData.PointType.values()[i];

            for (int x = 0; x < mapSize; x++)
                for (int z = 0; z < mapSize; z++)
                    heightMap[x][z] = noise(rx + x, rz + z, type.amplitude);

            pointMap[i] = new PointData(heightMap, type);
        }

        this.timeTookNoising += System.currentTimeMillis() - timeNow;

        return pointMap;
    }

    public static List<CaveInstruction> generateCaveSystem(int size) {
        List<CaveInstruction> instructions = new ArrayList<>(size);
        int currentSize = size;

        for (int i = 0; i < size; i++) {
            CaveInstruction instruction = new CaveInstruction(false, false, false, false,
                    (currentSize -= (ThreadLocalRandom.current().nextBoolean() ? 1 : 0)));
            if (noise(caveNoise.getKey(), i, i) > caveDecidingFactor)
                instruction.setUp(true);
            else
                instruction.setDown(true);

            if (noise(caveNoise.getValue().orElse(caveNoise.getKey()), i, i) > caveDecidingFactor)
                instruction.setLeft(true);
            else
                instruction.setRight(true);

            instructions.add(instruction);
        }

        return instructions;
    }

    @Deprecated
    public float noise(float x, float y, float amplitude) {
        float noiseValue = (1.f * noise(noise, 1 * x, 1 * y)
                + .5f * noise(noise, 2 * x, 2 * y)
                + .25f * noise(noise, 4 * x, 4 * y)
                + .13f * noise(noise, 8 * x, 8 * y)
                + .06f * noise(noise, 16 * x, 16 * y)
                + .03f * noise(noise, 32 * x, 32 * y));
        noiseValue /= (1.f + .50f + .25f + .13f + .06f + .03f);
        noiseValue = (float) Math.pow(noiseValue, amplitudePower);

        return noiseValue * amplitude;
    }

    public CompletableFuture<PointData[]> modify(PointData[] inputData) {
        long timeNow = System.currentTimeMillis();

        return CompletableFuture.supplyAsync(() -> {
            for (PointData inputDatum : inputData) {
                float[][] input = inputDatum.height;

                for (ModificationType mod : inputDatum.pointType.modificationTypes)
                    input = mod.method.run(input);
                inputDatum.height = input;
            }

            timeTookModifying += System.currentTimeMillis() - timeNow;

            return inputData;
        });
    }

    public float noise(Map.Entry<FastNoise, Optional<FastNoise>> noise, float x, float y, float amplitude) {
        float noiseValue = (float) Math.pow(noise(noise, x, y), amplitudePower);

        return noiseValue * amplitude;
    }

    public float noise(Map.Entry<FastNoise, Optional<FastNoise>> noise, float x, float y) {
        FastNoise primaryNoise = noise.getKey();
        float noiseValue;

        if (noise.getValue().isPresent())
            noiseValue = primaryNoise.getNoise(x, y) + noise.getValue().orElse(noise.getKey()).getNoise(x, y) * secondaryNoiseMultiplier;
        else
            noiseValue = primaryNoise.getNoise(x, y);

        return noiseValue;
    }

    public static float noise(FastNoise noise, float x, float y) {
        return noise.getNoise(x, y);
    }

    @Data
    @AllArgsConstructor(staticName = "of")
    public static class CaveInstruction {
        boolean up, left, down, right;
        int size;
    }

    public static class PointData {
        public float[][] height;
        public PointType pointType;

        public PointData(float[][] heightMap, PointType type) {
            this.height = heightMap;
            this.pointType = type;
        }

        public enum PointType {
            ISLAND(165, 7.5f, 9.f, .4f, BlockChoice.ISLAND, false),
            TERRAIN(50, 15.f, 22.5f, 0.f, BlockChoice.TERRAIN, true,
                    ModificationType.EROSION),
            CLOUD(150, 6.f, 17.5f, .25f, BlockChoice.CLOUD, false);

            public final int offset;
            public final float amplitude;
            public final float multiplier;
            public final float threshold;
            public final BlockChoice blocks;
            public final boolean doWater;
            public final ModificationType[] modificationTypes;

            PointType(int offset, float amplitude, float multiplier, float threshold, BlockChoice blocks,
                      boolean doWater, ModificationType... mods) {
                this.offset = offset;
                this.amplitude = amplitude;
                this.multiplier = multiplier;
                this.threshold = threshold;
                this.modificationTypes = mods;
                this.blocks = blocks;
                this.doWater = doWater;
            }
        }

        public enum BlockChoice {
            TERRAIN(Material.SNOW_BLOCK, Material.ICE, Material.STONE, Material.COBBLESTONE, Material.ANDESITE),
            ISLAND(Material.GRASS_BLOCK, null, Material.STONE, Material.COBBLESTONE, Material.ANDESITE),
            CLOUD(null, null, Material.CYAN_STAINED_GLASS, Material.CYAN_STAINED_GLASS_PANE,
                    Material.WHITE_STAINED_GLASS, Material.WHITE_STAINED_GLASS_PANE);

            public final Material surfaceBlock;
            public final Material heightDependentBlock;
            public final Material[] stoneBlock;

            BlockChoice(Material surfaceBlock, Material iceBlock, Material... stoneBlocks) {
                this.surfaceBlock = surfaceBlock;
                this.stoneBlock = stoneBlocks;
                this.heightDependentBlock = iceBlock;
            }
        }
    }

    public enum ModificationType {
        EROSION("Eroding", input -> erosion.erode(input)),
        ISLAND_MASK("Masking", input -> {
            for (int x = 0; x < mapSize; x++)
                for (int z = 0; z < mapSize; z++)
                    input[x][z] = (float) -Math.sin(input[x][z] / maskLength) * amplitudePower;
            return input;
        }),
        TERRAIN_MASK("Masking", input -> {
            for (int x = 0; x < mapSize; x++)
                for (int z = 0; z < mapSize; z++)
                    input[x][z] = (float) Math.sin(input[x][z] / maskLength) * amplitudePower;
            return input;
        });

        public final ModificationRunnable method;
        public final String name;

        ModificationType(String name, ModificationRunnable runnable) {
            this.name = name;
            this.method = runnable;
        }
    }
}
