package net.iceyleagons.worldgenerator.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class FunctionIterator extends ArrayList<Location> {

    public FunctionIterator(World world, Vector origin, Plane plane, double length, double radius, Function function, double a, double b) {
        Vector prevPoint = new Vector(0, 0, function.complete(0, a, b));
        Vector point = new Vector(1, 0, function.complete(1, a, b));
        double currentLength = prevPoint.distance(point);

        while (currentLength < length) {
            Vector sBegin = origin.clone().add(plane.translate(prevPoint.getX(), prevPoint.getZ()));
            Vector sEnd = origin.clone().add(plane.translate(point.getX(), point.getZ()));

            for (Location location : new SegmentIterator(world, sBegin, sEnd, radius))
                if (!contains(location))
                    add(location);

            currentLength += prevPoint.distance(point);
            prevPoint = point;
            point = new Vector(point.getX() + 1, 0, function.complete(point.getX() + 1, a, b));
        }
    }

}
