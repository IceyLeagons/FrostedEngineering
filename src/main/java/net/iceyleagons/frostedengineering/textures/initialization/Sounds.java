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

package net.iceyleagons.frostedengineering.textures.initialization;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.annotations.Expose;

import net.iceyleagons.frostedengineering.textures.Textures;

public class Sounds {

    @Expose(serialize = true, deserialize = false)
    public HashMap<String, SoundData> sounds = new HashMap<>();

    private String json;

    public String getJson() {
        return json;
    }

    public Sounds(HashMap<String, SoundData> sounds) {
        this.sounds = sounds;
        this.json = StringUtils.removeEnd(Textures.GSON.toJson(this).replace("\"sounds\": {", ""), "}");
    }

    public static class SoundData {
        @Expose(serialize = true, deserialize = false)
        public String category = "master";
        @Expose(serialize = true, deserialize = false)
        public List<String> sounds;

        public SoundData(String category, List<String> sounds) {
            this.category = category;
            this.sounds = sounds;
        }
    }
}
