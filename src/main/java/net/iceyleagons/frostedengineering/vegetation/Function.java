package net.iceyleagons.frostedengineering.vegetation;

import fastnoise.MathUtils.Vector2;

public abstract class Function {
	private String functionString;

	public Function(String paramString) {
		this.functionString = paramString;
	}

	@Override
	public String toString() {
		return this.functionString;
	}

	public abstract double f(double paramDouble);

	public Vector2 pointAt(double paramDouble) {
		return new Vector2((float) paramDouble, (float) f(paramDouble));
	}
}