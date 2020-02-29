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

import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

import net.iceyleagons.frostedengineering.Main;

public class DefaultConfig extends Config {
	
	public DefaultConfig(Main m) {
		super(m,"config");
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
