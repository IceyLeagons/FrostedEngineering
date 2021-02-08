package net.iceyleagons.frostedengineering.api.addon;

import net.iceyleagons.frostedengineering.api.IFrostedEngineering;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;

import java.io.File;

/**
 * @author TOTHTOMI
 */
public interface Addon {

    IFrostedEngineering getFrostedEngineering();

    boolean isEnabled();

    AddonLoader getAddonLoader();

    AddonMetadata getAddonMetadata();

    ClassLoader getClassLoader();

    File getDataFolder();

    ChunkGenerator getDefaultChunkGenerator(String worldName, String id);

    void onLoad();

    void onEnable();

    void onDisable();

    void openSettingsMenu(Player player);

    Material getIcon();

}
