package net.iceyleagons.worldgenerator.utils;

import net.iceyleagons.worldgenerator.trees.genes.RootGene;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class SegmentIterator extends ArrayList<Location> {

    public SegmentIterator(World world, Vector begin, Vector end, double radius) {
        double length = begin.distance(end);
        Vector dir = end.clone().subtract(begin).normalize();
        Vector perpetualVector = RootGene.getRandomPerpVector(dir).normalize();

        for (double i = 0; i < length; i += .5)
            for (double r = 0; r < Math.PI * 2; r += Math.PI / 32)
                for (double j = 0; j < radius; j += 0.5) {
                    Vector point = begin.clone().add(dir.clone().multiply(i)).add(perpetualVector.rotateAroundAxis(dir, r).multiply(j));
                    if (isInRadius(point, begin, dir, radius)) {
                        Location location = point.toLocation(world);
                        if (!contains(location))
                            add(location);
                    }
                }
    }

    private boolean isInRadius(Vector v1, Vector v2, Vector s, double radius) {
        return Math.abs(getMagnitude(new Vector(v2.getX() - v1.getX(), v2.getY() - v1.getY(), v2.getZ() - v1.getZ()).crossProduct(s)) / getMagnitude(s)) < radius;
    }

    private double getMagnitude(Vector v) {
        return Math.sqrt(v.getX() * v.getX() + v.getY() * v.getY() + v.getZ() * v.getZ());
    }

}
