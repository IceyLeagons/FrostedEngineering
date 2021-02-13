package net.iceyleagons.worldgenerator.biome.utils;

public enum BiomeSize {

    TINY(0.001),
    SMALL(0.0008),
    MODERATE(0.0004),
    BIG(0.0002),
    LARGE(0.0001);

    private double size;

    BiomeSize(double size) {
        this.size = size;
    }

    public double getSize() {
        return size;
    }

}
