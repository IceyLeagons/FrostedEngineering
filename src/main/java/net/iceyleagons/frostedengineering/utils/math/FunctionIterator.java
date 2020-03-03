package net.iceyleagons.frostedengineering.utils.math;

import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import fastnoise.MathUtils.Vector2;
import net.iceyleagons.frostedengineering.vegetation.SegmentIterator;

@SuppressWarnings("serial")
public class FunctionIterator extends ArrayList<Block> {

	public FunctionIterator(World world, Vector origin, Plane plane, double length, double radius, Function function) {
		Vector2 prevPoint = function.pointAt(0);
		Vector2 point = function.pointAt(1);
		double currentLength = prevPoint.distance(point);

		while (currentLength < length) {
			Vector sBegin = origin.clone().add(plane.translate(prevPoint.x, prevPoint.y));
			Vector sEnd = origin.clone().add(plane.translate(point.x, point.y));

			for (Block b : new SegmentIterator(world, sBegin, sEnd, radius)) {
				if (!contains(b)) {
					add(b);
				}
			}

			currentLength += prevPoint.distance(point);
			prevPoint = point;
			point = function.pointAt(point.x + 1);
		}
	}

}