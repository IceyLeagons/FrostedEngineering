/*******************************************************************************
 * Copyright (C) IceyLeagons(https://iceyleagons.net/) 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iceyleagons.frostedengineering.vegetation.genes.types.genome;

import fastnoise.MathUtils;
import net.iceyleagons.frostedengineering.utils.math.Bounds;
import net.iceyleagons.frostedengineering.vegetation.genes.types.interfaces.IDecayGene;

public class DecayGene implements IDecayGene {

	private Bounds bounds;

	public DecayGene(Bounds bounds) {
		this.bounds = bounds;
	}

	@Override
	public double next() {
		return bounds.nextValue();
	}

	@Override
	public double next(double angle, Bounds angleBounds) {
		return MathUtils.map(Math.abs(angle), angleBounds, bounds);
	}

	@Override
	public double next(double previous) {
		return bounds.nextValue();
	}

}
