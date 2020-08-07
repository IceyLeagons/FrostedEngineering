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
package net.iceyleagons.frostedengineering.storage.yaml;

import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

import net.iceyleagons.frostedengineering.Main;

public class DefaultConfig extends Config {

    public DefaultConfig(Main m) {
        super(m, "config");
    }

    @Override
    public YamlConfiguration getConfig() {
        return super.config;
    }

    @Override
    public void init() {
        getConfig().options().header("  ______             _           _ ______             _                      _             \r\n" +
                " |  ____|           | |         | |  ____|           (_)                    (_)            \r\n" +
                " | |__ _ __ ___  ___| |_ ___  __| | |__   _ __   __ _ _ _ __   ___  ___ _ __ _ _ __   __ _ \r\n" +
                " |  __| '__/ _ \\/ __| __/ _ \\/ _` |  __| | '_ \\ / _` | | '_ \\ / _ \\/ _ \\ '__| | '_ \\ / _` |\r\n" +
                " | |  | | | (_) \\__ \\ ||  __/ (_| | |____| | | | (_| | | | | |  __/  __/ |  | | | | | (_| |\r\n" +
                " |_|  |_|  \\___/|___/\\__\\___|\\__,_|______|_| |_|\\__, |_|_| |_|\\___|\\___|_|  |_|_| |_|\\__, |\r\n" +
                "                                                 __/ |                                __/ |\r\n" +
                "                                                |___/                                |___/ ");

        getConfig().addDefault("auto-update", false);
        getConfig().addDefault("lang", "en");
        getConfig().addDefault("allow-cheating", false);


        getConfig().options().copyDefaults(true);
        saveConfig();

        Variables.AUTO_UPDATE = getConfig().getBoolean("auto-update");
        Variables.LANG = getConfig().getString("lang");
        Variables.ALLOW_CHEAT = getConfig().getBoolean("allow-cheating");
    }

    @Override
    public void saveConfig() {
        Main.debug("Saving config " + getName());
        try {
            super.config.save(super.cfile);
        } catch (IOException e) {
            Main.debug("There was an error trying to save config file " + getName());
            Main.debug(e);
        }
    }

    @Override
    public void reloadConfig() {
        Main.debug("Reloading config " + getName());
        super.config = YamlConfiguration.loadConfiguration(super.cfile);
    }

}
