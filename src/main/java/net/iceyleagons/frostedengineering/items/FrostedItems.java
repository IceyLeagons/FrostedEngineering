package net.iceyleagons.frostedengineering.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.iceyleagons.frostedengineering.utils.ItemFactory;
import net.iceyleagons.frostedengineering.utils.RecipeBuilder;
import net.iceyleagons.frostedengineering.utils.RecipeBuilder.RecipeType;

public class FrostedItems {
	
	public static ItemStack STORAGE;
	public static ItemStack INVENTORY_FILLER;
	public static ItemStack CRAFTING_TABLE;
	
	
	public static void init() {
		STORAGE = new ItemFactory(Material.COBBLESTONE).setDisplayName("§b§lStorage - §r§b0 FP")
		.addLoreLine("§fStores: §b0 §fFP")
		.addLoreLine("§fCapacity: §b500 §fFP")
		.addLoreLine(" ")
		.addLoreLine("§7Storage ID §b#0000").build();
		
		CRAFTING_TABLE = new ItemFactory(Material.CRAFTING_TABLE).setDisplayName("§b§lFrosted CraftingTable").build();
		
		ItemStack invf = new ItemStack(Material.DIAMOND_HOE);
		ItemMeta invfm = invf.getItemMeta();
		invfm.setCustomModelData(6784001);
		invf.setItemMeta(invfm);
		INVENTORY_FILLER = invf;
		
		
		
		
		RecipeBuilder.createRecipe().name("test").addIngredient('a', new ItemStack(Material.COBBLESTONE))
		.shape("aaa", "a a", "aaa").type(RecipeType.SHAPED).output(STORAGE.clone())
		.build();
		
		RecipeBuilder.createRecipe().name("frostedcraftingtable")
		.addIngredient('l', new ItemStack(Material.LADDER))
		.addIngredient('c', new ItemStack(Material.CHEST))
		.addIngredient('w',new ItemStack(Material.CRAFTING_TABLE))
		.addIngredient('a', new ItemStack(Material.ANVIL))
		.shape("lcl","lwl","aaa")
		.type(RecipeType.SHAPED).output(CRAFTING_TABLE.clone()).build();
		
	}
	

}
