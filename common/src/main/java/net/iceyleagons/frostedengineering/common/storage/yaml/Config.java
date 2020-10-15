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
package net.iceyleagons.frostedengineering.common.storage.yaml;

import java.io.File;

import net.iceyleagons.frostedengineering.common.Main;
import org.bukkit.configuration.file.YamlConfiguration;


public abstract class Config {

    private Main m;
    private String name;
    protected File cfile;
    protected YamlConfiguration config;

    public Config(Main m, String name) {
        this.m = m;
        this.name = name;
        cfile = new File(Main.MAIN.getDataFolder() + File.separator + name + ".yml");
        if (cfile == null) {
            cfile.mkdirs();
        }
        config = YamlConfiguration.loadConfiguration(cfile);
    }

    public String getName() {
        return name;
    }

    public Main getPlugin() {
        return m;
    }

    public abstract void init();

    public abstract YamlConfiguration getConfig();

    public abstract void saveConfig();

    public abstract void reloadConfig();

}
