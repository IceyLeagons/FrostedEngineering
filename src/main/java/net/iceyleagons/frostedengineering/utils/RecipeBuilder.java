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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import net.iceyleagons.frostedengineering.Main;

public class RecipeBuilder {
	private static List<RecipeBuilder> toBuild = new ArrayList<>();;
	private boolean shapelessRecipe;
	private String name;
	private ItemStack output;
	private HashMap<Character, ItemStack> ingredients;
	private String[] shape;

	/**
	 * Creates a new recipe
	 */
	public static RecipeBuilder createRecipe() {
		return new RecipeBuilder();
	}

	/**
	 * @Deprecated Don't use this! For internal uses only! {@link RecipeBuilder#createRecipe()}.
	 */
	private RecipeBuilder() {
		this.shapelessRecipe = false;
		this.name = "NO NAME SPECIFIED";
		this.output = new ItemStack(Material.BARRIER);
		this.ingredients = new HashMap<>();
		this.shape = new String[0];
	}

	public RecipeBuilder addIngredient(char key, ItemStack item) {
		if (!this.ingredients.containsKey(key)) {
			if (!this.ingredients.containsValue(item)) {
				this.ingredients.put(key, item);
			}
		} else {
			this.ingredients.remove(key);
			this.ingredients.put(key, item);
		}
		return this;
	}

	/**
	 * {@link RecipeBuilder#setOutput(ItemStack)}
	 */
	public RecipeBuilder output(ItemStack output) {
		return this.setOutput(output);
	}

	/**
	 * Sets the result of the crafting recipe.
	 * @param output the itemstack you want to give.
	 */
	public RecipeBuilder setOutput(ItemStack output) {
		this.output = output;
		return this;
	}

	/**
	 * {@link RecipeBuilder#setName(String)}
	 */
	public RecipeBuilder name(String name) {
		return this.setName(name);
	}

	/**
	 * Sets the name of the recipe.
	 * @param name the name in the namespace.
	 */
	public RecipeBuilder setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * This replaces the ingredient map that already exists.
	 * @param ingredientMap the map you with which you wish to replace the current ingredient map.
	 */
	public RecipeBuilder setIngredients(HashMap<Character, ItemStack> ingredientMap) {
		this.ingredients = ingredientMap;
		return this;
	}

	public RecipeBuilder type(RecipeBuilder.RecipeType type) {
		return this.setType(type);
	}

	public RecipeBuilder setType(RecipeBuilder.RecipeType type) {
		switch (type) {
		case SHAPELESS: {
			this.shapelessRecipe = true;
			return this;
		}
		case SHAPED: {
			this.shapelessRecipe = false;
			return this;
		}
		default: {
			return this;
		}
		}
	}

	public RecipeBuilder shape(String... shape) {
		return this.setShape(shape);
	}

	public RecipeBuilder setShape(String... shape) {
		this.shape = shape;
		return this;
	}

	public enum RecipeType {
		SHAPELESS, SHAPED;
	}

	public static List<Recipe> flush() {
		List<Recipe> builtRecipes = new ArrayList<>();
		RecipeBuilder.toBuild.forEach(builder -> builtRecipes.add(new RecipeBuilder.Recipe(builder)));
		Bukkit.getConsoleSender()
				.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cFlushed toBuild, and we now have "
						+ builtRecipes.size() + " recipe" + ((builtRecipes.size() >= 2) ? "s" : "") + "."));
		return builtRecipes;
	}

	public void build() {
		RecipeBuilder.toBuild.add(this);
	}

	public static NamespacedKey newKey(String name) {
		return new NamespacedKey(Main.MAIN, name);
	}

	public static class Recipe {
		public Recipe(RecipeBuilder builder) {
			this(builder.shapelessRecipe, builder.name, builder.output, builder.ingredients, builder.shape);
		}

		public Recipe(boolean shapeless, String name, ItemStack output, HashMap<Character, ItemStack> ingredients,
				String... shape) {
			if (!shapeless) {
				ShapedRecipe a = new ShapedRecipe(RecipeBuilder.newKey(name), output);
				a.shape(shape);
				ingredients.forEach((key, ingredient) -> {
					a.setIngredient(key, ingredient);
				});
				Bukkit.addRecipe(a);
			} else {
				ShapelessRecipe a2 = new ShapelessRecipe(RecipeBuilder.newKey(name), output);
				ingredients.forEach((key, ingredient) -> {
					a2.addIngredient(ingredient);
				});
				Bukkit.addRecipe(a2);
			}
		}
	}
}
