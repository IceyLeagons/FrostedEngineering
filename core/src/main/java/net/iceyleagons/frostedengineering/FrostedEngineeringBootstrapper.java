package net.iceyleagons.frostedengineering;

import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Bootstrapper for the official {@link net.iceyleagons.frostedengineering.api.IFrostedEngineering} instance
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since 1.0.0
 */
public class FrostedEngineeringBootstrapper extends JavaPlugin {

    private static FrostedEngineering instance = null;

    @Override
    public void onLoad() {
        getLogger().info("Bootstrapping FrostedEngineering Official...");
        if (instance == null) instance = new FrostedEngineering(this);
        instance.onLoad();
    }

    @Override
    public void onDisable() {
        instance.onDisable();
    }

    @Override
    public void onEnable() {
        instance.onEnable();
    }

    @Nullable
    @Override
    public ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String id) {
        return instance.getDefaultWorldGenerator(worldName, id);
    }
}
