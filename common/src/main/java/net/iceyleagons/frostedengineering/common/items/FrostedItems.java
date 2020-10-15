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
package net.iceyleagons.frostedengineering.common.items;

import net.iceyleagons.frostedengineering.common.Main;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;



import net.iceyleagons.frostedengineering.utils.ItemFactory;
import net.iceyleagons.frostedengineering.utils.RecipeBuilder;
import net.iceyleagons.frostedengineering.utils.RecipeBuilder.RecipeType;

public class FrostedItems {

    public static ItemStack STORAGE;
    public static ItemStack INVENTORY_FILLER;
    public static ItemStack CRAFTING_TABLE;

    public static void init() {
        /*
         * Storage item
         */
        STORAGE = new ItemFactory(Material.COBBLESTONE).setDisplayName("§b§lStorage - §r§b0 FP")
                .addLoreLine("§fStores: §b0 §fFP").addLoreLine("§fCapacity: §b500 §fFP").addLoreLine(" ")
                .addLoreLine("§7Storage ID §b#0000").build();

        /*
         * Inventory filler  item
         */
        INVENTORY_FILLER = Main.GUI_ITEM.getItem();

        /*
         * Test crafting for Storage
         */

        RecipeBuilder.createRecipe().name("test").addIngredient('a', new ItemStack(Material.COBBLESTONE))
                .shape("aaa", "a a", "aaa").type(RecipeType.SHAPED).output(STORAGE.clone()).build();

        /*
         * Crafting Table item,crafting
         */

        CRAFTING_TABLE = new ItemFactory(Material.CRAFTING_TABLE).setDisplayName("§b§lFrosted Crafting Table").build();
        RecipeBuilder.createRecipe().name("frostedcraftingtable").addIngredient('l', new ItemStack(Material.LADDER))
                .addIngredient('c', new ItemStack(Material.CHEST))
                .addIngredient('w', new ItemStack(Material.CRAFTING_TABLE))
                .addIngredient('a', new ItemStack(Material.ANVIL)).shape("lcl", "lwl", "aaa").type(RecipeType.SHAPED)
                .output(CRAFTING_TABLE.clone()).build();

    }

}
