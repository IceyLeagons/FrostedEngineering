package net.iceyleagons.worldgenerator;

import net.iceyleagons.icicle.reflect.Reflections;
import net.iceyleagons.worldgenerator.biome.utils.BiomeSize;
import net.iceyleagons.worldgenerator.generator.FrostedChunkGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URISyntaxException;

public final class WorldGenerator extends JavaPlugin implements IMain {

    public static BlockData permanentLeaves = Material.OAK_LEAVES.createBlockData("[persistent=true]");
    public static IMain MAIN;
    public static BiomeSize biomeSize = BiomeSize.TINY;

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new FrostedChunkGenerator();
    }

    @Override
    public void onEnable() {
        MAIN = this;

        overwriteGenerator(Bukkit.getServer(), "world");
    }

    private static final Class<?> craft_CraftServer;
    private static final Field craft_configuration;

    static {
        craft_CraftServer = Reflections.getNormalCBClass("CraftServer");
        craft_configuration = Reflections.getField(craft_CraftServer, "configuration", true);
    }

    public static void overwriteGenerator(Server server, String world) {
        YamlConfiguration configuration = Reflections.get(craft_configuration, YamlConfiguration.class, craft_CraftServer.cast(server));
        if (configuration.getConfigurationSection("worlds") != null) {
            if (configuration.getString("worlds." + world + ".generator") == null)
                configuration.set("worlds." + world + ".generator", MAIN instanceof AddonMain ? "FrostedEngineering" : "FE-Generator");
        } else
            configuration.set("worlds." + world + ".generator", MAIN instanceof AddonMain ? "FrostedEngineering" : "FE-Generator");

        Reflections.set(craft_configuration, craft_CraftServer.cast(server), configuration);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public JavaPlugin getPlugin() {
        return this;
    }
}
