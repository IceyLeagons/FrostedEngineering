package net.iceyleagons.frostedengineering.api.exceptions.addon;

import java.io.File;

/**
 * @author TOTHTOMI
 */
public class InvalidAddonException extends Exception {

    public InvalidAddonException(File file) {
        super("Addon: " + file + " is not a valid addon file!");
    }
    public InvalidAddonException(String msg) { super(msg); }

}
