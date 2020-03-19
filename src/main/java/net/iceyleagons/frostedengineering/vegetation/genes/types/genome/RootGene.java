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

import org.bukkit.util.Vector;

import fastnoise.MathUtils;
import net.iceyleagons.frostedengineering.utils.math.Bounds;
import net.iceyleagons.frostedengineering.utils.math.Function;
import net.iceyleagons.frostedengineering.utils.math.Plane;
import net.iceyleagons.frostedengineering.vegetation.genes.types.interfaces.IRootGene;
import net.iceyleagons.frostedengineering.vegetation.parts.Branch;
import net.iceyleagons.frostedengineering.vegetation.parts.Root;

public class RootGene implements IRootGene {

	private int min;
	private int max;
	private Bounds radiusBounds;
	private Function curve;
	private Bounds lengthBounds;

	public RootGene(int min, int max, Function curve, Bounds radiusBounds, Bounds lengthBounds) {
		this.min = min;
		this.max = max;
		this.curve = curve;
		this.radiusBounds = radiusBounds;
		this.lengthBounds = lengthBounds;
	}

	@Override
	public Root next(Branch trunk) {
		Vector yAxis = trunk.getDirection();
		Vector xAxis = MathUtils.getRandomPerpVector(yAxis);
		Plane plane = new Plane(xAxis, yAxis);
		double length = lengthBounds.nextValue();
		return new Root(trunk.getBegin().subtract(xAxis.clone().multiply(length * 0.75)), plane, length,
				radiusBounds.nextValue(), curve);
	}

	@Override
	public int nextAmount() {
		return MathUtils.randomInt(min, max);
	}

}
