package net.iceyleagons.frostedengineering.vegetation.parts;

import org.bukkit.util.Vector;

import fastnoise.MathUtils;

public class Branch {
	protected Vector begin;

	protected Vector end;

	protected double radius;

	public Branch(Vector paramVector1, Vector paramVector2, double paramDouble) {
		this.begin = paramVector1;
		this.end = paramVector2;
		this.radius = paramDouble;
	}

	public Vector getBegin() {
		return this.begin.clone();
	}

	public Vector getEnd() {
		return this.end.clone();
	}

	public double getRadius() {
		return this.radius;
	}

	public double getLength() {
		return this.begin.distance(this.end);
	}

	public Vector getDirection() {
		return this.end.clone().subtract(this.begin).normalize();
	}

	public enum Thickness {
		THIN, THICK;
	}

	public Thickness getThickness() {
		return (this.radius > 0.33D) ? Thickness.THICK : Thickness.THIN;
	}

	public Branch split(Vector base, double angle, double angle2) {
		Vector vector1 = getDirection();
		vector1 = MathUtils.getRotatedVector(vector1, base, angle);
		if (angle2 > 1.0D)
			angle2 = 1.0D;
		vector1.multiply(getLength() * (1.0D - angle2));
		Vector vector2 = new Vector(this.end.getX() + vector1.getX(), this.end.getY() + vector1.getY(),
				this.end.getZ() + vector1.getZ());
		Branch branch = new Branch(this.end, vector2, this.radius * (1.0D - angle2));
		if (branch.getLength() < 1.0D)
			return null;
		return branch;
	}
}