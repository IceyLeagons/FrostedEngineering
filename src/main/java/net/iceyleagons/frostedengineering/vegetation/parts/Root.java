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
package net.iceyleagons.frostedengineering.vegetation.parts;

import org.bukkit.util.Vector;

import net.iceyleagons.frostedengineering.utils.math.Function;
import net.iceyleagons.frostedengineering.utils.math.Plane;

public class Root {

    private Vector origin;
    private Plane plane;
    private double length;
    private double radius;
    private Function function;

    public Root(Vector origin, Plane plane, double length, double radius, Function function) {
        this.origin = origin;
        this.plane = plane;
        this.length = length;
        this.radius = radius;
        this.function = function;
    }

    public Vector getOrigin() {
        return origin.clone();
    }

    public Plane getPlane() {
        return plane;
    }

    public double getLength() {
        return length;
    }

    public double getRadius() {
        return radius;
    }

    public Function getFunction() {
        return function;
    }
}
