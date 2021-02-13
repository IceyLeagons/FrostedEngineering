package net.iceyleagons.worldgenerator.trees;

import net.iceyleagons.worldgenerator.WorldGenerator;
import net.iceyleagons.worldgenerator.trees.data.Cluster;
import net.iceyleagons.worldgenerator.trees.genes.*;
import net.iceyleagons.worldgenerator.utils.Function;
import net.iceyleagons.worldgenerator.utils.BData;
import net.iceyleagons.worldgenerator.utils.Range;
import org.bukkit.Material;

public class StandardClusters {

    public static Cluster OAK = new Cluster(new Clump(.05), new Split(3, 4), new Angle(Range.of(0, Math.PI / 3)),
            new Decay(Range.of(.23, .34)), new Trunk(Range.of(6, 9), Range.of(1.5, 2), Range.of(-1, 1)), new LeafGene(1, 2.2),
            new RootGene(4, 6, Range.of(1, 1), Range.of(4, 5), new Function() {
                @Override
                public double complete(double x, double a, double b) {
                    return (Math.pow(x, b) * a);
                }

                @Override
                public double getA() {
                    return 1;
                }

                @Override
                public double getB() {
                    return 1.2;
                }
            }), BData.of(WorldGenerator.permanentLeaves), BData.of(Material.OAK_LOG), BData.of(Material.SPRUCE_FENCE), BData.of(Material.OAK_LOG));

}
