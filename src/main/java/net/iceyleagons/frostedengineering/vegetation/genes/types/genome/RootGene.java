package net.iceyleagons.frostedengineering.vegetation.genes.types.genome;

import org.bukkit.util.Vector;

import fastnoise.MathUtils;
import net.iceyleagons.frostedengineering.utils.math.Bounds;
import net.iceyleagons.frostedengineering.utils.math.Function;
import net.iceyleagons.frostedengineering.utils.math.Plane;
import net.iceyleagons.frostedengineering.vegetation.genes.types.interfaces.IRootGene;
import net.iceyleagons.frostedengineering.vegetation.parts.Branch;
import net.iceyleagons.frostedengineering.vegetation.parts.Root;

public class RootGene implements IRootGene {

	private int min;
	private int max;
	private Bounds radiusBounds;
	private Function curve;
	private Bounds lengthBounds;

	public RootGene(int min, int max, Function curve, Bounds radiusBounds, Bounds lengthBounds) {
		this.min = min;
		this.max = max;
		this.curve = curve;
		this.radiusBounds = radiusBounds;
		this.lengthBounds = lengthBounds;
	}

	@Override
	public Root next(Branch trunk) {
		Vector yAxis = trunk.getDirection();
		Vector xAxis = MathUtils.getRandomPerpVector(yAxis);
		Plane plane = new Plane(xAxis, yAxis);
		double length = lengthBounds.nextValue();
		return new Root(trunk.getBegin().subtract(xAxis.clone().multiply(length * 0.75)), plane, length,
				radiusBounds.nextValue(), curve);
	}

	@Override
	public int nextAmount() {
		return MathUtils.randomInt(min, max);
	}

}
