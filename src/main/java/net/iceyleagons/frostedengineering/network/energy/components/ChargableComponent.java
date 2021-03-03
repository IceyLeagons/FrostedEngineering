/*
 *  Copyright (C) IceyLeagons(https://iceyleagons.net/)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package net.iceyleagons.frostedengineering.network.energy.components;


/**
 * Strictly used for {@link net.iceyleagons.frostedengineering.network.energy.EnergyUnit}s or for {@link net.iceyleagons.frostedengineering.network.Unit}s
 * that have some sort of interest in power, but extending {@link net.iceyleagons.frostedengineering.network.energy.EnergyUnit} is required!
 *
 * @author TOTHTOMI
 */
public interface ChargableComponent {

    /**
     * @return the maximum amount of energy, that this {@link net.iceyleagons.frostedengineering.network.energy.EnergyUnit} can store
     */
    float getMaxStorage();

    /**
     * @return the currently stored amount of energy of the {@link net.iceyleagons.frostedengineering.network.energy.EnergyUnit}
     */
    float getStored();

    /**
     * Adds energy to the {@link net.iceyleagons.frostedengineering.network.energy.EnergyUnit}
     *
     * @param fp amount of power to add
     * @return the amount of power that cannot be stored, because of overcharge.
     */
    float addEnergy(float fp);

    /**
     * Removes energy from the {@link net.iceyleagons.frostedengineering.network.energy.EnergyUnit}
     *
     * @param fp amount of power needed
     * @return the amount of power that the {@link net.iceyleagons.frostedengineering.network.energy.EnergyUnit} can provide, can be less then the required one!
     */
    float consumeEnergy(float fp);

    /**
     * @return true if the {@link #getStored()} equals to the {@link #getMaxStorage()}
     */
    boolean isFull();

}
