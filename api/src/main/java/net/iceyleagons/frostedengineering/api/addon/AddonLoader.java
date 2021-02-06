package net.iceyleagons.frostedengineering.api.addon;

import net.iceyleagons.frostedengineering.api.exceptions.addon.AddonLoadingException;
import net.iceyleagons.frostedengineering.api.exceptions.addon.InvalidAddonException;

import java.io.File;

/**
 * @author TOTHTOMI
 */
public interface AddonLoader {

    Addon loadAddon(File file) throws InvalidAddonException, AddonLoadingException;

}
