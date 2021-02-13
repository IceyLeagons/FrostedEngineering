package net.iceyleagons.worldgenerator.trees.genes;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.iceyleagons.worldgenerator.biome.BiomeManager;

@AllArgsConstructor
@Data
public class Split {

    private int min, max;

    public int next() {
        return BiomeManager.random.nextInt(max - min + 1) + min;
    }

}
