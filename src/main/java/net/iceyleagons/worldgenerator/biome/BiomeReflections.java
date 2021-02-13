package net.iceyleagons.worldgenerator.biome;

import net.iceyleagons.icicle.reflect.Reflections;
import net.iceyleagons.icicle.wrapped.biome.WrappedBiomeStorage;
import org.bukkit.generator.ChunkGenerator;

import java.lang.reflect.Field;

public class BiomeReflections {

    private static Class<?> cb_CustomBiomeGrid;

    private static Field cb_biomeStorage;

    static {
        cb_CustomBiomeGrid = Reflections.getNormalCBClass("generator.CustomChunkGenerator$CustomBiomeGrid");

        cb_biomeStorage = Reflections.getField(cb_CustomBiomeGrid, "biome", true);
    }

    public static WrappedBiomeStorage getBiomeStorage(ChunkGenerator.BiomeGrid grid) {
        try {
            return new WrappedBiomeStorage(cb_biomeStorage.get(grid));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

}
