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
package net.iceyleagons.frostedengineering.utils;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public class FurnaceRecipeHandler {
	
	public FurnaceRecipe getRecipeFromInput(ItemStack input) {
		Iterator<Recipe> recipes = Bukkit.recipeIterator();
		while(recipes.hasNext()) {
			Recipe recipe = recipes.next();
			
			if (!(recipe instanceof FurnaceRecipe)) continue;
			FurnaceRecipe r = (FurnaceRecipe) recipe;
			
			if (r.getInput().equals(input)) {
				return r;
			}
		}
		return null;
	}

}
