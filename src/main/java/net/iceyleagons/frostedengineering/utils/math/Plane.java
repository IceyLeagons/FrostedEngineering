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
package net.iceyleagons.frostedengineering.utils.math;

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
