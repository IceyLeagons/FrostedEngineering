/*******************************************************************************
 * Copyright (C) 2019 Tóth Tamás and Márton Kissik (https://www.iceyleagons.net/)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package net.iceyleagons.frostedengineering.storage.yaml;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import net.iceyleagons.frostedengineering.Main;

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
