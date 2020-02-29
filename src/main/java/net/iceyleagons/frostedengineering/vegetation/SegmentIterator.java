package net.iceyleagons.frostedengineering.vegetation;

import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import fastnoise.MathUtils;

@SuppressWarnings("serial")
public class SegmentIterator extends ArrayList<Block> {

	public SegmentIterator(World world, Vector begin, Vector end, double radius) {
		double length = begin.distance(end);
		Vector dir = end.clone().subtract(begin).normalize();
		Vector rPerp = MathUtils.getRandomPerpVector(dir).normalize();

		for (double i = 0; i < length; i += 0.5) {
			for (double r = 0; r < Math.PI * 2; r += Math.PI / 32) {
				for (double j = 0; j < radius; j += 0.5) {
					Vector up = dir.clone().multiply(i);
					Vector out = MathUtils.getRotatedVector(rPerp, dir, r).multiply(j);
					Vector point = begin.clone().add(up).add(out);
					if (isInRadius(point, begin, dir, radius)) {
						Block block = world.getBlockAt(point.toLocation(world));
						if (!contains(block)) {
							add(block);
						}
					}
				}
			}
		}
	}

	private boolean isInRadius(Vector M0, Vector M1, Vector s, double radius) {
		Vector M0M1 = new Vector(M1.getX() - M0.getX(), M1.getY() - M0.getY(), M1.getZ() - M0.getZ());
		Vector M0M1xs = M0M1.crossProduct(s);
		double d = Math.abs(MathUtils.getMagnitude(M0M1xs) / MathUtils.getMagnitude(s));
		return d < radius;
	}

}