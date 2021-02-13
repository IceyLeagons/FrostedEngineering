package net.iceyleagons.worldgenerator.biome.utils;

import net.iceyleagons.worldgenerator.WorldGenerator;
import net.iceyleagons.worldgenerator.generator.PerlinNoise;

import java.util.Random;

public class Humidity {

    private int randomKey;

    public Humidity(int baseSeed) {
        randomKey = new Random(baseSeed * 2).nextInt();
    }

    public double getHumidity(int x, int z) {
        return (PerlinNoise.getNoise(x * WorldGenerator.biomeSize.getSize(), z * WorldGenerator.biomeSize.getSize(), randomKey + 1)) / 2;
    }

}
