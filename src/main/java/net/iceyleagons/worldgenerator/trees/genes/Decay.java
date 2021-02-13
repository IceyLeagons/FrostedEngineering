package net.iceyleagons.worldgenerator.trees.genes;

import lombok.AllArgsConstructor;
import lombok.Setter;
import net.iceyleagons.worldgenerator.utils.Range;

@AllArgsConstructor
@Setter
public class Decay {

    private Range range;

    public double next() {
        return range.random();
    }

    public double next(double angle, Range range) {
        return Range.map(Math.abs(angle), range, this.range);
    }

}
