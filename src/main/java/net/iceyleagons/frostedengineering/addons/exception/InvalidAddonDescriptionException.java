/*
 *  Copyright (C) IceyLeagons(https://iceyleagons.net/)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

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
