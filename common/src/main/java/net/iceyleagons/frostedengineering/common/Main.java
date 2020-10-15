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
package net.iceyleagons.frostedengineering.common;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import inventorymenulib.listeners.InventoryListener;
import net.iceyleagons.frostedengineering.commands.CommandManager;
import net.iceyleagons.frostedengineering.common.addons.AddonLoader;
import net.iceyleagons.frostedengineering.common.addons.AddonManager;
import net.iceyleagons.frostedengineering.common.addons.IAddon;
import net.iceyleagons.frostedengineering.api.addons.exceptions.AddonFolderCreationException;
import net.iceyleagons.frostedengineering.common.generator.frosted.FrostedDimension;
import net.iceyleagons.frostedengineering.common.generator.nether.NetherGenerator;
import net.iceyleagons.frostedengineering.common.gui.CustomCraftingTable;
import net.iceyleagons.frostedengineering.common.items.FrostedItems;
import net.iceyleagons.frostedengineering.common.items.GUIItem;
import net.iceyleagons.frostedengineering.common.network.impl.generators.coal.TexturedCoalGenerator;
import net.iceyleagons.frostedengineering.common.network.impl.storages.battery.TexturedBattery;
import net.iceyleagons.frostedengineering.common.storage.StorageHandler;
import net.iceyleagons.frostedengineering.common.textures.Textures;
import net.iceyleagons.frostedengineering.common.textures.base.TexturedBase;
import net.iceyleagons.frostedengineering.api.ISecond;
import net.iceyleagons.frostedengineering.api.ITick;
import net.iceyleagons.frostedengineering.storage.StorageType;
import net.iceyleagons.frostedengineering.utils.CustomCrafting;
import net.iceyleagons.frostedengineering.utils.InventoryFactory;
import net.iceyleagons.frostedengineering.utils.Metrics;
import net.iceyleagons.frostedengineering.utils.RecipeBuilder;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Main extends JavaPlugin implements CommandExecutor {
    //8657.866 60 -8089.848 -148.1 -6.1
    public static Main MAIN;
    public static Logger LOGGER;
    public static CommandManager COMMAND_MANAGER;
    public static ExecutorService executor = Executors.newCachedThreadPool();

    public static StorageType CHOSEN_STORAGE;

    public static int PLUGIN_ID = 6426; //Used for BStats
    public static boolean DEBUG = true;

    public static List<TexturedBase> customBases = new ArrayList<>();
    public static Textures texturesMain;

    public static CustomCraftingTable CUSTOM_CRAFTING_TABLE;
    public static GUIItem GUI_ITEM;

    public static List<ITick> tickListeners = new ArrayList<>();
    public static List<ISecond> secondListeners = new ArrayList<>();

    public static AddonManager ADDON_MANAGER;

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
        ADDON_MANAGER = new AddonManager();
        printLogo();
        try {
            AddonLoader.loadAddons();
        } catch (AddonFolderCreationException e) {
            e.printStackTrace();
        }

        //=-=-=Init stuff=-=-=//
        initStorage();
        setupCommands();
        startSchedulers();
        new InventoryListener(MAIN);
        texturesMain = new Textures();
        startDimensionGeneration();
        setupCustomItemsAndBlocks();
        setupCustomItemsAndCrafting();
        StorageHandler.init(MAIN);
        InventoryFactory.setup(this);
        Bukkit.getServer().getPluginManager().registerEvents(new Listeners(this), MAIN);
        //=-=-=Web stuff=-=-=//
        //WebAPI.getAvailableLanguages();
        new Metrics(this, PLUGIN_ID);

        //EnergySaver.createFolders();

        AddonLoader.addons.keySet().forEach(addon -> {
            addon.onEnable(ADDON_MANAGER);
        });


        for (IAddon addon : AddonLoader.addons.keySet()) {
            addon.onEnable(ADDON_MANAGER);
        }

    }

    @Override
    public void onDisable() {
        for (IAddon addon : AddonLoader.addons.keySet()) {
            addon.onDisable(ADDON_MANAGER);
        }
        //EnergySaver.saveEnergyNetworks();
        texturesMain.onDisable();
    }

    /*
     * This is used to reduce lag.
     *
     * Ex. The ITick runnables will run on every seconds and not on every ticks
     */
    public static boolean LOW_COMPUTE = false;

    public static void switchToLowComputing() {
        onTick.cancel();
        LOW_COMPUTE = true;

        ////Objects.requireNonNull(Bukkit.getWorld("frosted")).getPlayers().forEach(p -> {
          //  p.teleport(new Location(Bukkit.getWorld("island"), 0, 0, 0)); //Teleport all players in the frosted dimension to the Engineers Island to stop generation
        //});
    }

    public static void restoreNormalComputing() {
        startOnTick();
        LOW_COMPUTE = false;
    }

    /*
     * Initializing
     */

    private void initStorage() {
        CHOSEN_STORAGE = StorageType.SQLITE;

    }

    private void setupCustomItemsAndCrafting() {
        FrostedItems.init();
        new CustomCrafting(null, new ItemStack(Material.DIRT), null,
                new String[]{"lllll", "aaaaa", "aaaaa", "aaaaa", "aaaaa"})
                .addIngredient('l', new ItemStack(Material.LADDER))
                .addIngredient('a', new ItemStack(Material.AIR));
    }


    public WorldEditPlugin getWorldEdit() {
        Plugin p = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        if (p instanceof WorldEditPlugin)
            return (WorldEditPlugin) p;
        return null;
    }

    private static BukkitTask onTick = null;

    private static void startOnTick() {
        onTick = Bukkit.getScheduler().runTaskTimer(Main.MAIN, () -> tickListeners.forEach(ITick::onTick), 0L, 1L);
    }

    private void startSchedulers() {
        startOnTick();

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            secondListeners.forEach(ISecond::onSecond);
            if (LOW_COMPUTE)
                tickListeners.forEach(ITick::onTick);
        }, 0L, 20L);
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, RecipeBuilder::flush);
    }

    private void setupCustomItemsAndBlocks() {
        //customBases.add(new TexturedBatteryStorage());
        //customBases.add(new TexturedCoalGenerator());
        //customBases.add(new TexturedNonthermalGenerator());
        //customBases.add(new TexturedFurnace());
        customBases.add(new TexturedCoalGenerator());
        customBases.add(new TexturedBattery());
        customBases.add(CUSTOM_CRAFTING_TABLE = new CustomCraftingTable(this));
        customBases.add(GUI_ITEM = new GUIItem(this));
        customBases.forEach(Textures::register);
    }

    private void startDimensionGeneration() {
        Bukkit.getConsoleSender().sendMessage("§e§lWarming up generator for usage.");

        Bukkit.getConsoleSender().sendMessage("§e§lGenerating Frosted Dimension, this may take a while");
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
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
        });
    }

    private void setupCommands() {
        COMMAND_MANAGER = new CommandManager(MAIN, "[FrostedEngineering - Help]", "frostedengineering",
                "frostedengineering", "fe");

        List<ClassLoader> cll = new LinkedList<>();
        cll.add(ClasspathHelper.contextClassLoader());
        cll.add(ClasspathHelper.staticClassLoader());

        Reflections r = new Reflections(
                new ConfigurationBuilder().setScanners(new SubTypesScanner(false), new ResourcesScanner())
                        .setUrls(ClasspathHelper.forClassLoader(cll.toArray(new ClassLoader[0])))
                        .filterInputsBy(new FilterBuilder()
                                .include(FilterBuilder.prefix("net.iceyleagons.frostedengineering.commands.cmds"))));
        Set<Class<?>> classes = r.getSubTypesOf(Object.class);
        classes.forEach(clazz -> COMMAND_MANAGER.loadCommandClass(clazz));

        Objects.requireNonNull(getCommand("tpxd")).setExecutor(this);

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

    public static void error(Optional<String> prefix, String s) {
        if (prefix.isPresent()) {
            Bukkit.getConsoleSender().sendMessage("§4[FrostedEngineering - " + prefix.get() + "] §c" + s);
            return;
        }

        Bukkit.getConsoleSender().sendMessage("§4[FrostedEngineering] §c" + s);
    }

    public static void error(Optional<String> prefix, Exception e) {
        if (prefix.isPresent()) {
            Bukkit.getConsoleSender().sendMessage("§4[FrostedEngineering - " + prefix.get() + "] §cEXCEPTION THROWN!");
            if (e.getMessage() != null)
                Bukkit.getConsoleSender()
                        .sendMessage("§4[FrostedEngineering - " + prefix.get() + "] §c" + e.getMessage());
            for (StackTraceElement sTE : e.getStackTrace())
                Bukkit.getConsoleSender()
                        .sendMessage("§4[FrostedEngineering - " + prefix.get() + "] §c" + sTE.toString());
            Bukkit.getConsoleSender().sendMessage("§4[FrostedEngineering - " + prefix.get() + "] §cEND OF STACKTRACE");
            return;
        }

        Bukkit.getConsoleSender().sendMessage("§4[FrostedEngineering] §cEXCEPTION THROWN!");
        if (e.getMessage() != null)
            Bukkit.getConsoleSender().sendMessage("§4[FrostedEngineering - " + prefix.get() + "] §c" + e.getMessage());
        for (StackTraceElement sTE : e.getStackTrace())
            Bukkit.getConsoleSender().sendMessage("§4[FrostedEngineering] §c" + sTE.toString());
        Bukkit.getConsoleSender().sendMessage("§4[FrostedEngineering] §cEND OF STACKTRACE");
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
     * @param isecond is the {@link ISecond} to register
     */
    public static void registerISecond(ISecond isecond) {
        secondListeners.add(isecond);
    }

    /**
     * Used to unregister {@link ITick}s
     *
     * @param isecond is the {@link ISecond} to unregister
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
