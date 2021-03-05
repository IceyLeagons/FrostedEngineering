package net.iceyleagons.dynamicserver;

import lombok.SneakyThrows;
import net.iceyleagons.frostedengineering.api.addon.impl.FrostedAddon;
import net.iceyleagons.icicle.wrapped.WrappedDedicatedServer;
import net.iceyleagons.icicle.wrapped.WrappedPlayerList;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public class DynamicServer extends FrostedAddon {
    int preferredRenderDistance;
    int minimumRenderDistance;
    int tpsThreshold;
    int stepSize;

    WrappedDedicatedServer wrappedDedicatedServer;
    WrappedPlayerList wrappedPlayerList;
    BukkitRunnable runnable;

    @Override
    public void onEnable() {
        if (runnable != null)
            return;

        runnable = new BukkitRunnable() {
            @SneakyThrows
            @Override
            public void run() {
                wrappedDedicatedServer = WrappedDedicatedServer.from(getFrostedEngineering().getPlugin().getServer());
                wrappedPlayerList = wrappedDedicatedServer.getPlayerList();

                File configFile = new File(getDataFolder(), "config.yml");
                YamlConfiguration configuration = YamlConfiguration.loadConfiguration(configFile);

                if (!configuration.isSet("preferred-render-distance"))
                    try {
                        configuration.set("preferred-render-distance", wrappedPlayerList.getViewDistance());
                    } catch (NullPointerException exception) {
                        configuration.set("preferred-render-distance", 10);
                    }
                if (!configuration.isSet("minimum-render-distance"))
                    configuration.set("minimum-render-distance", 3);
                if (!configuration.isSet("tps-threshold"))
                    configuration.set("tps-threshold", 16);
                if (!configuration.isSet("step-size"))
                    configuration.set("step-size", 1);

                preferredRenderDistance = configuration.getInt("preferred-render-distance");
                minimumRenderDistance = configuration.getInt("minimum-render-distance");
                tpsThreshold = configuration.getInt("tps-threshold");
                stepSize = configuration.getInt("step-size");

                configuration.save(configFile);

                // Every 10 seconds we will check the tps, and update it accordingly.
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {
                            if (wrappedDedicatedServer.getTPS(WrappedDedicatedServer.TPSTime.LAST_MINUTE) < tpsThreshold)
                                wrappedPlayerList.setViewDistance(Math.max(wrappedPlayerList.getViewDistance() - stepSize, minimumRenderDistance));
                            else
                                wrappedPlayerList.setViewDistance(Math.min(wrappedPlayerList.getViewDistance() + stepSize, preferredRenderDistance));
                        } catch (NullPointerException ignored) {
                            // Ignored.

                            wrappedDedicatedServer = WrappedDedicatedServer.from(getFrostedEngineering().getPlugin().getServer());
                            wrappedPlayerList = wrappedDedicatedServer.getPlayerList();
                        }
                    }
                }.runTaskTimer(getFrostedEngineering().getPlugin(), 200L, 200L);
            }
        };

        runnable.runTask(getFrostedEngineering().getPlugin());

        super.onEnable();
    }

    @Override
    public Material getIcon() {
        return Material.COMMAND_BLOCK;
    }

    @Override
    public void openSettingsMenu(Player player) {

    }
}
