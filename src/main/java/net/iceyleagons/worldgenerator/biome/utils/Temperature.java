package net.iceyleagons.worldgenerator.biome.utils;

import net.iceyleagons.worldgenerator.WorldGenerator;
import net.iceyleagons.worldgenerator.generator.PerlinNoise;

import java.util.Random;

public class Temperature {

    private int randomKey;

    public Temperature(int baseSeed) {
        randomKey = new Random(baseSeed * 4).nextInt();
    }

    public double getTemperature(int x, int z) {
        return (PerlinNoise.getNoise(x * WorldGenerator.biomeSize.getSize(), z * WorldGenerator.biomeSize.getSize(), randomKey + 1)) / 2;
    }

}
