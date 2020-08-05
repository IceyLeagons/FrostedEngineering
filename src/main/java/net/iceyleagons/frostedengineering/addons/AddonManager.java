package net.iceyleagons.frostedengineering.addons;

import java.io.File;

import net.iceyleagons.frostedengineering.addons.exception.AddonFolderCreationException;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;

import net.iceyleagons.frostedengineering.Main;

public class AddonManager {

    public AddonManager() {}

    /**
     * @return the {@link Main} of FrostedEngineering
     */
    public Main getMain() {
        return Main.MAIN;
    }

    /**
     *
     * @param addon is the targeted addon
     * @return the DataFolder for the addon
     * @throws AddonFolderCreationException if the addon folder cannot be created.
     */
    public File getAddonDataFolder(IAddon addon) throws AddonFolderCreationException {
        LoadedAddon loadedAddon = AddonLoader.addons.get(addon);
        File addonFolder = new File(Main.MAIN.getDataFolder() + File.separator + "addons" + File.separator + loadedAddon.getDescription().getName());
        if (!addonFolder.exists()) if (!addonFolder.mkdir()) throw new AddonFolderCreationException();
        return addonFolder;
    }

    /**
     * @return the {@link Server}
     */
    public Server getServer() {
        return Main.MAIN.getServer();
    }

    /**
     * @return {@link PluginManager}
     */
    public PluginManager getBukkitPluginManager() {
        return Bukkit.getPluginManager();
    }

    /**
     * @return the {@link BukkitScheduler}
     */
    public BukkitScheduler getScheduler() {
        return Bukkit.getScheduler();
    }

}
