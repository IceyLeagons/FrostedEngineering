package net.iceyleagons.frostedengineering.addon;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import net.iceyleagons.frostedengineering.api.IFrostedEngineering;
import net.iceyleagons.frostedengineering.api.addon.Addon;
import net.iceyleagons.frostedengineering.api.addon.AddonManager;
import net.iceyleagons.frostedengineering.api.addon.AddonLoader;
import net.iceyleagons.frostedengineering.api.exceptions.addon.AddonLoadingException;
import net.iceyleagons.frostedengineering.api.exceptions.addon.AlreadyEnabledException;
import net.iceyleagons.frostedengineering.api.exceptions.addon.InvalidAddonException;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Implementation of {@link AddonManager}
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since 1.0.0
 */
@EqualsAndHashCode
public class AddonManagerImpl implements AddonManager {

    private static final Pattern addonFilePattern = Pattern.compile("\\.jar$");

    @Getter
    private final IFrostedEngineering frostedEngineering;
    @Getter
    private final AddonLoader addonLoader;

    private final List<Addon> enabledAddons = new ArrayList<>();
    private final List<Addon> loadedAddons = new ArrayList<>();

    @Getter
    private final File addonFolder;

    public AddonManagerImpl(@NonNull final IFrostedEngineering iFrostedEngineering) {
        this.frostedEngineering = iFrostedEngineering;
        this.addonFolder = new File(iFrostedEngineering.getPlugin().getDataFolder(), "addons");
        this.addonLoader = new AddonLoaderImpl(iFrostedEngineering,this);
        if (!this.addonFolder.exists()) {
            if (!addonFolder.mkdirs()) {
                iFrostedEngineering.getPlugin().getServer().getPluginManager().disablePlugin(iFrostedEngineering.getPlugin());
                iFrostedEngineering.getLogger().severe("Could not create addon folder! Please create it manually! Disabling FrostedEngineering...");
            }
        }
    }

    @Override
    public void loadAddonsInFolder() {
        this.frostedEngineering.getLogger().info("Loading in FrostedEngineering addons...");
        for (File file : Objects.requireNonNull(addonFolder.listFiles())) {
            if (addonFilePattern.matcher(file.getName()).find()) {
                try {
                    Addon loadedAddon = addonLoader.loadAddon(file);
                    loadedAddons.add(loadedAddon);
                } catch (InvalidAddonException | AddonLoadingException e) {
                    this.frostedEngineering.getLogger().severe("Addon file could not be loaded! For cause see the stack trace.");
                    e.printStackTrace();
                }
            } else {
                if (!file.isDirectory())
                    this.frostedEngineering.getLogger().warning("Invalid addon file found in folder! Please consider removing it! ("+file.getName()+")");
            }
        }
        this.frostedEngineering.getLogger().info("Addon loading finished!");
    }

    @Override
    public void enableAddons() {
        for (Addon loadedAddon : loadedAddons) {
            try {
                enableAddon(loadedAddon);
            } catch (AlreadyEnabledException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void disableAddons() {
        for (Addon enabledAddon : new ArrayList<>(enabledAddons)) {
            disableAddon(enabledAddon);
        }
    }

    @Override
    public Optional<Addon> getAddon(String name) {
        for (Addon loadedAddon : loadedAddons) {
            if (loadedAddon.getAddonMetadata().getName().equals(name)) return Optional.of(loadedAddon);
        }
        return Optional.empty();
    }

    @Override
    public void enableAddon(Addon addon) throws AlreadyEnabledException {
        if (addon.isEnabled()) throw new AlreadyEnabledException(addon);

        enabledAddons.add(addon);
        addon.onEnable();
    }

    @Override
    public void disableAddon(Addon addon) {
        if (!addon.isEnabled()) return;

        enabledAddons.remove(addon);
        addon.onDisable();
    }

    @Override
    public Collection<Addon> getEnabledAddons() {
        return this.enabledAddons;
    }

    @Override
    public Collection<Addon> getLoadedAddons() {
        return this.loadedAddons;
    }
}
