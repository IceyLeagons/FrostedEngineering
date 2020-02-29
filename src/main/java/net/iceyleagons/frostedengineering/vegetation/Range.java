package net.iceyleagons.frostedengineering.vegetation;

public class Range {

	protected double min;
	protected double max;

	public Range(double min, double max) {
		assert min < max : "oof";
		this.min = min;
		this.max = max;
	}

	public double getMin() {
		return this.min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMax() {
		return this.max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public double asDouble() {
		return this.max - this.min;
	}

}
