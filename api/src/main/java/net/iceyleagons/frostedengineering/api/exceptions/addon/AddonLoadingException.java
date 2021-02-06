package net.iceyleagons.frostedengineering.api.exceptions.addon;

import java.io.File;

/**
 * @author TOTHTOMI
 */
public class AddonLoadingException extends Exception {

    public AddonLoadingException(File file, Throwable cause) {
        super("Addon " + file.getName() + " failed to load due " + cause.getMessage(), cause);
    }

}
