package net.iceyleagons.worldgenerator.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

@AllArgsConstructor(staticName = "of")
@Getter
public class BData {

    private final BlockData blockData;

    public static BData of(Material material) {
        return BData.of(material.createBlockData());
    }

}
