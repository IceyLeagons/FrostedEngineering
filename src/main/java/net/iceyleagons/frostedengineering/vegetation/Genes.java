package net.iceyleagons.frostedengineering.vegetation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.vegetation.genes.ElmGenome;
import net.iceyleagons.frostedengineering.vegetation.genes.HickoryGenome;
import net.iceyleagons.frostedengineering.vegetation.genes.MagnoliaGenome;
import net.iceyleagons.frostedengineering.vegetation.genes.OakGenome;
import net.iceyleagons.frostedengineering.vegetation.genes.types.IGenome;
import net.iceyleagons.frostedengineering.vegetation.parts.FractalGrowthPattern;
import net.iceyleagons.frostedengineering.vegetation.parts.FractalTreeBuilder;
import net.iceyleagons.frostedengineering.vegetation.themes.DeadTheme;
import net.iceyleagons.frostedengineering.vegetation.themes.ITheme;
import net.iceyleagons.frostedengineering.vegetation.themes.OakTheme;

public enum Genes {
	Elm(GeneType.TREE, new ElmGenome(), new OakTheme(), 20), Oak(GeneType.TREE, new OakGenome(), new OakTheme(), 15),
	Oak_Dead(GeneType.TREE, new OakGenome(), new DeadTheme(), 15),
	Hickory_Dead(GeneType.TREE, new HickoryGenome(), new DeadTheme(), 3),
	Hickory(GeneType.TREE, new HickoryGenome(), new OakTheme(), 3),
	Magnolia(GeneType.BUSH, new MagnoliaGenome(), new OakTheme(), 0);

	final IGenome genome;
	final GeneType geneType;
	static final Random random = new Random();
	public final int average;
	private Genes gene;
	public final ITheme theme;

	private Genes(GeneType gT, IGenome genome, ITheme theme, int average) {
		this.geneType = gT;
		this.genome = genome;
		this.average = average;
		this.gene = this;
		this.theme = theme;
	}

	public static Genes isSaplingItem(ItemStack item) {

		for (Genes g : values()) {
			if (g.getItem().getItemMeta().equals(item.getItemMeta())) return g;
		}
		
		return null;
	}

	private static int i = 0;

	public static Genes[] values(GeneType gT) {
		List<Genes> valuesList = Arrays.asList(Genes.values());

		valuesList.forEach((gene) -> {
			if (gene.geneType == gT) {
				i++;
			}
		});

		Genes[] values = new Genes[i];

		i = 0;
		valuesList.forEach((gene) -> {
			if (gene.geneType == gT) {
				values[i] = gene;
			}
		});
		i = 0;

		return values;
	}

	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.OAK_SAPLING);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a" + this.name() + " sapling"));
		im.addEnchant(Enchantment.DURABILITY, 10, true);
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		ArrayList<String> lore = new ArrayList<>();
		lore.add("§7CUSTOM_ITEM");
		lore.add("§6This plant contains:");
		if (genome.getGenes() != null)
			genome.getGenes().forEach((percentage, gene) -> {
				lore.add("§6 - " + percentage * 100 + "% " + gene.name() + " genes.");
			});
		else
			lore.add("§6 - 100% " + gene.name() + " genes.");

		im.setLore(lore);
		item.setItemMeta(im);

		return item;
	}

	@SuppressWarnings("deprecation")
	public void growPlantPhased(Location location, long delay) {
		// We do this async cause it would cause EXTREME lag for the server. (It still does, but it's better now!)

		Bukkit.getScheduler().scheduleAsyncDelayedTask(Main.MAIN, new Runnable() {

			@Override
			public void run() {
				long timeNow = System.currentTimeMillis();
				new FractalTreeBuilder(gene, null, location, new FractalGrowthPattern(genome).generateBlueprint())
						.growPhased(delay);
				Main.debug("Populating chunk with vegetation took " + ((System.currentTimeMillis() - timeNow) / 1000)
						+ " seconds. On average generating a " + gene.toString() + " tree takes about " + average
						+ " seconds.");
			}

		});
	}

	@SuppressWarnings("deprecation")
	public void growPlant(Location location) {
		// We do this async cause it would cause EXTREME lag for the server. (It still does, but it's better now!)

		Bukkit.getScheduler().scheduleAsyncDelayedTask(Main.MAIN, new Runnable() {

			@Override
			public void run() {
				long timeNow = System.currentTimeMillis();
				new FractalTreeBuilder(gene, null, location, new FractalGrowthPattern(genome).generateBlueprint())
						.growInstant();
				Main.debug("Populating chunk with vegetation took " + ((System.currentTimeMillis() - timeNow) / 1000)
						+ " seconds. On average generating a " + gene.toString() + " tree takes about " + average
						+ " seconds.");
			}

		});
	}

	public enum GeneType {
		TREE, PLANT, BUSH;
	}

	public static Genes randomGene() {
		return Genes.values()[random.nextInt(Genes.values().length)];
	}

	public static Genes randomGene(GeneType gT) {
		return Genes.values(gT)[random.nextInt(Genes.values(gT).length)];
	}
}