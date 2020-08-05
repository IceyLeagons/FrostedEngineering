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
package net.iceyleagons.frostedengineering.network.energy;


public enum EnergyNetworkType {

    LOW_VOLTAGE(0f, 600f, 1), MEDIUM_VOLTAGE(600f, 2000f, 2), HIGH_VOLTAGE(2000f, Float.POSITIVE_INFINITY, 3);

    private float bound1, bound2;
    private int id;

    /**
     * @param bound1 is the bound where the {@link net.iceyleagons.frostedengineering.network.energy.EnergyNetwork} is considered as a
     *               {@link #values()}
     * @param bound2 is the end of that bound, above that it is considered as an
     *               other {@link EnergyNetworkType}
     * @param id     is mainly used for easier comparison.
     */

    private EnergyNetworkType(float bound1, float bound2, int id) {
        this.bound1 = bound1;
        this.bound2 = bound2;
        this.id = id;
    }

    /**
     * @return the id of that type, mainly used for easier comparison
     */
    public int getID() {
        return id;
    }

    /**
     * @return the starting value of a {@link EnergyNetworkType}
     */
    public float getStartingFE() {
        return bound1;
    }

    /**
     * @return the ending value of a {@link EnergyNetworkType}
     */
    public float getEndingFE() {
        return bound2;
    }

    /**
     * @param val is the current FrostedPower value of an {@link net.iceyleagons.frostedengineering.network.energy.EnergyNetwork}
     * @return the {@link EnergyNetworkType} which is in range.
     */
    public static EnergyNetworkType getClassification(float val) {
        for (EnergyNetworkType nt : values()) {
            if (nt.getStartingFE() <= val && nt.getEndingFE() >= val) {
                return nt;
            }
        }
        return null;
    }

}
