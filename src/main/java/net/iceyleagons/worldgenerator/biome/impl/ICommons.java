package net.iceyleagons.worldgenerator.biome.impl;

import net.iceyleagons.icicle.wrapped.biome.WrappedBiomeBase;
import net.iceyleagons.icicle.wrapped.biome.WrappedBiomeStorage;
import net.iceyleagons.worldgenerator.generator.FrostedChunkGenerator;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public interface ICommons {

    default void fillWithEither(Random random, ChunkGenerator.ChunkData chunkData, int x, int y, int z, BlockData... materials) {
        chunkData.setBlock(x, y, z, materials[random.nextInt(materials.length)]);
    }

    default boolean isTaller(int x, int height, int z, ChunkGenerator.ChunkData chunkData, WrappedBiomeStorage biomeStorage, WrappedBiomeBase wrappedBiomeBase) {
        if (height < 94) {
            chunkData.setBlock(x, height, z, Material.SAND);
            chunkData.setBlock(x, height - 1, z, Material.SAND);
            chunkData.setBlock(x, height - 2, z, Material.SAND);
            chunkData.setBlock(x, height - 3, z, Material.SAND);
            chunkData.setBlock(x, height - 4, z, Material.STONE);
            FrostedChunkGenerator.fillWithWater(x, 94, z, chunkData);

            for (int i = 0; i < 256; i++)
                biomeStorage.setBiome(x >> 2, i >> 2, z >> 2, wrappedBiomeBase);
            return false;
        }

        return true;
    }

}
