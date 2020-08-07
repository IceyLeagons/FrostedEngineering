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
package net.iceyleagons.frostedengineering.utils.math;

import fastnoise.MathUtils.Vector2;

public abstract class Function {
    private String functionString;

    public Function(String paramString) {
        this.functionString = paramString;
    }

    @Override
    public String toString() {
        return this.functionString;
    }

    public abstract double f(double paramDouble);

    public Vector2 pointAt(double paramDouble) {
        return new Vector2((float) paramDouble, (float) f(paramDouble));
    }
}
