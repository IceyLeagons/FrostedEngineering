package net.iceyleagons.worldgenerator.trees.data;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.util.Vector;

@AllArgsConstructor
@EqualsAndHashCode
@Setter
public class Branch {

    private Vector begin, end;
    @Getter
    private double radius;

    public Vector getBegin() {
        return begin.clone();
    }

    public Vector getEnd() {
        return end.clone();
    }

    public double getLength() {
        return begin.distance(end);
    }

    public Vector getDirection() {
        return end.clone().subtract(begin).normalize();
    }

    public enum Thickness {
        THIN, THICK
    }

    public Thickness getThickness() {
        return radius > 0.33 ? Thickness.THICK : Thickness.THIN;
    }

    public Branch split(Vector axis, double angle, double decay) {
        Vector dir = getDirection().clone().rotateAroundAxis(axis, angle);

        if (decay > 1) decay = 1;
        dir.multiply(getLength() * (1 - decay));
        Branch branch = new Branch(end, new Vector(end.getX() + dir.getX(), end.getY() + dir.getY(), end.getZ() + dir.getZ()), radius * (1 - decay));

        if (branch.getLength() < 1) return null;
        else return branch;
    }

}
