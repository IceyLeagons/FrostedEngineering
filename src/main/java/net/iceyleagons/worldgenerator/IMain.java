package net.iceyleagons.worldgenerator;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public interface IMain {

    File getDataFolder();

    JavaPlugin getPlugin();

}
