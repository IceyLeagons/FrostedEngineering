package net.iceyleagons.worldgenerator.api;

import lombok.NonNull;
import net.iceyleagons.worldgenerator.biome.Biome;
import net.iceyleagons.worldgenerator.biome.BiomeManager;
import net.iceyleagons.worldgenerator.trees.data.Cluster;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.Nullable;

public interface WorldGeneratorAPI {

    void generateTree(@NonNull Location location, @Nullable Cluster type);

    void pasteTree(@NonNull Location location, @NonNull Biome biome);

    @Nullable
    BiomeManager getManager(@NonNull World world);

    @Nullable
    Biome getBiome(@NonNull World world, @NonNull String name);

    void addPopulator(World world, BlockPopulator populator);

    void addStructure(World world, Object structure);

    void generateStructure(Location location, Object structure);

}
