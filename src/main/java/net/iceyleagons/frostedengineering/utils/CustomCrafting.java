package net.iceyleagons.frostedengineering.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.iceyleagons.frostedengineering.gui.InventoryFactory;

public class CustomCrafting {

	private ItemStack inputItem, outputItem, powerItem;
	private Map<String, ItemStack> recipeItems = new HashMap<String, ItemStack>();
	private String[] recipe;

	public CustomCrafting(@Nullable ItemStack inputItem, @Nullable ItemStack outputItem, @Nullable ItemStack powerItem,
			String[] recipe) {
		this.outputItem = outputItem;
		this.inputItem = inputItem;
		this.powerItem = powerItem;
		this.recipe = recipe;
		list.add(this);
	}

	public CustomCrafting addIngredient(char key, ItemStack input) {
		recipeItems.put(String.valueOf(key), input);
		return this;
	}

	private boolean crafted = false;
	public void check(InventoryFactory fac) {
		List<ItemStack> inInventory = new ArrayList<ItemStack>(); // recipe
		ItemStack inputItem = null;
		ItemStack powerItem = null;

		for (int i = 0; i <= 4; i++) {
			for (int j = 2; j <= 6; j++) {
				if (i > 0) {
					inInventory.add(fac.getSourceInventory().getItem(i * 9 + j) != null
							? fac.getSourceInventory().getItem(i * 9 + j)
							: new ItemStack(Material.AIR));
				} else {
					inInventory.add(fac.getSourceInventory().getItem(j) != null ? fac.getSourceInventory().getItem(j)
							: new ItemStack(Material.AIR));
				}
			}
			if (i == 2) {
				inputItem = fac.getSourceInventory().getItem(i * 9);
				// fac.setItem(new ItemStack(Material.AIR), i*9+8);
			}
			if (i == 4) {
				powerItem = fac.getSourceInventory().getItem((i + 1) * 9);
			}
		}

		if (powerItem == null && this.powerItem == null) {
			if (inputItem == null && this.inputItem == null) {
				if (isRecipeSame(inInventory.toArray(new ItemStack[] {}))) {
					if (crafted == false) {
						crafted = true;	
						fac.setItem(outputItem, 26);
					} else {
						if (fac.getSourceInventory().getItem(26) == null) {
							crafted = false;
							clear(fac);
						}
					}
				} else {
					if (crafted==true) {
						fac.removeItem(26);
						crafted = false;
					}
				}
			}
			return;
		}

		if (powerItem.equals(this.powerItem)) {
			if (inputItem.equals(this.inputItem)) {
				if (isRecipeSame(inInventory.toArray(new ItemStack[] {}))) {
					if (crafted == false) {
						crafted = true;	
						fac.setItem(outputItem, 26);
					} else {
						if (fac.getSourceInventory().getItem(26) == null) {
							crafted = false;
							clear(fac);
						}
					}
				} else {
					if (crafted==true) {
						fac.removeItem(26);
						crafted = false;
					}
				}
			}
		}
	}
	
	private void clear(InventoryFactory fac) {
		for (int i = 0; i <= 4; i++) {
			for (int j = 2; j <= 6; j++) {
				if (i > 0) {
					fac.removeItem(i*9+j);
				} else {
					fac.removeItem(j);
				}
			}
			if (i == 2) {
				fac.removeItem(i*9);
			}
			if (i == 4) {
				fac.removeItem((i+1)*9);
			}
		}
	}

	private char[] getRecipeArray() {
		int sumlength = 0;

		for (int i = 0; i < this.recipe.length; i++) {
			sumlength += recipe.length;
		}

		char[] c = new char[sumlength];
		for (int i = 0; i < this.recipe.length; i++) {
			for (int j = 0; j < this.recipe[i].length(); j++) {
				c[i*5+j] = this.recipe[i].charAt(j);
			}
		}

		return c;
	}

	private boolean isRecipeSame(ItemStack[] input) {
		int same = 0;
		char[] c = getRecipeArray();

		for (int i = 0; i < input.length; i++) {
			ItemStack in = input[i];
			ItemStack to = recipeItems.get(String.valueOf(c[i]));
			if (in.equals(to)) {
				same++;
			}
		}
		if (same == 25) {
			return true;
		}
		return false;
	}

	public static List<CustomCrafting> list = new ArrayList<CustomCrafting>();

}
