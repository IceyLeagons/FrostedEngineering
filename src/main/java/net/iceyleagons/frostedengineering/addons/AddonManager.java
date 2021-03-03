/*
 *  Copyright (C) IceyLeagons(https://iceyleagons.net/)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

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
