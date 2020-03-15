package net.iceyleagons.frostedengineering.storage.yaml.copy;

import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

import net.iceyleagons.frostedengineering.Main;

public class CheatConfig extends Config {
	
	public CheatConfig(Main m) {
		super(m,"cheats");
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
				"                                                |___/                                |___/ \r\n\n"
				+ "NOTE that these options will only work if you've enabled cheating in config.yml!");
		
		getConfig().addDefault("can-cables-shock", true);
		getConfig().addDefault("can-cables-melt", true);
		getConfig().addDefault("can-consumers-explode", true);
		getConfig().addDefault("can-generators-overheat", true);
		getConfig().addDefault("allow-creative-mode", true);
		
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		Variables.CAN_CABLES_SHOCK = getConfig().getBoolean("can-cables-shock");
		Variables.CAN_CABLES_MELT = getConfig().getBoolean("can-cables-melt");
		Variables.CAN_CONSUMERS_EXPLODE = getConfig().getBoolean("can-consumers-explode");
		Variables.CAN_GENERATORS_OVERHEAT = getConfig().getBoolean("can-generators-overheat");
		Variables.ALLOW_CREATIVE_MODE = getConfig().getBoolean("allow-creative-mode");
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
