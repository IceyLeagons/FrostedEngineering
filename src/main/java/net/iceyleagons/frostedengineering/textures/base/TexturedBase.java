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
package net.iceyleagons.frostedengineering.textures.base;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class TexturedBase {

    private Material baseMaterial;
    private String name;
    private String modelPath;
    private Plugin plugin;
    private int id;

    /**
     * Creates a new TexturedBase instance
     *
     * @param plugin       the plugin which contains the assets.zip
     * @param name         the name of the base (used in give)
     * @param modelPath    the path to the model in assets.zip
     * @param baseMaterial the base material to "extend" upon
     */
    public TexturedBase(JavaPlugin plugin, String name, String modelPath, Material baseMaterial) {
        this.baseMaterial = baseMaterial;
        this.name = name;
        this.modelPath = modelPath;
        this.plugin = plugin;
    }

    /**
     * Gets the name used in the give command
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the id of the base
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the new id of the base
     *
     * @param id
     * @deprecated for internal use only.
     */
    @Deprecated
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the model path of the base
     *
     * @return the model path
     */
    public String getModel() {
        return modelPath;
    }

    /**
     * Gets the base material of the base
     *
     * @return the base material
     */
    public Material getBaseMaterial() {
        return baseMaterial;
    }

    /**
     * Gets the registrar plugin
     *
     * @return the plugin with which this TexturedBase was registered
     */
    public Plugin getPlugin() {
        return plugin;
    }

    public ItemStack getItem() {
        return null;
    }

    public Recipe getRecipe() {
        return null;
    }

}
