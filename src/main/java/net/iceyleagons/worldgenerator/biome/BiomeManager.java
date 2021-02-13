package net.iceyleagons.worldgenerator.biome;

import com.google.common.collect.Maps;
import net.iceyleagons.icicle.wrapped.biome.WrappedBiomeBase;
import net.iceyleagons.icicle.wrapped.biome.WrappedBiomeFog;
import net.iceyleagons.worldgenerator.biome.impl.*;
import net.iceyleagons.worldgenerator.biome.utils.Humidity;
import net.iceyleagons.worldgenerator.biome.utils.Temperature;
import org.bukkit.NamespacedKey;
import org.bukkit.World;

import java.awt.*;
import java.util.List;
import java.util.*;

public class BiomeManager {
    public final int seed;
    public static Random random;
    public final List<Biome> biomeList;
    public static final Map<String, WrappedBiomeBase> biomeBaseMap = Maps.newHashMap();
    public final World world;

    static {
        random = new Random();
    }

    public BiomeManager(World world) {
        this.seed = (int) (world.getSeed() / 2);
        random = new Random(seed);
        this.biomeList = new ArrayList<>();
        biomeList.add(new Jungle(seed, 0.0, 0.0));
        biomeList.add(new Oasis(seed, 0.0, 0.0));
        biomeList.add(new Island(seed, 0.0, 0.0));
        biomeList.add(new Sea(seed, 0.0, 0.0));
        biomeList.add(new Mountain(seed, 0.0, 0.0));
        biomeList.add(new Swamp(seed, 0.0, 0.0));
        this.world = world;

        postSetup();
    }

    private void postSetup() {
        Collections.shuffle(biomeList, random);

        double stepSize = Math.PI * 2 / biomeList.size();
        double commonValue = 0.0;

        for (Biome biome : biomeList) {
            biome.setHumidity(Math.cos(commonValue) / 4 + .5);
            biome.setTemperature(Math.sin(commonValue) / 4 + .5);
            System.out.printf("Biome %s has been assigned the temperature value of %s, and a humidity value of %s.%n", biome.getBiomeName(), biome.getTemperature(), biome.getHumidity());
            commonValue += stepSize;
        }
    }

    static {
        biomeBaseMap.putIfAbsent("fe:river", WrappedBiomeBase.Builder.create()
                .setScale(1.f)
                .setDepth(1.f)
                .setDownfall(1.f)
                .setGeneration()
                .setMobs()
                .setPrecipitation(WrappedBiomeBase.Precipitation.RAIN)
                .setGeography(WrappedBiomeBase.Geography.RIVER)
                .setTemperature(1.f)
                .setTemperatureModifier(WrappedBiomeBase.TemperatureModifier.NONE)
                .setSpecialEffects(WrappedBiomeFog.Builder.create()
                        .setFogColor(Color.CYAN)
                        .setSkyColor(new Color(124, 172, 252))
                        .setWaterFogColor(new Color(0, 187, 255))
                        .setWaterColor(new Color(0, 79, 107))
                        .build())
                .build().register(new NamespacedKey("fe", "river")));

        biomeBaseMap.putIfAbsent("fe:plains", WrappedBiomeBase.Builder.create()
                .setScale(1.f)
                .setDepth(1.f)
                .setDownfall(1.f)
                .setGeneration()
                .setMobs()
                .setPrecipitation(WrappedBiomeBase.Precipitation.RAIN)
                .setGeography(WrappedBiomeBase.Geography.PLAINS)
                .setTemperature(1.f)
                .setTemperatureModifier(WrappedBiomeBase.TemperatureModifier.NONE)
                .setSpecialEffects(WrappedBiomeFog.Builder.create()
                        .setFogColor(Color.CYAN)
                        .setSkyColor(new Color(124, 172, 252))
                        .setWaterFogColor(new Color(0, 187, 255))
                        .setWaterColor(new Color(0, 140, 140))
                        .build())
                .build().register(new NamespacedKey("fe", "plains")));
    }

    public int getHeight(int x, int z) {
        double baseValue = 0.0;
        double dividingValue = 0.0;

        for (Biome biome : biomeList) {
            double temperature = biome.toValue(x, z);
            double height = biome.getHeight(x, z);

            baseValue += temperature * height;
            dividingValue += temperature;
        }

        return (int) Math.round(baseValue / dividingValue);
    }

    public Biome getBiome(int x, int z) {
        return getBiome(new Humidity(seed).getHumidity(x, z), new Temperature(seed).getTemperature(x, z), biomeList);
    }

    public Biome getBiome(int x, int z, Random random) {
        return getBiome(new Humidity(seed).getHumidity(x, z) + (random.nextDouble() * 2.0 - 1.0) * 0.005,
                new Temperature(seed).getTemperature(x, z) + (random.nextDouble() * 2.0 - 1.0) * 0.005, biomeList);
    }

    private Biome getBiome(double x, double z, List<Biome> biomeList) {
        Biome biome = biomeList.get(0);
        double closestValue = Math.abs(x - biome.getHumidity()) + Math.abs(z - biome.getTemperature());
        for (int i = 1; i < biomeList.size(); ++i) {
            double newValue = Math.abs(x - (biomeList.get(i).getHumidity()) + Math.abs(z - biomeList.get(i).getTemperature()));
            if (!(newValue < closestValue)) continue;
            closestValue = newValue;
            biome = biomeList.get(i);
        }

        return biome;
    }

}
