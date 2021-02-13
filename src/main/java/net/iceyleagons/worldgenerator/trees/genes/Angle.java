package net.iceyleagons.worldgenerator.trees.genes;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.iceyleagons.worldgenerator.utils.Range;

@AllArgsConstructor
@Data
public class Angle {
    private Range range;

    public double next() {
        return range.random();
    }
}
