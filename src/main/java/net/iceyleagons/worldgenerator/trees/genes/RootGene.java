package net.iceyleagons.worldgenerator.trees.genes;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.iceyleagons.worldgenerator.biome.BiomeManager;
import net.iceyleagons.worldgenerator.trees.data.Branch;
import net.iceyleagons.worldgenerator.trees.data.Root;
import net.iceyleagons.worldgenerator.utils.Function;
import net.iceyleagons.worldgenerator.utils.Plane;
import net.iceyleagons.worldgenerator.utils.Range;
import org.bukkit.util.Vector;

@AllArgsConstructor
@Data
public class RootGene {

    private int min, max;
    private Range radius, length;
    private Function curve;

    public Root next(Branch trunk) {
        Vector yAxis = trunk.getDirection();
        Vector xAxis = getRandomPerpVector(yAxis);
        Plane plane = new Plane(xAxis, yAxis);
        double length = this.length.random();
        return new Root(trunk.getBegin().subtract(xAxis.clone().multiply(length * 0.75)), plane, length, radius.random(), curve);

    }

    public int nextAmount() {
        return BiomeManager.random.nextInt(max - min + 1) + min;
    }

    public static Vector getRandomPerpVector(Vector v) {
        Vector randomVector = new Vector(randomDouble(-1, 1), randomDouble(-1, 1), randomDouble(-1, 1));
        return randomVector.subtract(v.clone().multiply(randomVector.dot(v) / v.dot(v)));
    }

    private static double randomDouble(double min, double max) {
        return min + (max - min) * BiomeManager.random.nextDouble();
    }


}
