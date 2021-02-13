package net.iceyleagons.worldgenerator.trees.genes;

import lombok.AllArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
public class Clump {
    private double factor;

    public double next() {
        return factor;
    }

}
