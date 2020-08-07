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
package net.iceyleagons.frostedengineering.utils.festruct.elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;

/**
 * @author TOTHT
 * <p>
 * <p>
 * This class contains all the information for a structure
 */
public class Data {

    private int rarity = 0;
    private List<String> entities;
    private List<Loot> loots;

    private List<Layer> layers;

    private Map<String, String> materials;

    public Data() {
        entities = new ArrayList<String>();
        loots = new ArrayList<Loot>();
        materials = new HashMap<String, String>();
        layers = new ArrayList<Layer>();
    }

    /**
     * This will return a {@link Loot} from a letter code.
     *
     * @param letter is the letter code.
     * @return the found {@link Loot}
     */
    public Loot getLootFromCode(String letter) {
        for (Loot l : loots) {
            if (l.getLetter().equals(letter)) return l;
        }
        return null;
    }

    /**
     * This will return a {@link BlockData} from a letter code.
     *
     * @param letter is the letter code.
     * @return the found {@link BlockData}
     */
    public BlockData getBlockDataFromCode(String letter) {
        if (materials.get(letter) != null)
            if (materials.get(letter).contains("minecraft")) {
                return Bukkit.createBlockData(materials.get(letter));
            }
        return null;
    }

    /**
     * @return the rarity of the structure
     */
    public int getRarity() {
        return rarity;
    }

    /**
     * @param id is the layer id
     * @return the {@link Layer} with that id
     */
    public Layer getLayer(int id) {
        return this.layers.get(id);
    }

    /**
     * This is used to set the rarity to the desired one
     *
     * @param rarity is the int to set the rarity to
     */
    public void setRarity(int rarity) {
        this.rarity = rarity;
    }

    /**
     * @return the {@link Loot}s that can be found inside the structure
     */
    public List<Loot> getLoots() {
        return loots;
    }

    /**
     * @return the {@link Layer}s of this structure
     */
    public List<Layer> getLayers() {
        return layers;
    }

    /**
     * @return the BlockData(Materials) with their code.
     */
    public Map<String, String> getMaterials() {
        return materials;
    }

    /**
     * @return the {@link EntityType}s that can spawn inside the structure.
     */
    public List<String> getEntities() {
        return entities;
    }

}
