package net.iceyleagons.worldgenerator.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.iceyleagons.worldgenerator.biome.BiomeManager;

@AllArgsConstructor(staticName = "of")
@Getter
@Setter
public class Range {

    private double min, max;

    public double getDifference() {
        return max - min;
    }

    public double random() {
        return min + getDifference() * BiomeManager.random.nextDouble();
    }

    public static double map(double value, Range old, Range New) {
        double diff = old.getDifference();
        if (diff != 0)
            return (((value - old.getMin()) * New.getDifference()) / diff) + New.getMin();

        return value;
    }


}
