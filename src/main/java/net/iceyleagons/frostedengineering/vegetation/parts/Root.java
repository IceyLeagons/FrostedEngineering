package net.iceyleagons.frostedengineering.vegetation.parts;

import org.bukkit.util.Vector;

import net.iceyleagons.frostedengineering.utils.math.Function;
import net.iceyleagons.frostedengineering.utils.math.Plane;

public class Root {

	private Vector origin;
	private Plane plane;
	private double length;
	private double radius;
	private Function function;

	public Root(Vector origin, Plane plane, double length, double radius, Function function) {
		this.origin = origin;
		this.plane = plane;
		this.length = length;
		this.radius = radius;
		this.function = function;
	}

	public Vector getOrigin() {
		return origin.clone();
	}

	public Plane getPlane() {
		return plane;
	}

	public double getLength() {
		return length;
	}

	public double getRadius() {
		return radius;
	}

	public Function getFunction() {
		return function;
	}
}
