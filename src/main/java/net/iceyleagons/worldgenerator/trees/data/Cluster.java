package net.iceyleagons.worldgenerator.trees.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.iceyleagons.worldgenerator.trees.genes.*;
import net.iceyleagons.worldgenerator.utils.BData;

@AllArgsConstructor
@Getter
public class Cluster {
    private final Clump clump;
    private final Split split;
    private final Angle angle;
    private final Decay decay;
    private final Trunk trunk;
    private final LeafGene leaf;
    private final RootGene root;

    private final BData[] leafMaterial, thickMaterial, thinMaterial, rootMaterial;

    public Cluster(Clump clump, Split split, Angle angle, Decay decay, Trunk trunk, LeafGene leaf, RootGene root, BData leafMaterial, BData thickMaterial, BData thinMaterial, BData rootMaterial) {
        this(clump, split, angle, decay, trunk, leaf, root, new BData[]{leafMaterial}, new BData[]{thickMaterial}, new BData[]{thinMaterial}, new BData[]{rootMaterial});
    }

}
