package net.iceyleagons.frostedengineering.utils.math;

import fastnoise.MathUtils;

public class Exponential extends Function {

	private double a;
	private double b;

	public Exponential(double a, double b) {
		super("y = " + a + "* x^" + b); // y = ax^b
		this.a = a;
		this.b = b;

	}

	@Override
	public double f(double x) {
		return (MathUtils.fastPow(x, b) * a);
	}

}