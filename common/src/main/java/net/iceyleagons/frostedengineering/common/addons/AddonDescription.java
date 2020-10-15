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

package net.iceyleagons.frostedengineering.common.addons;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import net.iceyleagons.frostedengineering.api.addons.exceptions.AddonLoadingException;
import net.iceyleagons.frostedengineering.api.addons.exceptions.InvalidAddonDescriptionException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class AddonDescription {

    private final String name;
    private final String version;
    private final String description;
    private final String main;
    private final String authors;

    public AddonDescription(InputStream is,String fileName) throws InvalidAddonDescriptionException, AddonLoadingException {
        JSONObject obj = getJSONObject(is,fileName);
        name = obj.getString("name");
        version = obj.getString("version");
        description = obj.getString("description");
        main = obj.getString("main");
        authors = obj.getString("authors");

        if (name.isEmpty() || version.isEmpty() || description.isEmpty() || main.isEmpty() || authors.isEmpty())
            throw new InvalidAddonDescriptionException(fileName);


    }

    private JSONObject getJSONObject(InputStream is, String fileName) throws AddonLoadingException {
        JSONParser parser = new JSONParser();
        try (InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            return new JSONObject(((org.json.simple.JSONObject) parser.parse(isr)).toJSONString());
        } catch (IOException | ParseException ex) {
            throw new AddonLoadingException("An exception happened during AddonDescription parsing for addon file " + fileName);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignore) {}
            }
        }
    }

    /**
     * @return the name of the addon, defined in the addon.json file
     */
    public String getName() {
        return name;
    }

    /**
     * @return the version of the addon, defined in the addon.json file
     */
    public String getVersion() {
        return version;
    }

    /**
     * @return the description of the addon, defined in the addon.json file
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the path to the main class of the addon, defined in the addon.json
     * file
     */
    public String getMain() {
        return main;
    }

    /**
     * @return the authors of the addon, defined in the addon.json file
     */
    public String getAuthors() {
        return authors;
    }

}
