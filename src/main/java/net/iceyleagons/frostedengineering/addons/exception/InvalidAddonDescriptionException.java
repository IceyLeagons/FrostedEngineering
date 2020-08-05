package net.iceyleagons.frostedengineering.addons.exception;

import java.io.File;

public class InvalidAddonDescriptionException extends Exception {

    public InvalidAddonDescriptionException(File file) {
        super("Could not load addon.json for addon file " + file.getName() + " . (Is it a proper addon file?)");
    }

    public InvalidAddonDescriptionException(String fileName) {
        super("Could not load addon.json for addon file " + fileName + " . (Is it a proper addon file?)");
    }

}
