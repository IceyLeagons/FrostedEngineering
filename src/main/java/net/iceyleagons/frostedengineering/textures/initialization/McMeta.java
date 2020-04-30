/*******************************************************************************
 * Copyright (C) IceyLeagons(https://iceyleagons.net/) 
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iceyleagons.frostedengineering.textures.initialization;

import com.google.gson.annotations.Expose;

public class McMeta {
    @Expose(serialize = true, deserialize = false)
    public Pack pack;

    public McMeta(int pack_format, String description) {
        this.pack = new Pack(pack_format, description);
    }

    public class Pack {
        @Expose(serialize = true, deserialize = false)
        public int pack_format;
        @Expose(serialize = true, deserialize = false)
        public String description;

        public Pack(int pack, String desc) {
            this.pack_format = pack;
            this.description = desc;
        }
    }
}
