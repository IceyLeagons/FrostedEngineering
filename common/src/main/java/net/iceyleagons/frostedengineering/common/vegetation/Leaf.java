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

package net.iceyleagons.frostedengineering.common.vegetation;

import lombok.Getter;

import static fastnoise.MathUtils.*;

/**
 * A trees leaf class. Nothing special.
 *
 * @author Gabe
 * @version 1.0
 */
public class Leaf {
    /**
     * The position of the leaf.
     */
    @Getter
    private Vector3 position;
    /**
     * Whether or not this leaf's been reached yet.
     */
    public boolean reached = false;

    /**
     * Initiates a new leaf Object.
     *
     * @param position the position of the leaves.
     */
    public Leaf(Vector3 position) {
        this.position = position;
    }

    /**
     * Get the X coordinate of the leaf.
     *
     * @return the X coordinate.
     */
    public int getX() {
        return (int) position.x;
    }

    /**
     * Get the Y coordinate of the leaf.
     *
     * @return the Y coordinate.
     */
    public int getY() {
        return (int) position.y;
    }

    /**
     * Get the Z coordinate of the leaf.
     *
     * @return the Z coordinate.
     */
    public int getZ() {
        return (int) position.z;
    }
}
