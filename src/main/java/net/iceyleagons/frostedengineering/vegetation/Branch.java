package net.iceyleagons.frostedengineering.vegetation;

import fastnoise.MathUtils.Vector3;
import lombok.Getter;
import lombok.Setter;

/**
 * Tree-branch class. Nothing special.
 *
 * @author Gabe
 * @version 1.0
 */
public class Branch {
    @Getter
    Branch parent;
    @Getter
    @Setter
    Vector3 direction, originalDirection, position;
    int length;
    public int count;

    /**
     * Creates a completely new branch with the specified data. Useful for trunks, etc.
     *
     * @param parent    the parent of the branch. Ie: a trunk.
     * @param direction the direction the branch goes in.
     * @param position  the position of the branch.
     */
    public Branch(Branch parent, Vector3 direction, Vector3 position) {
        this.length = 1;
        this.count = 0;
        this.parent = parent;
        this.position = position;
        this.direction = direction;
        this.originalDirection = direction;
    }

    /**
     * Creates a new branch with the specified parent.
     *
     * @param anotherBranch the parent branch
     */
    public Branch(Branch anotherBranch) {
        this.length = 1;
        this.count = 0;
        this.parent = anotherBranch;
        this.position = parent.next();
        this.direction = parent.direction;
        this.originalDirection = direction;
    }

    /**
     * Gets the next position of the branch.
     *
     * @return the next position of the branch
     */
    public Vector3 next() {
        final Vector3 nextDir = Vector3.mul(this.direction, this.length);
        final Vector3 nextPos = Vector3.add(this.position, nextDir);
        return nextPos;
    }

    /**
     * Resets everything.
     */
    public void reset() {
        this.count = 0;
        this.direction = this.originalDirection;
    }
}