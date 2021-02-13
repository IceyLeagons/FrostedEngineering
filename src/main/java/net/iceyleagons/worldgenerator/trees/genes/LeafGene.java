package net.iceyleagons.worldgenerator.trees.genes;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LeafGene {
    private double maxBranchRadius, radiusMultiplier;
}
