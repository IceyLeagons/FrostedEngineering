package net.iceyleagons.frostedengineering.addons;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;

import net.iceyleagons.frostedengineering.Main;

public class AddonManager {

    public AddonManager() {

    }

    public Main getMain() {
        return Main.MAIN;
    }

    public File getAddonDataFolder(IAddon addon) {
        LoadedAddon loadedAddon = AddonLoader.addons.get(addon);
        File addonFolder = new File(Main.MAIN.getDataFolder() + File.separator + "addons" + File.separator + loadedAddon.getDescription().getName());
        if (!addonFolder.exists()) addonFolder.mkdir();
        return addonFolder;
    }

    public Server getServer() {
        return Main.MAIN.getServer();
    }

    public PluginManager getBukkitPluginManager() {
        return Bukkit.getPluginManager();
    }

    public BukkitScheduler getScheduler() {
        return Bukkit.getScheduler();
    }

}
