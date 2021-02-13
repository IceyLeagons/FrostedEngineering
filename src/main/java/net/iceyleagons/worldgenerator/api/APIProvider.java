package net.iceyleagons.worldgenerator.api;

import net.iceyleagons.worldgenerator.biome.Biome;
import net.iceyleagons.worldgenerator.biome.BiomeManager;
import net.iceyleagons.worldgenerator.generator.FrostedChunkGenerator;
import net.iceyleagons.worldgenerator.trees.StandardClusters;
import net.iceyleagons.worldgenerator.trees.Trees;
import net.iceyleagons.worldgenerator.trees.data.Cluster;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class APIProvider implements WorldGeneratorAPI {
    @Override
    public void generateTree(Location location, Cluster type) {
        Trees.create(location, Trees.TreeConstructor.Builder.create(type == null ? StandardClusters.OAK : type).build()).place();
    }

    @Override
    public void pasteTree(Location location, Biome biome) {
        biome.getTreeList().get(BiomeManager.random.nextInt(biome.getTreeList().size())).paste(location, -2, true, false, false);
    }

    @Override
    public BiomeManager getManager(World world) {
        if (!(world.getGenerator() instanceof FrostedChunkGenerator))
            return null;

        return ((FrostedChunkGenerator) world.getGenerator()).getBiomeManager();
    }

    @Override
    public Biome getBiome(World world, String name) {
        BiomeManager manager = getManager(world);

        if (manager != null)
            return manager.biomeList.stream().filter(b -> b.getBiomeName().equalsIgnoreCase(name)).findFirst().orElse(null);

        return null;
    }

    @Override
    public void addPopulator(World world, BlockPopulator populator) {
        if (!(world.getGenerator() instanceof FrostedChunkGenerator))
            return;

        ((FrostedChunkGenerator) world.getGenerator()).getPopulatorList().add(populator);
    }

    @Override
    public void addStructure(World world, Object structure) {
        throw new NotImplementedException("Coming soon!");
    }

    @Override
    public void generateStructure(Location location, Object structure) {
        throw new NotImplementedException("Coming soon!");
    }
}
