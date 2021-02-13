package net.iceyleagons.worldgenerator.trees.genes;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.iceyleagons.worldgenerator.trees.data.Branch;
import net.iceyleagons.worldgenerator.utils.Range;
import org.bukkit.util.Vector;

@AllArgsConstructor
@Data
public class Trunk {
    private Range height, radius, offset;

    public Branch next() {
        double height = this.height.random();
        double radius = Range.map(height, this.height, this.radius);
        Vector begin = new Vector(0, 0, 0);
        Vector end = new Vector(offset.random(), height, offset.random());
        return new Branch(begin, end, radius);
    }
}
