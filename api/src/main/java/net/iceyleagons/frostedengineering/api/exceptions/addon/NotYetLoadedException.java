package net.iceyleagons.frostedengineering.api.exceptions.addon;

import net.iceyleagons.frostedengineering.api.addon.impl.FrostedAddon;

/**
 * @author TOTHTOMI
 */
public class NotYetLoadedException extends RuntimeException {

    public NotYetLoadedException(FrostedAddon frostedAddon) {
        super("Addon named " + frostedAddon.getClass().getName() + " has not been loaded.");
    }

}
