package net.iceyleagons.worldgenerator.utils;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.bukkit.util.Vector;

@Setter
@EqualsAndHashCode
public class Plane {
    private Vector xAxis;
    private Vector yAxis;

    public Plane(Vector xAxis, Vector yAxis) {
        this.xAxis = xAxis.normalize();
        this.yAxis = yAxis.normalize();
    }

    public Vector getXAxis() {
        return xAxis.clone();
    }

    public Vector getYAxis() {
        return yAxis.clone();
    }

    public Vector translate(double x, double y) {
        return getXAxis().multiply(x).add(getYAxis().multiply(y));
    }
}
