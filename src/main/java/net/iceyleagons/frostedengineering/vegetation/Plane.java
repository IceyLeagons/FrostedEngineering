package net.iceyleagons.frostedengineering.vegetation;

import org.bukkit.util.Vector;

public class Plane {
	private Vector xAxis;

	private Vector yAxis;

	public Plane(Vector paramVector1, Vector paramVector2) {
		assert paramVector1.dot(paramVector2) == 0.0D : "";
		this.xAxis = paramVector1.normalize();
		this.yAxis = paramVector2.normalize();
	}

	public Vector getXAxis() {
		return this.xAxis.clone();
	}

	public Vector getYAxis() {
		return this.yAxis.clone();
	}

	public Vector translate(double paramDouble1, double paramDouble2) {
		return getXAxis().multiply(paramDouble1).add(getYAxis().multiply(paramDouble2));
	}
}