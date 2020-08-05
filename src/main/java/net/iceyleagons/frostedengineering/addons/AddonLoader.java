package net.iceyleagons.frostedengineering.addons;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import net.iceyleagons.frostedengineering.addons.exception.AddonFolderCreationException;
import net.iceyleagons.frostedengineering.addons.exception.AddonLoadingException;
import net.iceyleagons.frostedengineering.addons.exception.InvalidAddonDescriptionException;

import net.iceyleagons.frostedengineering.Main;

public class AddonLoader {

    public static Map<IAddon, LoadedAddon> addons = new HashMap<>();

    /**
     * @param file is the addon jar file
     * @return the generated {@link LoadedAddon}
     * @throws ClassNotFoundException    if the main class was not found
     * @throws InstantiationException    if the main class of the addon declares
     *                                   theunderlying constructor represents an
     *                                   abstract class.
     * @throws IllegalAccessException    if the addon's {@link Constructor} objectis
     *                                   enforcing Java language access control and
     *                                   the underlyingconstructor is inaccessible.
     * @throws IllegalArgumentException  if the jar file's URL is not absolute
     * @throws InvocationTargetException if the addon's {@link Constructor} throws an exception
     * @throws NoSuchMethodException     if the main class was not found
     * @throws SecurityException         if the file cannot be accessed or if
     *                                   something went wrong with
     *                                   {@link Constructor#newInstance(Object...)}
     * @throws IOException               if something went wrong with the reading of
     *                                   the file
     * @throws InvalidAddonDescriptionException if the addon.json file is not present or malformed or cannot be loaded.
     * @throws AddonLoadingException if there was an error during the loading of the addon.
     */
    public static LoadedAddon getAddon(File file)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException, IOException, InvalidAddonDescriptionException, AddonLoadingException {

        AddonDescription desc = getDescription(file);
        ClassLoader loader = URLClassLoader.newInstance(new URL[]{file.toURI().toURL()},
                AddonLoader.class.getClassLoader());
        Class<?> addonClass = loader.loadClass(desc.getMain());
        // TODO If addon interface is not present
        IAddon addon = (IAddon) addonClass.getDeclaredConstructor().newInstance();
        addon.onLoad();
        return new LoadedAddon(addon, desc);

    }

    /**
     * @param file is (hopefully) the addon jar file
     * @return the generated {@link AddonDescription}
     * @throws InvalidAddonDescriptionException if the addon.json file is not found or malformed or cannot be loaded.
     */
    private static AddonDescription getDescription(File file) throws InvalidAddonDescriptionException, AddonLoadingException {
        JarFile jf = null;
        InputStream is = null;
        try {
            jf = new JarFile(file);
            JarEntry je = jf.getJarEntry("addon.json");

            if (je == null) throw new InvalidAddonDescriptionException(file);

            is = jf.getInputStream(je);
            return new AddonDescription(is,file.getName());
        } catch (IOException ex) {
            throw new InvalidAddonDescriptionException(file);
        } finally {
            if (jf != null)
                try {
                    jf.close();
                } catch (IOException ignore) {}
            if (is != null)
                try {
                    is.close();
                } catch (IOException ignore) {}
        }
    }

    public static void loadAddons() throws AddonFolderCreationException {
        File folder = new File(Main.MAIN.getDataFolder() + File.separator + "addons");
        if (!folder.exists()) {
            if (!folder.mkdir()) {
                throw new AddonFolderCreationException();
            }
        }

        if (folder.isDirectory())
            for (File f : Objects.requireNonNull(folder.listFiles()))
                try {
                    LoadedAddon addon = getAddon(f);
                    Main.info(Optional.of("Addon Loader"),
                            "Loading addon named " + addon.getDescription().getName());
                    addons.put(addon, addon);
                    addon.onLoad();
                    Main.info(Optional.of("Addon Loader"),
                            "Successfully loaded addon named " + addon.getDescription().getName() + "!");
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | IOException | InvalidAddonDescriptionException | AddonLoadingException ex) {
                   ex.printStackTrace();
                }
    }

}
