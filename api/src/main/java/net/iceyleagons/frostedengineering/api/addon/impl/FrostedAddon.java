package net.iceyleagons.frostedengineering.api.addon.impl;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.iceyleagons.frostedengineering.api.IFrostedEngineering;
import net.iceyleagons.frostedengineering.api.addon.Addon;
import net.iceyleagons.frostedengineering.api.addon.AddonLoader;
import net.iceyleagons.frostedengineering.api.addon.AddonManager;
import net.iceyleagons.frostedengineering.api.addon.AddonMetadata;
import net.iceyleagons.frostedengineering.api.exceptions.AlreadyRegisteredException;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.concurrent.ExecutorService;

/**
 * Implementation of the {@link Addon} interface
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@EqualsAndHashCode
public abstract class FrostedAddon implements Addon {

    private IFrostedEngineering frostedEngineering;
    private AddonLoader addonLoader;
    private AddonMetadata addonMetadata;
    private ClassLoader classLoader;
    private AddonManager addonManager;

    public void loaderInit(IFrostedEngineering frostedEngineering, AddonManager addonManager, AddonLoader addonLoader, AddonMetadata addonMetadata, ClassLoader classLoader) {
        this.frostedEngineering = frostedEngineering;
        this.addonManager = addonManager;
        this.addonLoader = addonLoader;
        this.addonMetadata = addonMetadata;
        this.classLoader = classLoader;
    }

    protected FrostedAddon() {}

    public void registerListener(Listener listener) {
        frostedEngineering.registerEventListener(listener);
    }

    public ExecutorService getExecutorService() {
        return frostedEngineering.getExecutorService();
    }

    public void registerTickListener(Runnable runnable) throws AlreadyRegisteredException {
        frostedEngineering.getOnTickManager().register(runnable);
    }

    public void registerSecondListener(Runnable runnable) throws AlreadyRegisteredException {
        frostedEngineering.getOnSecondManager().register(runnable);
    }

    @Override
    public abstract Material getIcon();

    @Override
    public abstract void openSettingsMenu(Player player);

    @Override
    public boolean isEnabled() {
        return addonManager.getEnabledAddons().contains(this);
    }

    @Override
    public File getDataFolder() {
        return new File(addonManager.getAddonFolder(), addonMetadata.getName());
    }

    @Override
    public ChunkGenerator getDefaultChunkGenerator(String worldName, String id) {
        return null;
    }

    @Override
    public void onLoad() {}
    @Override
    public void onEnable() {}
    @Override
    public void onDisable() {}

}
