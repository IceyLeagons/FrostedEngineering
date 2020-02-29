package net.iceyleagons.frostedengineering.generator.frosted;

import org.bukkit.Material;

import fastnoise.MathUtils;
import fastnoise.MathUtils.Erosion;
import fastnoise.MathUtils.FastNoise;
import fastnoise.MathUtils.FastNoise.Interp;
import fastnoise.MathUtils.FastNoise.NoiseType;
import net.iceyleagons.frostedengineering.generator.frosted.ChunkData.PointData.PointType;

// 2/26/2020 General optimizations
// 2/26/2020 Replaced reflections with direct calling (ModificationType)
public class ChunkData {
	int erosionIterations = 11500;
	static int mapSize = 16;
	static int terraceNumber = 11;
	static int vegetationNumber = 1; // add + 1 for max possible trees.
	static int vegetationOverflow = 2; // this is in blocks.
	final float cloudNoiseLimit = 0.75f;
	static double vegetationChance = .2D;
	int iceHeight = 16;
	int waterHeight = 6;
	int amplitudePower = 5;
	float multiplier = 22.5f;

	private MathUtils utils;
	private static FastNoise noiser;
	private static Erosion erosion;

	static int typeSize = PointData.PointType.values().length;

	public PointData[] pointMap;

	public long timeTookModifying = 0L;
	public long timeTookNoising = 0L;

	public ChunkData(PointData[] map, long seed) {
		setup(seed);
		this.pointMap = map;
	}

	public ChunkData(int baseX, int baseZ, long seed) {
		setup(seed);
		this.pointMap = createNoiseMap(baseX, baseZ);
		this.pointMap = modify(this.pointMap);
	}

	private void setup(long seed) {
		this.utils = MathUtils.getInstance(seed);
		ChunkData.noiser = utils.getNoiser();
		ChunkData.erosion = utils.getErosion() != null ? utils.getErosion()
				: utils.getErosion(mapSize, 1f, 1f, 128, 10f, .01f, .8f, .8f, 01f, 4f, .3f, 4, erosionIterations);

		ChunkData.noiser.setNoiseType(NoiseType.PERLIN);
		ChunkData.noiser.setInterp(Interp.QUINTIC);
	}

	public PointData[] createNoiseMap(int rx, int rz) {
		long timeNow = System.currentTimeMillis();
		PointData[] pointMap = new PointData[typeSize];

		for (int i = 0; i < typeSize; i++) {
			float[][] heightMap = new float[mapSize][mapSize];

			PointType type = PointData.PointType.values()[i];

			for (int x = 0; x < mapSize; x++)
				for (int z = 0; z < mapSize; z++) {
					heightMap[x][z] = noise(rx + x, rz + z, Technique.REGULAR, type.amplitude);
				}
			pointMap[i] = new PointData(heightMap, type);
		}

		this.timeTookNoising += System.currentTimeMillis() - timeNow;

		return pointMap;
	}

	public PointData[] modify(PointData[] inputData) {
		long timeNow = System.currentTimeMillis();

		for (int i = 0; i < inputData.length; i++) {
			float[][] input = inputData[i].height;

			for (ModificationType mod : inputData[i].pointType.modificationTypes) {
				input = mod.method.run(input);
			}

			inputData[i].height = input;
		}

		this.timeTookModifying += System.currentTimeMillis() - timeNow;

		return inputData;
	}

	public float noise(float x, float y, Technique mode, float amplitude) {
		float noiseValue;

		if (mode == Technique.REGULAR) {
			noiseValue = (1.f * noiseValue(1 * x, 1 * y, Technique.REGULAR)
					+ .5f * noiseValue(2 * x, 2 * y, Technique.REGULAR)
					+ .25f * noiseValue(4 * x, 4 * y, Technique.REGULAR)
					+ .13f * noiseValue(8 * x, 8 * y, Technique.REGULAR)
					+ .06f * noiseValue(16 * x, 16 * y, Technique.REGULAR)
					+ .03f * noiseValue(32 * x, 32 * y, Technique.REGULAR));
			noiseValue /= (1.f + .50f + .25f + .13f + .06f + .03f);
		} else {
			float e1 = 1f * noise(1 * x, 1 * y, Technique.RIDGED, amplitude);
			float e2 = 0.5f * noise(2 * x, 2 * y, Technique.RIDGED, amplitude) * e1;
			float e3 = 0.25f * noise(4 * x, 4 * y, Technique.RIDGED, amplitude) * (e1 + e2);

			noiseValue = e1 + e2 + e3;
		}
		noiseValue = MathUtils.fastPow(noiseValue, amplitudePower);

		return noiseValue * amplitude;
	}

	private float noiseValue(float x, float y, Technique mode) {
		switch (mode) {
		case RIDGED: {
			return (float) (2 * (0.5 - Math.abs(0.5 - noiseValue(x, y, Technique.REGULAR))));
		}
		case REGULAR:
		default: {
			return noiser.getNoise(x, y) / 2f + 0.5f;
		}
		}
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
			TERRAIN(50, 15.f, 22.5f, 0.f, BlockChoice.TERRAIN, true, ModificationType.EROSION),
			CLOUD(150, 6.f, 17.5f, .25f, BlockChoice.CLOUD, false);

			public final int offset;
			public final float amplitude;
			public final float multiplier;
			public final float threshold;
			public final BlockChoice blocks;
			public final boolean doWater;
			public final ModificationType[] modificationTypes;

			private PointType(int offset, float amplitude, float multiplier, float threshold, BlockChoice blocks,
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

			private BlockChoice(Material surfaceBlock, Material iceBlock, Material... stoneBlocks) {
				this.surfaceBlock = surfaceBlock;
				this.stoneBlock = stoneBlocks;
				this.heightDependentBlock = iceBlock;
			}
		}
	}

	public enum ModificationType {
		EROSION("Eroding", new ModificationRunnable() {

			@Override
			public float[][] run(float[][] input) {
				return erosion.erode(input);
			}

		}), TERRACE("Terracing", new ModificationRunnable() {

			@Override
			public float[][] run(float[][] input) {
				for (int i = 0; i < typeSize; i++) {

					for (int x = 0; x < mapSize; x++) {
						for (int z = 0; z < mapSize; z++) {
							input[x][z] = MathUtils.fastRound(input[x][z] * terraceNumber) / terraceNumber;
						}
					}
				}

				return input;
			}

		});

		public final ModificationRunnable method;
		public final String name;

		private ModificationType(String name, ModificationRunnable runnable) {
			this.name = name;
			this.method = runnable;
		}
	}

	public enum Technique {
		RIDGED, REGULAR;
	}
}