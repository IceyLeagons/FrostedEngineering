/*******************************************************************************
 * Copyright (C) IceyLeagons(https://iceyleagons.net/) 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iceyleagons.frostedengineering;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import dev.arantes.inventorymenulib.listeners.InventoryListener;
import net.iceyleagons.frostedengineering.api.APIHandler;
import net.iceyleagons.frostedengineering.api.EngineersAPI;
import net.iceyleagons.frostedengineering.commands.CommandManager;
import net.iceyleagons.frostedengineering.energy.network.Unit;
import net.iceyleagons.frostedengineering.entity.Registry;
import net.iceyleagons.frostedengineering.generator.frosted.ChunkData;
import net.iceyleagons.frostedengineering.generator.frosted.FrostedDimension;
import net.iceyleagons.frostedengineering.generator.nether.NetherGenerator;
import net.iceyleagons.frostedengineering.gui.CustomCraftingTable;
import net.iceyleagons.frostedengineering.items.FrostedItems;
import net.iceyleagons.frostedengineering.modules.ModuleManager;
import net.iceyleagons.frostedengineering.modules.builtin.ExampleModule;
import net.iceyleagons.frostedengineering.other.Changelog;
import net.iceyleagons.frostedengineering.storage.StorageManager;
import net.iceyleagons.frostedengineering.storage.StorageType;
import net.iceyleagons.frostedengineering.storage.sql.SQL;
import net.iceyleagons.frostedengineering.storage.sql.types.altypes.craftingtables.CraftingTablesSQLite;
import net.iceyleagons.frostedengineering.storage.yaml.CheatConfig;
import net.iceyleagons.frostedengineering.storage.yaml.Config;
import net.iceyleagons.frostedengineering.storage.yaml.DefaultConfig;
import net.iceyleagons.frostedengineering.utils.CustomCrafting;
import net.iceyleagons.frostedengineering.utils.Metrics;
import net.iceyleagons.frostedengineering.utils.RecipeBuilder;

public class Main extends JavaPlugin implements CommandExecutor {

	public static Main MAIN;
	public static ModuleManager MODULE_MANAGER;
	public static Logger LOGGER;
	public static CommandManager COMMAND_MANAGER;
	public static StorageManager STORAGE_MANAGER;

	private static List<Config> configs = new ArrayList<>();

	public static int PLUGIN_ID = 6426; //Used for BStats
	public static boolean DEBUG = true;
	

	/*
	 * Spigot methods
	 */

	@Override
	public void onLoad() {
		//Registry.registerEntities();
		Registry.load();
	}

	@Override
	public void onEnable() {
		MAIN = this;
		LOGGER = this.getLogger();
		printLogo();

		//=-=-=Init stuff=-=-=//
		initConfigs();
		SQL s = new SQL(this, StorageType.SQLITE);
		s.sqlite.open();
		
		STORAGE_MANAGER = new StorageManager("FrostedEngineering", "Asdf1234", "craftingtables", null, null, StorageType.SQLITE);
		
		setupCommands();
		initModules();
		startSchedulers();
		new Listeners(MAIN);
		new InventoryListener(MAIN);
		setupCustomItemsAndCrafting();
		startDimensionGeneration();

		CustomCraftingTable.loadFromDatabase();
		
		
		//=-=-=Web stuff=-=-=//
		//WebAPI.getAvailableLanguages();
		new Metrics(this, PLUGIN_ID);

		//=-=-=API stuff=-=-=//
		Bukkit.getServicesManager().register(EngineersAPI.class, new APIHandler(), MAIN, ServicePriority.High);

		Changelog.populateBook();
	}

	@Override
	public void onDisable() {

	}

	/*
	 * Initializing
	 */

	private void setupCustomItemsAndCrafting() {
		FrostedItems.init();
		new CustomCrafting(null, new ItemStack(Material.DIRT), null,
				new String[] { "lllll", "aaaaa", "aaaaa", "aaaaa", "aaaaa" })
						.addIngredient('l', new ItemStack(Material.LADDER))
						.addIngredient('a', new ItemStack(Material.AIR));
	}

	private void initModules() {
		MODULE_MANAGER = new ModuleManager();
		MODULE_MANAGER.registerModule(new ExampleModule());
		MODULE_MANAGER.enableModules();

	}

	private void initConfigs() {
		configs.add(new DefaultConfig(MAIN));
		configs.add(new CheatConfig(MAIN));

		configs.forEach(c -> c.init());
	}

	private void startSchedulers() {
		Bukkit.getScheduler().runTaskTimer(this, () -> {
			Unit.tickListeners.forEach(l -> {
				l.onTick();
			});
		}, 0L, 1L);

		Bukkit.getScheduler().runTaskTimer(this, () -> {
			Unit.secondListeners.forEach(l -> {
				l.onSecond();
			});
		}, 0L, 20L);

		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

			@Override
			public void run() {
				RecipeBuilder.flush();
			}

		});
	}

	private void startDimensionGeneration() {
		Bukkit.getConsoleSender().sendMessage("§e§lWarming up generator for usage.");

		for (int i = 0; i < 100; i++) {
			@SuppressWarnings("unused")
			ChunkData tempChunkData = new ChunkData(10, 10, 0L);
			tempChunkData = null;
		}

		Bukkit.getConsoleSender().sendMessage("§e§lGenerating Frosted Dimension, this may take a while");
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

			@Override
			public void run() {
				try {
					WorldCreator wC = new WorldCreator("frosted");
					wC.seed(new Random().nextLong());
					wC.generator(new FrostedDimension());
					wC.type(WorldType.NORMAL);
					wC.environment(Environment.NORMAL);

					wC.createWorld();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
	}

	private void setupCommands() {
		COMMAND_MANAGER = new CommandManager(MAIN, "[FrostedEngineering - Help]", "frostedengineering",
				"frostedengineering", "fe");

		List<ClassLoader> cll = new LinkedList<ClassLoader>();
		cll.add(ClasspathHelper.contextClassLoader());
		cll.add(ClasspathHelper.staticClassLoader());

		Reflections r = new Reflections(
				new ConfigurationBuilder().setScanners(new SubTypesScanner(false), new ResourcesScanner())
						.setUrls(ClasspathHelper.forClassLoader(cll.toArray(new ClassLoader[0])))
						.filterInputsBy(new FilterBuilder()
								.include(FilterBuilder.prefix("net.iceyleagons.frostedengineering.commands.cmds"))));
		Set<Class<?>> classes = r.getSubTypesOf(Object.class);
		classes.forEach(clazz -> {
			COMMAND_MANAGER.loadCommandClass(clazz);
		});

		getCommand("tpxd").setExecutor(this);

	}

	/*
	 * Getters
	 */

	public static Config getConfig(String name) {
		for (Config c : configs)
			if (c.getName().equalsIgnoreCase(name))
				return c;
		return null;
	}

	/*
	 * Logo printing
	 */

	private void printLogo() {
		Bukkit.getConsoleSender().sendMessage(
				"\r\n§b  ______             _           _ ______             _                      _             \r\n"
						+ "§b |  ____|           | |         | |  ____|           (_)                    (_)            \r\n"
						+ "§b | |__ _ __ ___  ___| |_ ___  __| | |__   _ __   __ _ _ _ __   ___  ___ _ __ _ _ __   __ _ \r\n"
						+ "§b |  __| '__/ _ \\/ __| __/ _ \\/ _` |  __| | '_ \\ / _` | | '_ \\ / _ \\/ _ \\ '__| | '_ \\ / _` |\r\n"
						+ "§b | |  | | | (_) \\__ \\ ||  __/ (_| | |____| | | | (_| | | | | |  __/  __/ |  | | | | | (_| |\r\n"
						+ "§b |_|  |_|  \\___/|___/\\__\\___|\\__,_|______|_| |_|\\__, |_|_| |_|\\___|\\___|_|  |_|_| |_|\\__, |\r\n"
						+ "§b                                                 __/ |                                __/ |\r\n"
						+ "§b                                                |___/                                |___/ \r\n\n");
		Bukkit.getConsoleSender().sendMessage(" ");
	}

	/*
	 * Debugging
	 */

	public static void debug(String s) {
		if (!DEBUG)
			return;
		Bukkit.broadcastMessage("§b[FrostedEngineering - DEBUG] §r" + s);
		System.out.println("[FrostedEngineering - DEBUG] " + s);
	}

	public static void debug(Exception e) {
		if (!DEBUG)
			return;
		Bukkit.broadcastMessage("§b[FrostedEngineering - DEBUG](Exception) §c" + e.getMessage());
		System.out.println("[FrostedEngineering - DEBUG](Exception) ");
		e.printStackTrace();
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (arg0 instanceof Player) {
			Player player = (Player) arg0;
			player.teleport(new Location(Bukkit.getWorld(arg3[0]), player.getLocation().getX(),
					player.getLocation().getY(), player.getLocation().getZ()));

			return true;
		}

		return false;
	}

	@Override
	public ChunkGenerator getDefaultWorldGenerator(String string, String string2) {
		return new NetherGenerator();
	}

}
