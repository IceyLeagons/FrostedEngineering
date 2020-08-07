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

import fastnoise.MathUtils;

public class Exponential extends Function {

    private double a;
    private double b;

    public Exponential(double a, double b) {
        super("y = " + a + "* x^" + b); // y = ax^b
        this.a = a;
        this.b = b;

    }

    @Override
    public double f(double x) {
        return (MathUtils.fastPow(x, b) * a);
    }

}
