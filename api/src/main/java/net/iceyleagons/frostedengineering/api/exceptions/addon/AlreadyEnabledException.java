package net.iceyleagons.frostedengineering.api.exceptions.addon;

import net.iceyleagons.frostedengineering.api.addon.Addon;

/**
 * @author TOTHTOMI
 */
public class AlreadyEnabledException extends Exception {

    public AlreadyEnabledException(Addon addon) {
        super("An instance of addon named " + addon.getAddonMetadata().getName() + " has already been enabled!");
    }
}
