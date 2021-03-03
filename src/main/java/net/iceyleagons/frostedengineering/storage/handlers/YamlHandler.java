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
package net.iceyleagons.frostedengineering.storage.handlers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.storage.yaml.CheatConfig;
import net.iceyleagons.frostedengineering.storage.yaml.Config;
import net.iceyleagons.frostedengineering.storage.yaml.DefaultConfig;

public class YamlHandler {

    private List<Config> configs;
    private Main main;

    public YamlHandler(Main main) {
        configs = new ArrayList<>();
        this.main = main;
        initConfigs();
    }

    private void initConfigs() {
        register(DefaultConfig.class);
        register(CheatConfig.class);
    }

    public void register(Class<? extends Config> clazz) {
        try {
            Constructor<? extends Config> c = clazz.getConstructor(Main.class);
            c.setAccessible(true);
            Config conf = c.newInstance(main);
            configs.add(conf);
            conf.init();
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public Config getConfig(String name) {
        for (Config c : configs)
            if (c.getName().equalsIgnoreCase(name))
                return c;
        return null;
    }


}
