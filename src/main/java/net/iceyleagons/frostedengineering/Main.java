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
import java.util.Optional;
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
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import dev.arantes.inventorymenulib.listeners.InventoryListener;
import net.iceyleagons.frostedengineering.api.EngineersAPI;
import net.iceyleagons.frostedengineering.api.EngineersAPIProvider;
import net.iceyleagons.frostedengineering.commands.CommandManager;
import net.iceyleagons.frostedengineering.generator.frosted.FrostedDimension;
import net.iceyleagons.frostedengineering.generator.nether.NetherGenerator;
import net.iceyleagons.frostedengineering.gui.CustomCraftingTable;
import net.iceyleagons.frostedengineering.interfaces.ISecond;
import net.iceyleagons.frostedengineering.interfaces.ITick;
import net.iceyleagons.frostedengineering.items.FrostedItems;
import net.iceyleagons.frostedengineering.items.GUIItem;
import net.iceyleagons.frostedengineering.modules.ModuleManager;
import net.iceyleagons.frostedengineering.modules.builtin.ExampleModule;
import net.iceyleagons.frostedengineering.network.energy.EnergyAPI;
import net.iceyleagons.frostedengineering.network.energy.components.sub.consumers.furnace.TexturedFurnace;
import net.iceyleagons.frostedengineering.network.energy.components.sub.generators.coal.TexturedCoalGenerator;
import net.iceyleagons.frostedengineering.network.energy.components.sub.generators.nonthermal.TexturedNonthermalGenerator;
import net.iceyleagons.frostedengineering.network.energy.components.sub.storages.battery.TexturedBatteryStorage;
import net.iceyleagons.frostedengineering.network.energy.components.sub.transformers.lowvoltage.TexturedLowVoltageTransformer;
import net.iceyleagons.frostedengineering.particles.ParticleManager;
import net.iceyleagons.frostedengineering.particles.effects.Effect1;
import net.iceyleagons.frostedengineering.particles.effects.Effect2;
import net.iceyleagons.frostedengineering.particles.effects.Effect3;
import net.iceyleagons.frostedengineering.particles.effects.Effect4;
import net.iceyleagons.frostedengineering.storage.StorageHandler;
import net.iceyleagons.frostedengineering.storage.StorageType;
import net.iceyleagons.frostedengineering.textures.Textures;
import net.iceyleagons.frostedengineering.textures.base.TexturedBase;
import net.iceyleagons.frostedengineering.utils.CustomCrafting;
import net.iceyleagons.frostedengineering.utils.Metrics;
import net.iceyleagons.frostedengineering.utils.RecipeBuilder;

public class Main extends JavaPlugin implements CommandExecutor {
	//8657.866 60 -8089.848 -148.1 -6.1
	public static Main MAIN;
	public static ModuleManager MODULE_MANAGER;
	public static Logger LOGGER;
	public static CommandManager COMMAND_MANAGER;

	public static StorageType CHOOSEN_STORAGE;

	public static int PLUGIN_ID = 6426; //Used for BStats
	public static boolean DEBUG = true;

	public static List<TexturedBase> customBases = new ArrayList<>();
	public static Textures texturesMain;

	public static CustomCraftingTable CUSTOM_CRAFTING_TABLE;
	public static GUIItem GUI_ITEM;

	public static List<ITick> tickListeners = new ArrayList<ITick>();
	public static List<ISecond> secondListeners = new ArrayList<ISecond>();

	//public static CraftingTableDatabase CTD = null;

	/*
	 * Spigot methods
	 */

