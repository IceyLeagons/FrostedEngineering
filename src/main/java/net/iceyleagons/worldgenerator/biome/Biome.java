package net.iceyleagons.worldgenerator.biome;

import lombok.Getter;
import net.iceyleagons.icicle.schematic.Schematic;
import net.iceyleagons.icicle.wrapped.biome.WrappedBiomeBase;
import net.iceyleagons.icicle.wrapped.biome.WrappedBiomeFog;
import net.iceyleagons.icicle.wrapped.biome.WrappedBiomeStorage;
import net.iceyleagons.worldgenerator.biome.utils.Humidity;
import net.iceyleagons.worldgenerator.biome.utils.Temperature;
import org.bukkit.NamespacedKey;
import org.bukkit.generator.ChunkGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Biome {

    protected final int seed;
    protected double humidity, temperature;
    @Getter
    private List<Schematic> treeList;

    public String getNamespace() {
        return "fe";
    }

    public String getBiomeName() {
        return "default-biome" + ThreadLocalRandom.current().nextInt(100);
    }

    public Color getFog() {
        return Color.CYAN;
    }

    public Color getSkyColor() {
        return new Color(124, 172, 252);
    }

    public Color getWaterColor() {
        return new Color(0, 187, 255);
    }

    public Color getWaterFogColor() {
        return new Color(0, 79, 107);
    }

    public WrappedBiomeBase.Precipitation getPrecipitation() {
        return WrappedBiomeBase.Precipitation.RAIN;
    }

    public WrappedBiomeBase.TemperatureModifier getTemperatureModifier() {
        return WrappedBiomeBase.TemperatureModifier.NONE;
    }

    public WrappedBiomeBase.Geography getGeography() {
        return WrappedBiomeBase.Geography.PLAINS;
    }

    public Object[] getStructures() {
        return null;
    }

    public void populateChunkGenerator(BiomeManager manager, ChunkGenerator.ChunkData chunkData, int chunkX, int chunkZ, int rowX, int rowZ, Random random, WrappedBiomeStorage biomeStorage) {
    }

    public Biome(int seed, double humidity, double temperature) {
        this.seed = seed;
        this.humidity = humidity;
        this.temperature = temperature;

        this.treeList = new ArrayList<>(5);

        WrappedBiomeBase biomeBase = WrappedBiomeBase.Builder.create()
                .setDepth(1.f)
                .setDownfall(1.f)
                .setGeneration()
                .setMobs()
                .setPrecipitation(getPrecipitation())
                .setTemperature(1.f)
                .setTemperatureModifier(getTemperatureModifier())
                .setGeography(getGeography())
                .setScale(1.f)
                .setSpecialEffects(WrappedBiomeFog.Builder.create()
                        .setFogColor(getFog())
                        .setWaterColor(getWaterColor())
                        .setWaterFogColor(getWaterFogColor())
                        .setSkyColor(getSkyColor())
                        .build())
                .build();

        biomeBase.register(new NamespacedKey(getNamespace(), getBiomeName()));
        BiomeManager.biomeBaseMap.put(getNamespace() + ":" + getBiomeName(), biomeBase);
    }

    public double getHumidity() {
        return humidity;
    }

    public double getTreeChance() {
        return humidity;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getHeight(int x, int z) {
        return 63;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double toValue(int x, int z) {
        double humidityValue = new Humidity(seed).getHumidity(x, z);
        double temperatureValue = new Temperature(seed).getTemperature(x, z);
        return Math.exp(((1.0 - Math.abs(humidityValue - humidity)) + (1.0 - Math.abs(temperatureValue - temperature))) / 2.0 * 50.0);
    }

}
