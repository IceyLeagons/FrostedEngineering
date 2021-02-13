package net.iceyleagons.worldgenerator.trees.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.iceyleagons.worldgenerator.utils.Function;
import net.iceyleagons.worldgenerator.utils.Plane;
import org.bukkit.util.Vector;

@AllArgsConstructor
@Data
public class Root {

    private Vector origin;
    private Plane plane;
    private double length, radius;
    private Function function;

    public Vector getOrigin() {
        return origin.clone();
    }

}
