package net.iceyleagons.frostedengineering.vegetation;

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
