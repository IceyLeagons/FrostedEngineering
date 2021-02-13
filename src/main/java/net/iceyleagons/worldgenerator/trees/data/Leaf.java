package net.iceyleagons.worldgenerator.trees.data;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.util.Vector;

@AllArgsConstructor
@EqualsAndHashCode
@Setter
public class Leaf {

    Vector center;

    @Getter
    double radius;

    public Vector getCenter() {
        return center.clone();
    }

}
