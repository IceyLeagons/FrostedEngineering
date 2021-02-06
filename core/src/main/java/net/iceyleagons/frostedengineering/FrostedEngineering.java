package net.iceyleagons.frostedengineering;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;
import net.iceyleagons.frostedengineering.addon.AddonGUI;
import net.iceyleagons.frostedengineering.addon.AddonManagerImpl;
import net.iceyleagons.frostedengineering.api.IFrostedEngineering;
import net.iceyleagons.frostedengineering.api.addon.Addon;
import net.iceyleagons.frostedengineering.api.addon.AddonManager;
import net.iceyleagons.frostedengineering.api.exceptions.AlreadyRegisteredException;
import net.iceyleagons.frostedengineering.api.exceptions.NotRegisteredException;
import net.iceyleagons.frostedengineering.api.multiblock.MultiblockPattern;
import net.iceyleagons.frostedengineering.api.network.UnitManager;
import net.iceyleagons.frostedengineering.api.other.APIService;
import net.iceyleagons.frostedengineering.api.other.Interactable;
import net.iceyleagons.frostedengineering.api.other.registry.PairRegistry;
import net.iceyleagons.frostedengineering.api.other.registry.Registry;
import net.iceyleagons.frostedengineering.api.textures.TextureProvider;
import net.iceyleagons.frostedengineering.listeners.InteractListener;
import net.iceyleagons.frostedengineering.listeners.MultiblockListener;
import net.iceyleagons.frostedengineering.utils.Metrics;
import net.iceyleagons.icicle.Icicle;
import net.iceyleagons.icicle.IcicleFeatures;
import net.iceyleagons.icicle.misc.ASCIIArt;
import net.iceyleagons.icicle.misc.commands.CommandUtils;
import net.iceyleagons.icicle.time.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of the {@link IFrostedEngineering} interface.
 * The core of FrostedEngineering
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@EqualsAndHashCode
public class FrostedEngineering implements IFrostedEngineering {

    private final String version = "1.0.0";
    private final String license = "Licensed under the terms of SOMETHING";
    private final int bStats_PluginId = 6426;

    private final JavaPlugin plugin;
    private final Logger logger;

    private final ExecutorService executorService;

    private final Registry<Runnable> onTickManager = new Registry<>();
    private final Registry<Runnable> onSecondManager = new Registry<>();

    private final Registry<MultiblockPattern> multiblockPatternRegistry = new Registry<>();

    private final AddonManager addonManager;

    private final PairRegistry<Class<?>, APIService<?>> apis = new PairRegistry<>();
    private final PairRegistry<Location, Interactable> interactablePairRegistry = new PairRegistry<>();

    private boolean debug = false;
    private boolean lowComputing = false;

    public FrostedEngineering(JavaPlugin javaPlugin) {
        this.plugin = javaPlugin;
        this.logger = javaPlugin.getLogger();
        this.executorService = Executors.newCachedThreadPool();
        this.addonManager = new AddonManagerImpl(this);
    }

