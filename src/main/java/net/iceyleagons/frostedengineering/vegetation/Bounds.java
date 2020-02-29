package net.iceyleagons.frostedengineering.vegetation;

import fastnoise.MathUtils;

public class Bounds extends Range {
	public Bounds(double min, double max) {
		super(min, max);
	}

	public double nextValue() {
		return MathUtils.randomDouble(this.min, this.max);
	}
}