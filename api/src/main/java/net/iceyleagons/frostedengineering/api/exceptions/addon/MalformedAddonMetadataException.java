package net.iceyleagons.frostedengineering.api.exceptions.addon;

import java.io.File;

/**
 * @author TOTHTOMI
 */
public class MalformedAddonMetadataException extends Exception {

    public MalformedAddonMetadataException(File file) {
        super("Addon metadata for addon " + file.getName() + " is malformed. (Did you include it?)");
    }

}
