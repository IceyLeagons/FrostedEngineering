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
package net.iceyleagons.frostedengineering.energy.network;

import net.iceyleagons.frostedengineering.energy.interfaces.ExplodableComponent;

public enum NetworkType {

	LOW_VOLTAGE(0f, 10f), MEDIUM_VOLTAGE(10f, 50f), HIGH_VOLTAGE(50f, Float.POSITIVE_INFINITY);

	private float bound1, bound2;

	/**
	 * @param bound1 is the bound where the {@link EnergyNetwork} is considered as a
	 *               {@link #values()}
	 * @param bound2 is the end of that bound, above that it is considered as an
	 *               other {@link NetworkType}
	 */
	private NetworkType(float bound1, float bound2) {
		this.bound1 = bound1;
		this.bound2 = bound2;
	}

	/**
	 * @return the starting value of a {@link NetworkType}
	 */
	public float getStartingFE() {
		return bound1;
	}

	/**
	 * @return the ending value of a {@link NetworkType}
	 */
	public float getEndingFE() {
		return bound2;
	}

	/**
	 * @param val is the current FrostedPower value of an {@link EnergyNetwork}
	 * @return the {@link NetworkType} which is in range.
	 */
	public static NetworkType getClassification(float val) {
		for (NetworkType nt : values()) {
			if (nt.getStartingFE() <= val && nt.getEndingFE() >= val) {
				return nt;
			}
		}
		return null;
	}

	/**
	 * @param capable is the {@link ExplodableComponent}'s {@link NetworkType} it can capable of
	 * @param parent is the {@link NetworkType} of an {@link EnergyNetwork}
	 * @return true if {@link ExplodableComponent} should explode
	 */
	public static boolean doExplode(NetworkType capable, NetworkType parent) {
		if (capable == parent) {
			return false;
		} else {
			if (parent == LOW_VOLTAGE) return false;
			if (parent == MEDIUM_VOLTAGE && (capable == MEDIUM_VOLTAGE || capable == HIGH_VOLTAGE)) return false;
			return true;
		}
	}

}
