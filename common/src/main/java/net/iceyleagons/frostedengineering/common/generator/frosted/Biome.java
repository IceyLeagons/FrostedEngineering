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
package net.iceyleagons.frostedengineering.common.generator.frosted;

import java.util.HashMap;

/**
 * All credits go to this guy. He's awesome, check him out!
 *
 * @author Justin Michaud (https://github.com/justinmichaud)
 */
public enum Biome {

    TAIGA("Taiga", org.bukkit.block.Biome.SNOWY_TAIGA, .45f, VegetationType.TREES, .4D),
    HILLS("Hills", org.bukkit.block.Biome.ICE_SPIKES, .6f, VegetationType.TREES, .3D),
    MOUNTAINS("Mountains", org.bukkit.block.Biome.SNOWY_MOUNTAINS, .8f, VegetationType.BUSHES, .2D),
    TUNDRA("Tundra", org.bukkit.block.Biome.SNOWY_TUNDRA, .275f, VegetationType.BUSHES, .2D),
    WATER("Water", org.bukkit.block.Biome.FROZEN_OCEAN, .155f, VegetationType.TREES, 0.4D);

    public final org.bukkit.block.Biome biome;
    public final float heightFactor;
    public final String displayName;
    public final VegetationType vegetationType;
    public final double vegetationChance;

    private Biome(String displayName, org.bukkit.block.Biome biome, float value, VegetationType vType, double vChance) {
        this.biome = biome;
        this.heightFactor = value;
        this.displayName = displayName;
        this.vegetationType = vType;
        this.vegetationChance = vChance;
    }

    public static Biome fromBiome(org.bukkit.block.Biome biome) {
        for (Biome biom : Biome.values()) {
            if (biom.biome == biome)
                return biom;
        }

        return null;
    }

    public static Biome getDominantBiome(float height) {
        float maxNoiz = 0.0f;
        Biome maxBiome = null;

        HashMap<Biome, Float> biomes = getBiomes(height);

        for (Biome biome : biomes.keySet()) {
            if (biomes.get(biome) >= maxNoiz) {
                maxNoiz = biomes.get(biome);
                maxBiome = biome;
            }
        }

        if (maxBiome == null)
            return WATER;

        return maxBiome;
    }

    /**
     * Returns the mapping between the 3 closest biomes and "amount of the biome" in
     * this location. This is just so that we can limit the amount of calculations
     * we have to do. This could probably be cleaned up a bit
     */
    private static HashMap<Biome, Float> getBiomes(float value) {
        HashMap<Biome, Float> biomes = new HashMap<>(3);

        Biome closestBiome = null, secondClosestBiome = null, thirdClosestBiome = null;
        float closestDist = 10000000, secondClosestDist = 10000000, thirdClosestDist = 10000000;

        for (Biome biome : Biome.values()) {
            float dist = getSquaredDistance(biome, value);

            if (dist <= closestDist) {
                thirdClosestDist = secondClosestDist;
                thirdClosestBiome = secondClosestBiome;
                secondClosestDist = closestDist;
                secondClosestBiome = closestBiome;
                closestDist = dist;
                closestBiome = biome;
            } else if (dist <= secondClosestDist) {
                thirdClosestDist = secondClosestDist;
                thirdClosestBiome = secondClosestBiome;
                secondClosestDist = dist;
                secondClosestBiome = biome;
            } else if (dist <= thirdClosestDist) {
                thirdClosestDist = dist;
                thirdClosestBiome = biome;
            }
        }

        biomes.put(closestBiome, (float) (10.f / Math.sqrt(closestDist)));
        biomes.put(secondClosestBiome, (float) (10.f / Math.sqrt(secondClosestDist)));
        biomes.put(thirdClosestBiome, (float) (10.f / Math.sqrt(thirdClosestDist)));

        return biomes;
    }

    private static float getSquaredDistance(Biome biome, float value) {
        return Math.abs((biome.heightFactor - value) * (biome.heightFactor - value));
    }

    public static enum VegetationType {
        NONE, TREES, BUSHES;
    }
}