    @SneakyThrows
    @Override
    public void onLoad() {

        //ASCII Art and Copyright&Version notice
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "\r\n" + ASCIIArt.get("FrostedEngineering", null, getPlugin()));
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Version " + ChatColor.WHITE + version + ChatColor.AQUA + " | " + license);
        Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + Icicle.getCopyrightText());

        //Updater updater = new Updater("asd", getPlugin());
        /*if (updater.checkForUpdates().getType() == Updater.ResponseType.NOT_UP_TO_DATE) {
            logger.warning("There's an update available for FrostedEngineering. " +
                    "If an error occurs make sure to update it first, before you submit an error report! " +
                    "Optionally you can enable auto-update in the config to download and install the update automagically.");
        }
         */
    }

    @Override
    public TextureProvider getTextureProvider() {
        return null;
    }

    @Override
    public UnitManager getUnitManager() {
        //TODO
        return null;
    }

    @Override
    public <T> void registerAPI(Class<T> api, T provider, Addon registrationHolder) throws AlreadyRegisteredException {
        APIService<T> service = new APIService<>(registrationHolder, this, api, provider);
        apis.register(api, service);
        logger.log(Level.INFO, "Registered API {} for holder {} v{}",
                new Object[]{
                        api.getName(),
                        registrationHolder.getAddonMetadata().getName(),
                        registrationHolder.getAddonMetadata().getVersion()});
    }

    @Override
    public <T> Optional<APIService<T>> getAPI(Class<T> api, Addon getter) {
        try {
            APIService<?> service = apis.get(api);
            if (service.getServiceClass().isAssignableFrom(api)) //just making sure we don't throw class cast exception accidentally
                return Optional.of((APIService<T>) service);
            else {
                logger.log(Level.SEVERE, "Addon {} tried to hook into APIService {} v{} but stored value cannot be casted! " +
                                "(Do you have the latest version?)",
                        new Object[]{
                                getter.getAddonMetadata().getName(),
                                getter.getAddonMetadata().getVersion(),
                                api.getName()});
            }
        } catch (NotRegisteredException e) {
            logger.log(Level.WARNING, "Addon {} tried to hook into APIService {} v{} but it is not registered! " +
                            "(Have you installed the required addon dependencies? Is it the latest version?)",
                    new Object[]{
                            getter.getAddonMetadata().getName(),
                            getter.getAddonMetadata().getVersion(),
                            api.getName()});
        }
        return Optional.empty();
    }

    @SneakyThrows
    @Override
    public void onEnable() {
        Icicle.init(getPlugin(), IcicleFeatures.COMMANDS, IcicleFeatures.INVENTORIES);
        setupRunnables();
        registerEventListener(new InteractListener(this));
        registerEventListener(new MultiblockListener(this));
        addonManager.loadAddonsInFolder();

        setupMetrics();
        //AddonGUI must be initialized after addons have been loaded!
        AddonGUI addonGUI = new AddonGUI(this);

        //Registering main command
        Command command = new Command(addonGUI, this);
        CommandUtils.injectCommand("frostedengineering", command, command, "/fe", "Main command for FrostedEngineering", "fe.main",
                "No permission!", Collections.singletonList("fe"));


        //Make sure addons are loaded/enabled lastly to make sure all necessary methods/fields are initialized before
        addonManager.enableAddons();
    }

    private void setupMetrics() {
        logger.info("Settings up Metrics (bStats)");
        executorService.execute(() -> {
            Metrics metrics = new Metrics(getPlugin(), bStats_PluginId);
            metrics.addCustomChart(new Metrics.AdvancedPie("used_addons", () -> {
                Map<String, Integer> valueMap = new HashMap<>();
                getAddonManager().getLoadedAddons().forEach(addon -> valueMap.put(addon.getAddonMetadata().getName(), 1));
                return valueMap;
            }));
        });

    }

    private void setupRunnables() {
        logger.info("Setting up necessary runnables.");
        setupOnTick(); //separate because of low computing switching

        SchedulerUtils.runTaskTimer(getPlugin(), run -> {
            if (lowComputing) getOnTickManager().getRegistered().forEach(Runnable::run);
            getOnSecondManager().getRegistered().forEach(Runnable::run);
        }, 1, TimeUnit.SECONDS);
    }

    private void setupOnTick() {
        SchedulerUtils.runTaskTimer(getPlugin(), run -> {
            if (lowComputing) run.cancel();
            getOnTickManager().getRegistered().forEach(Runnable::run);
        }, 50, TimeUnit.MILLISECONDS);
    }

    @SneakyThrows
    @Override
    public void onDisable() {
        HandlerList.unregisterAll(getPlugin());
        addonManager.disableAddons();
        executorService.shutdown();
    }


    @Override
    public Registry<MultiblockPattern> getMultiblockRegistry() {
        return this.multiblockPatternRegistry;
    }

    @Override
    public PairRegistry<Location, Interactable> getInteractableRegistry() {
        return interactablePairRegistry;
    }

    @Override
    public void registerEventListener(Listener listener) {
        getPlugin().getServer().getPluginManager().registerEvents(listener, getPlugin());
    }

    @Override
    public void unregisterEventListener(Listener listener) {
        HandlerList.unregisterAll(listener);
    }

    @Override
    public void debug(String msg) {
        if (debug) Bukkit.broadcastMessage("§b[FE-DEBUG] §f" + msg);
    }

    @Override
    public void setDebugEnabled(boolean value) {
        this.debug = value;
    }

    @Override
    public boolean isDebuggingEnabled() {
        return debug;
    }

    @Override
    public void setLowComputing(boolean value) {
        String info = value ? "to low computing " : "back to normal computing ";
        logger.info("Switching " + info + " mode!");
        lowComputing = value;
        if (!value) setupOnTick(); //if off then we'll start on tick again
    }

    @Override
    public boolean isLowComputingEnabled() {
        return lowComputing;
    }
}
