package net.iceyleagons.frostedengineering.api.addon;

import net.iceyleagons.frostedengineering.api.IFrostedEngineering;
import net.iceyleagons.frostedengineering.api.exceptions.addon.AlreadyEnabledException;

import java.io.File;
import java.util.Collection;
import java.util.Optional;

/**
 * @author TOTHTOMI
 */
public interface AddonManager {

    /**
     * Will load in the addons in the addons folder specified by the exact implementation of that {@link IFrostedEngineering} instance
     */
    void loadAddonsInFolder();

    /**
     * Will enable all loaded addons
     */
    void enableAddons();

    /**
     * Will disable all enabled addons
     */
    void disableAddons();

    /**
     * Will search for an addon with the given name, if it founds one it will return that,
     * otherwise an empty optional.
     * Addon is not necessarily enabled!
     *
     * @param name the name of the addon
     * @return the found {@link Addon} or empty {@link Optional}
     */
    Optional<Addon> getAddon(String name);

    /**
     * Enables the passed addon
     *
     * @param addon the {@link Addon} to enabled
     * @throws AlreadyEnabledException if the given addon has already been enabled
     */
    void enableAddon(Addon addon) throws AlreadyEnabledException;

    /**
     * Disables the passed addon
     *
     * @param addon the {@link Addon} to disable
     */
    void disableAddon(Addon addon);

    /**
     * @return a collection of enabled {@link Addon}s
     */
    Collection<Addon> getEnabledAddons();

    /**
     * @return a collection of loaded {@link Addon}s
     */
    Collection<Addon> getLoadedAddons();

    /**
     * @return the {@link AddonLoader} implementation used by this instance of {@link AddonManager}
     */
    AddonLoader getAddonLoader();

    /**
     * @return the {@link IFrostedEngineering} instance
     */
    IFrostedEngineering getFrostedEngineering();

    /**
     * @return the folder of all {@link Addon}s specified by the instance of {@link IFrostedEngineering}
     */
    File getAddonFolder();

}
