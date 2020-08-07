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
package net.iceyleagons.frostedengineering.other;

import java.util.HashMap;
import java.util.Map;

import net.iceyleagons.frostedengineering.storage.yaml.Variables;
import net.md_5.bungee.api.ChatColor;

public class Language {

    private String countryCode, name;
    private String[] translators;
    private Map<String, String> messages = new HashMap<String, String>();

    public Language(String countryCode, String name, String... translators) {
        this.countryCode = countryCode.toLowerCase();
        this.name = name;
        this.translators = translators;
    }

    public Language addMessage(String messageCode, String message) {
        messages.put(messageCode, message);
        return this;
    }

    public Map<String, String> getMessages() {
        return messages;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getName() {
        return name;
    }

    public String[] getTranslator() {
        return translators;
    }


    /*
     * Static
     */

    public static Map<String, Language> languages = new HashMap<String, Language>();

    public static String getMessage(String messageCode) {
        String langcode = Variables.LANG.toLowerCase();

        if (languages.containsKey(langcode)) {
            if (languages.get(langcode).getMessages().containsKey(messageCode)) {
                return ChatColor.translateAlternateColorCodes('&', languages.get(langcode).getMessages().get(messageCode));
            } else {
                return ChatColor.translateAlternateColorCodes('&', languages.get("en").getMessages().get(messageCode));
            }
        } else {
            return ChatColor.translateAlternateColorCodes('&', languages.get("en").getMessages().get(messageCode));
        }
    }

}
