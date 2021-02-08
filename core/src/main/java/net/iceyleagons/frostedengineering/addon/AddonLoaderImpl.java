package net.iceyleagons.frostedengineering.addon;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import net.iceyleagons.frostedengineering.FrostedEngineering;
import net.iceyleagons.frostedengineering.api.IFrostedEngineering;
import net.iceyleagons.frostedengineering.api.addon.Addon;
import net.iceyleagons.frostedengineering.api.addon.AddonLoader;
import net.iceyleagons.frostedengineering.api.addon.AddonManager;
import net.iceyleagons.frostedengineering.api.addon.AddonMetadata;
import net.iceyleagons.frostedengineering.api.addon.impl.FrostedAddon;
import net.iceyleagons.frostedengineering.api.exceptions.addon.AddonLoadingException;
import net.iceyleagons.frostedengineering.api.exceptions.addon.InvalidAddonException;
import net.iceyleagons.frostedengineering.api.exceptions.addon.MalformedAddonMetadataException;
import net.iceyleagons.icicle.reflect.Reflections;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Implementation of the {@link AddonLoader} interface
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since 1.0.0
 */
@EqualsAndHashCode
public class AddonLoaderImpl implements AddonLoader {

    private final IFrostedEngineering frostedEngineering;
    private final AddonManager addonManager;

    public AddonLoaderImpl(@NonNull final IFrostedEngineering frostedEngineering, @NonNull final AddonManager addonManager) {
        this.frostedEngineering = frostedEngineering;
        this.addonManager = addonManager;
    }

    /**
     * Will attempt to load in an addon.
     *
     * @param file the addon file
     * @return the addon in case of successful loading
     * @throws InvalidAddonException if the file is null, metadata is corrupted or null or the file simply does not exist
     * @throws AddonLoadingException if some sort of java related exception occurs while loading the addon
     */
    @Override
    public Addon loadAddon(File file) throws InvalidAddonException, AddonLoadingException {
        if (file == null) throw new InvalidAddonException("Addon file null");
        if (!file.exists()) throw new InvalidAddonException(file);

        try {
            AddonMetadata addonMetadata = getMetaData(file);

            String authors = addonMetadata.getAuthors() != null ? " by " + String.join(", ", addonMetadata.getAuthors()) : "";
            frostedEngineering.getLogger().info("[FE-ADDONS] Loading in " + addonMetadata.getName() + " v" + addonMetadata.getVersion() + authors);

            ClassLoader classLoader = URLClassLoader.newInstance(new URL[]{file.toURI().toURL()}, getClass().getClassLoader());
            Class<?> jarClass = Class.forName(addonMetadata.getMain(), true, classLoader);
            Class<? extends FrostedAddon> addonClass = jarClass.asSubclass(FrostedAddon.class);
            Method setup = addonClass.getSuperclass().getDeclaredMethod("loaderInit", IFrostedEngineering.class, AddonManager.class,
                    AddonLoader.class, AddonMetadata.class, ClassLoader.class);
            setup.setAccessible(true);

            Addon addon = addonClass.newInstance();
            setup.invoke(addon, frostedEngineering, addonManager, this, addonMetadata, classLoader);
            //TODO load dependencies before loading this addon
            addon.onLoad();

            if (addonMetadata.getGenerator() != null) {
                Method generatorMethod = Reflections.getMethod(addonClass, "getDefaultChunkGenerator", true, String.class, String.class);
                for (String generator : addonMetadata.getGenerator())
                    FrostedEngineering.getGeneratorMap().put(generator, addon, generatorMethod);
            }

            return addon;
        } catch (MalformedAddonMetadataException e) {
            throw new InvalidAddonException(file);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException |
                ClassNotFoundException | NoSuchMethodException | IOException e) {
            throw new AddonLoadingException(file, e);
        }
    }


    /**
     * @param file is (hopefully) the addon jar file
     * @return the generated {@link AddonMetadata}
     * @throws MalformedAddonMetadataException if the addon.json file is not found or malformed
     * @throws IOException                     if a java related error occurs
     */
    private static AddonMetadata getMetaData(@NonNull File file) throws MalformedAddonMetadataException, IOException, IllegalAccessException {
        try (JarFile jarFile = new JarFile(file)) {
            JarEntry jarEntry = jarFile.getJarEntry("addon.json");
            if (jarEntry == null || jarEntry.isDirectory()) throw new MalformedAddonMetadataException(file);

            return new AddonMetadata(jarFile.getInputStream(jarEntry), file);
        }
    }
}
