package net.iceyleagons.frostedengineering.api.addon;

import net.iceyleagons.frostedengineering.api.IFrostedEngineering;
import org.bukkit.Material;
import org.bukkit.entity.Player;
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
    void onLoad();
    void onEnable();
    void onDisable();
    void openSettingsMenu(Player player);
    Material getIcon();

}
