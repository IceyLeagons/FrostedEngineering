package net.iceyleagons.frostedengineering.vegetation.genes.types.genome;

import org.bukkit.util.Vector;

import fastnoise.MathUtils;
import net.iceyleagons.frostedengineering.utils.math.Bounds;
import net.iceyleagons.frostedengineering.vegetation.genes.types.interfaces.ITrunkGene;
import net.iceyleagons.frostedengineering.vegetation.parts.Branch;

public class TrunkGene implements ITrunkGene {

	private Bounds heightBounds;
	private Bounds radiusBounds;
	private Bounds offSetBounds;

	public TrunkGene(Bounds heightBounds, Bounds radiusBounds, Bounds offSetBounds) {
		this.heightBounds = heightBounds;
		this.radiusBounds = radiusBounds;
		this.offSetBounds = offSetBounds;
	}

	@Override
	public Branch next() {
		double height = heightBounds.nextValue();
		double radius = MathUtils.map(height, heightBounds, radiusBounds);
		Vector begin = new Vector(0, 0, 0);
		Vector end = new Vector(offSetBounds.nextValue(), height, offSetBounds.nextValue());
		return new Branch(begin, end, radius);
	}

}
