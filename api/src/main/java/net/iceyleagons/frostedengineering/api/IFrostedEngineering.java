package net.iceyleagons.frostedengineering.api;

import net.iceyleagons.frostedengineering.api.addon.Addon;
import net.iceyleagons.frostedengineering.api.addon.AddonManager;
import net.iceyleagons.frostedengineering.api.exceptions.AlreadyRegisteredException;
import net.iceyleagons.frostedengineering.api.multiblock.MultiblockPattern;
import net.iceyleagons.frostedengineering.api.network.UnitManager;
import net.iceyleagons.frostedengineering.api.other.APIService;
import net.iceyleagons.frostedengineering.api.other.Interactable;
import net.iceyleagons.frostedengineering.api.other.registry.PairRegistry;
import net.iceyleagons.frostedengineering.api.other.registry.Registry;
import net.iceyleagons.frostedengineering.api.textures.ITextureProvider;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

/**
 * FrostedEngineering main interface
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since 1.0.0
 */
public interface IFrostedEngineering {

    /**
     * @return the version of the registered {@link IFrostedEngineering} instance
     */
    String getVersion();

    /**
     * @return the {@link JavaPlugin} of the registered {@link IFrostedEngineering} instance
     */
    JavaPlugin getPlugin();

    /**
     * @param worldName name of the world
     * @param id        id of the world
     * @return the {@link ChunkGenerator} for the world.
     */
    ChunkGenerator getDefaultWorldGenerator(String worldName, String id);

    /**
     * @return an instance of {@link Registry} responsible for the tick runnables
     */
    Registry<Runnable> getOnTickManager();

    /**
     * @return an instance of {@link Registry} responsible for the second runnables
     */
    Registry<Runnable> getOnSecondManager();

    /**
     * @return an instance of {@link Registry} responsible for containing all registered {@link MultiblockPattern}s
     */
    Registry<MultiblockPattern> getMultiblockRegistry();

    PairRegistry<Location, Interactable> getInteractableRegistry();

    /**
     * @return the {@link Logger} used by Bukkit
     */
    Logger getLogger();

    /**
     * Registers a {@link Listener} to the FrostedEngineering instance
     *
     * @param listener the listener to register
     */
    void registerEventListener(Listener listener);

    /**
     * Unregisters a {@link Listener} from the FrostedEngineering instance
     *
     * @param listener the listener to unregister
     */
    void unregisterEventListener(Listener listener);

    /**
     * Prints out a debug message with {@link org.bukkit.Bukkit#broadcastMessage(String)} if debug is enabled
     *
     * @param msg the message
     */
    void debug(String msg);

    /**
     * Sets the debug mode state
     *
     * @param value the value to set to
     */
    void setDebugEnabled(boolean value);

    /**
     * Sets the low computing state
     *
     * @param value the value to set to
     */
    void setLowComputing(boolean value);

    /**
     * @return true if low computing is enabled
     */
    boolean isLowComputingEnabled();


    /**
     * @return true if debugging is enabled
     */
    boolean isDebuggingEnabled();

    /**
     * Check {@link JavaPlugin#onEnable()}
     */
    void onEnable();

    /**
     * Check {@link JavaPlugin#onDisable()}
     */
    void onDisable();

    /**
     * Check {@link JavaPlugin#onLoad()}
     */
    void onLoad();

    /**
     * @return the {@link AddonManager} implementation used by this instance of {@link IFrostedEngineering}
     */
    AddonManager getAddonManager();

    /**
     * @return the {@link ITextureProvider} implementation used by this instance of {@link IFrostedEngineering}
     */
    ITextureProvider getTextureProvider();

    /**
     * @return the {@link ExecutorService} used by this instance of {@link IFrostedEngineering}
     */
    ExecutorService getExecutorService();

    /**
     * @return the {@link UnitManager} used by this instance of {@link IFrostedEngineering}
     */
    UnitManager getUnitManager();

    /**
     * Registers an API Service
     *
     * @param api                the API interface class
     * @param provider           the provider of such interface
     * @param registrationHolder the registration holder {@link Addon}
     * @param <T>                the API interface
     * @throws AlreadyRegisteredException if an API with that interface has been already registered once
     */
    <T> void registerAPI(Class<T> api, T provider, Addon registrationHolder) throws AlreadyRegisteredException;

    /**
     * Returns an {@link APIService} for the provided interface class if it has been registered, if not it
     * will throw an exception.
     *
     * @param api the API interface class
     * @param <T> the API interface
     * @return the {@link APIService} if there's any or an empty {@link java.util.Optional}
     */
    <T> Optional<APIService<T>> getAPI(Class<T> api, Addon getter);


}
