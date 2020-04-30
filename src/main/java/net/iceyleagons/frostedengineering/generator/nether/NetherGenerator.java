/*******************************************************************************
 * Copyright (C) IceyLeagons(https://iceyleagons.net/) 
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iceyleagons.frostedengineering.generator.nether;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.PerlinNoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import net.iceyleagons.frostedengineering.generator.nether.WorldManager;

public class NetherGenerator extends ChunkGenerator {
    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        @SuppressWarnings("unused")
        ArrayList<BlockPopulator> arrayList = new ArrayList<BlockPopulator>();
        arrayList.add(new GlowstonePopulator());
        arrayList.add(new LavaPopulator());
        arrayList.add(new StructuresPopulator());
        // arrayList.add(new TreePopulator());
        return arrayList;
    }

    @Override
    public ChunkGenerator.ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ,
                                                      ChunkGenerator.BiomeGrid biomeGrid) {
        int z;
        double generatedNoise3;
        double heightNoise;
        double noiseY;
        double generatedNoise4;
        int x;
        int offsetChunkX;
        int offsetChunkZ;
        double generatedNoise5;
        double generatedNoise1;
        double generatedNoise2;
        double generatedNoise6;
        ChunkGenerator.ChunkData chunkData = this.createChunkData(world);
        long seed1 = world.getSeed();
        long offsetSeed1 = world.getSeed() + 2L;
        long offsetSeed2 = world.getSeed() + 15L;
        long offsetSeed3 = world.getSeed() * 2L;
        long offsetSeed4 = world.getSeed() * 3L;
        long offsetSeed5 = world.getSeed() * 4L;
        long max = Long.MAX_VALUE;
        long min = Long.MIN_VALUE;
        if (seed1 <= min || seed1 >= max) {
            seed1 = 0L;
        }
        if (offsetSeed1 <= min || offsetSeed1 >= max) {
            offsetSeed1 = 0L;
        }
        if (offsetSeed2 <= min || offsetSeed2 >= max) {
            offsetSeed2 = 0L;
        }
        if (offsetSeed3 <= min || offsetSeed3 >= max) {
            offsetSeed3 = 0L;
        }
        if (offsetSeed4 <= min || offsetSeed4 >= max) {
            offsetSeed4 = 0L;
        }
        if (offsetSeed5 <= min || offsetSeed5 >= max) {
            offsetSeed5 = 0L;
        }
        Random random2 = new Random(seed1);
        Random random3 = new Random(offsetSeed1);
        Random random4 = new Random(offsetSeed2);
        Random random5 = new Random(offsetSeed3);
        Random random6 = new Random(offsetSeed4);
        Random random7 = new Random(offsetSeed5);
        SimplexNoiseGenerator noise1 = new SimplexNoiseGenerator(random2);
        PerlinNoiseGenerator noise2 = new PerlinNoiseGenerator(random3);
        SimplexNoiseGenerator noise3 = new SimplexNoiseGenerator(random4);
        PerlinNoiseGenerator noise4 = new PerlinNoiseGenerator(random5);
        SimplexNoiseGenerator noise5 = new SimplexNoiseGenerator(random6);
        SimplexNoiseGenerator noise6 = new SimplexNoiseGenerator(random7);
        for (x = 0; x < 16; ++x) {
            for (z = 0; z < 16; ++z) {
                biomeGrid.setBiome(x, z, Biome.NETHER);
                offsetChunkX = chunkX * 16 + x;
                offsetChunkZ = chunkZ * 16 + z;
                generatedNoise1 = noise1.noise(0.01 * offsetChunkX, 0.01 * offsetChunkZ);
                generatedNoise2 = noise2.noise(0.1 * offsetChunkX, 0.1 * offsetChunkZ);
                generatedNoise3 = noise3.noise(0.001 * offsetChunkX, 0.001 * offsetChunkZ);
                generatedNoise4 = noise4.noise(0.05 * offsetChunkX, 0.05 * offsetChunkZ);
                generatedNoise5 = noise5.noise(0.5 * offsetChunkX, 0.5 * offsetChunkZ);
                generatedNoise6 = noise6.noise(0.03 * offsetChunkX, 0.03 * offsetChunkZ);
                noiseY = heightNoise = 24.0 + generatedNoise1 * 15.0 + generatedNoise2 * 5.0 + generatedNoise3 * 25.0
                        + generatedNoise4 * 20.0 + generatedNoise5 * 3.0 + generatedNoise6 * 17.0;
                double d9 = heightNoise;
                if (heightNoise < 18.0) {
                    d9 = heightNoise = 18.0;
                } else {
                    heightNoise -= 2.0;
                }
                int y = 0;
                while (y < heightNoise) {
                    int randomNumber;
                    if (y <= 1) {
                        chunkData.setBlock(x, y, z, Material.BEDROCK);
                    } else if (y <= 23 && y > noiseY) {
                        randomNumber = random.nextInt(4);
                        chunkData.setBlock(x, y, z, randomNumber >= 2 ? Material.MAGMA_BLOCK : Material.LAVA);
                        //System.out.println(noiseY);
                    } else if (y > d9 && y <= heightNoise) {
                        randomNumber = random.nextInt(100);
                        if (randomNumber <= 3) {
                            chunkData.setBlock(x, y, z, Material.FIRE);
                        }
                    } else if (y >= 43) {
                        @SuppressWarnings("unused")
                        long a = 0;
                        if ((a = (random.nextInt(15 - 5) + 5) + Math.round(1.5F * Math.abs(43.0D - y))) <= 12)
                            chunkData.setBlock(x, y, z, Material.NETHERRACK);
                        else
                            chunkData.setBlock(x, y, z, Material.SOUL_SAND);
                    } else {
                        randomNumber = random.nextInt(250);
                        if (randomNumber < 2) {
                            NetherGenerator.generateWithChance(Material.NETHER_QUARTZ_ORE, x, y, z, random, chunkData,
                                    6, 2);
                        } else if (randomNumber < 15) {
                            NetherGenerator.generateWithChance(Material.SOUL_SAND, x, y, z, random, chunkData, 16, 8);
                        } else {
                            if (y >= 23) {
                                @SuppressWarnings("unused")
                                long a = 0;
                                if ((a = (random.nextInt(15 - 5) + 5) + Math.round(1.5F * Math.abs(19.0D - y))) <= 12)
                                    chunkData.setBlock(x, y, z, Material.NETHERRACK);
                                else
                                    chunkData.setBlock(x, y, z, randomMaterial(random));
                            } else {
                                randomNumber = random.nextInt(10);
                                if (randomNumber <= 2)
                                    chunkData.setBlock(x, y, z, Material.NETHER_WART_BLOCK);
                                else
                                    chunkData.setBlock(x, y, z, Material.NETHERRACK);
                            }
                        }
                    }
                    ++y;
                }

                if (chunkData.getBlockData(x, 17, z).getMaterial() == Material.AIR) {
                    chunkData.setBlock(x, 17, z, Material.LAVA);
                }
            }
        }
        if (WorldManager.roof) {
            for (x = 0; x < 16; ++x) {
                for (z = 0; z < 16; ++z) {
                    offsetChunkX = chunkX * 16 + x;
                    offsetChunkZ = chunkZ * 16 + z;
                    generatedNoise1 = noise1.noise(0.01 * offsetChunkX, 0.01 * offsetChunkZ);
                    generatedNoise2 = noise2.noise(0.1 * offsetChunkX, 0.1 * offsetChunkZ);
                    generatedNoise3 = noise3.noise(0.001 * offsetChunkX, 0.001 * offsetChunkZ);
                    generatedNoise4 = noise4.noise(0.05 * offsetChunkX, 0.05 * offsetChunkZ);
                    generatedNoise5 = noise5.noise(0.5 * offsetChunkX, 0.5 * offsetChunkZ);
                    generatedNoise6 = noise6.noise(0.03 * offsetChunkX, 0.03 * offsetChunkZ);
                    heightNoise = 80.0 + generatedNoise1 * 21.0 + generatedNoise2 * 6.0 + generatedNoise3 * 32.0
                            + generatedNoise4 * 27.0 + generatedNoise5 * 7.0 + generatedNoise6 * 35.0;
                    noiseY = 128;
                    while (noiseY > heightNoise) {
                        if (noiseY >= 128) {
                            chunkData.setBlock(x, (int) noiseY, z, Material.BEDROCK);
                        } else {
                            int n9 = random.nextInt(1000);
                            if (n9 < 5) {
                                NetherGenerator.generateWithChance(Material.NETHER_QUARTZ_ORE, x, (int) noiseY, z,
                                        random, chunkData, 6, 2);
                            } else if (n9 < 15) {
                                NetherGenerator.generateWithChance(Material.SOUL_SAND, x, (int) noiseY, z, random,
                                        chunkData, 16, 8);
                            } else {
                                if (noiseY < 60) {
                                    @SuppressWarnings("unused")
                                    long a = 0;
                                    if ((a = (random.nextInt(15 - 5) + 5) + Math.round(1.5F * (60.0D - noiseY))) <= 12)
                                        chunkData.setBlock(x, (int) noiseY, z, Material.NETHERRACK);
                                    else
                                        chunkData.setBlock(x, (int) noiseY, z, Material.OBSIDIAN);
                                    // System.out.println(a);
                                } else {
                                    chunkData.setBlock(x, (int) noiseY, z, Material.NETHERRACK);
                                }
                            }
                        }
                        --noiseY;
                    }

                    chunkData.setBlock(x, 128, z, Material.BEDROCK);
                }
            }
        }
        return chunkData;
    }

    private Material randomMaterial(Random random) {
        int randomNumber = random.nextInt(100);
        if (randomNumber <= 25) {
            return Material.PINK_TERRACOTTA;
        } else if (randomNumber <= 50) {
            return Material.RED_CONCRETE_POWDER;
        } else if (randomNumber <= 75) {
            return Material.RED_CONCRETE;
        } else {
            return Material.RED_TERRACOTTA;
        }
    }

    public static void generateWithChance(Material material, int x, int y, int z, Random random,
                                          ChunkGenerator.ChunkData chunkData, int max, int min) {
        int n6 = random.nextInt(max) + min;
        chunkData.setBlock(x, y, z, material);
        for (int i = 0; i < n6; ++i) {
            int randomNumber = random.nextInt(9);
            if (randomNumber == 0) {
                if (--x <= 0) {
                    ++x;
                }
            } else if (randomNumber == 1) {
                if (++x >= 16) {
                    --x;
                }
            } else if (randomNumber == 2) {
                if (--y <= 1) {
                    ++y;
                }
            } else if (randomNumber == 3) {
                if (++y >= 256) {
                    --y;
                }
            } else if (randomNumber == 4) {
                if (--z <= 0) {
                    ++z;
                }
            } else if (randomNumber == 5 && ++z >= 16) {
                --z;
            }
            if (chunkData.getType(x, y, z) == Material.AIR)
                continue;
            chunkData.setBlock(x, y, z, material);
        }
    }
}