	@Override
	public void onLoad() {
		//Registry.registerEntities();
		//EnergySaver.saveEnergyNetworks();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {
		MAIN = this;
		LOGGER = this.getLogger();
		printLogo();

		//=-=-=Init stuff=-=-=//
		new EnergyAPI();
		initStorage();
		setupCommands();
		initModules();
		startSchedulers();
		new InventoryListener(MAIN);
		texturesMain = new Textures();
		startDimensionGeneration();
		setupCustomItemsAndBlocks();
		setupCustomItemsAndCrafting();
		StorageHandler.init(MAIN);
		Bukkit.getServer().getPluginManager().registerEvents(new Listeners(this), MAIN);
		//=-=-=Web stuff=-=-=//
		//WebAPI.getAvailableLanguages();
		new Metrics(this, PLUGIN_ID);

		//=-=-=API stuff=-=-=//
		Bukkit.getServicesManager().register(EngineersAPI.class, new EngineersAPIProvider(), MAIN,
				ServicePriority.High);

		//EnergySaver.createFolders();
		ParticleManager.register(new Effect1(1, "Test"));
		ParticleManager.register(new Effect2(2,"Test2"));
		ParticleManager.register(new Effect3(3,"Test3"));
		ParticleManager.register(new Effect4(4,"Test4"));
	}

	@Override
	public void onDisable() {
		//EnergySaver.saveEnergyNetworks();
		texturesMain.onDisable();
	}

	/*
	 * Initializing
	 */

	private void initStorage() {
		CHOOSEN_STORAGE = StorageType.SQLITE;

	}

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

	public WorldEditPlugin getWorldEdit() {
		Plugin p = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		if (p instanceof WorldEditPlugin)
			return (WorldEditPlugin) p;
		return null;
	}

	private void startSchedulers() {
		Bukkit.getScheduler().runTaskTimer(this, () -> {
			tickListeners.forEach(l -> {
				l.onTick();
			});
		}, 0L, 1L);

		Bukkit.getScheduler().runTaskTimer(this, () -> {
			secondListeners.forEach(l -> {
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

	private void setupCustomItemsAndBlocks() {
		customBases.add(new TexturedBatteryStorage());
		customBases.add(new TexturedCoalGenerator());
		customBases.add(new TexturedNonthermalGenerator());
		customBases.add(new TexturedFurnace());
		customBases.add(new TexturedLowVoltageTransformer());
		customBases.add(CUSTOM_CRAFTING_TABLE = new CustomCraftingTable(this));
		customBases.add(GUI_ITEM = new GUIItem(this));
		customBases.forEach((base) -> {
			Textures.register(base);
		});
	}

	private void startDimensionGeneration() {
		Bukkit.getConsoleSender().sendMessage("§e§lWarming up generator for usage.");

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

	public static void info(Optional<String> prefix, String s) {
		if (prefix.isPresent()) {
			Bukkit.getConsoleSender().sendMessage("§b[FrostedEngineering - " + prefix.get() + "] §r" + s);
			return;
		}

		Bukkit.getConsoleSender().sendMessage("§b[FrostedEngineering] §r" + s);
	}

	/*
	 * Debugging
	 */

	public static void debug(String s) {
		if (!DEBUG)
			return;
		Bukkit.broadcastMessage("§b[FrostedEngineering - DEBUG] §r" + s);
	}

	public static void debug(Exception e) {
		if (!DEBUG)
			return;
		Bukkit.broadcastMessage("§b[FrostedEngineering - DEBUG](Exception) §c" + e.getMessage());
		e.printStackTrace();
	}

	/*
	 * ITicks & ISeconds
	 */

	/**
	 * @return the list of {@link ITick}s registered
	 */
	public static List<ITick> getITicks() {
		return tickListeners;
	}

	/**
	 * @return the list of {@link ISecond}s registered
	 */
	public static List<ISecond> getISeconds() {
		return secondListeners;
	}

	/**
	 * Used to register {@link ITick}s to the runnable
	 * 
	 * @param itick is the {@link ITick} to register
	 */
	public static void registerITick(ITick itick) {
		tickListeners.add(itick);
	}

	/**
	 * Used to unregister {@link ITick}s
	 * 
	 * @param itick is the {@link ITick} to unregister
	 */
	public static void unregisterITick(ITick itick) {
		tickListeners.remove(itick);
	}

	/**
	 * Used to register {@link ISecond}s to the runnable
	 * 
	 * @param itick is the {@link ISecond} to register
	 */
	public static void registerISecond(ISecond isecond) {
		secondListeners.add(isecond);
	}

	/**
	 * Used to unregister {@link ITick}s
	 * 
	 * @param itick is the {@link ITick} to unregister
	 */
	public static void unregisterISecond(ISecond isecond) {
		secondListeners.remove(isecond);
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
