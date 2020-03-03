package net.iceyleagons.frostedengineering.vegetation.genes.types.interfaces;

import net.iceyleagons.frostedengineering.utils.math.Bounds;

public interface IDecayGene {

	double next();

	double next(double angle, Bounds angleBounds);

	double next(double previous);

}
