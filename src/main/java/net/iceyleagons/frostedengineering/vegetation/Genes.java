package net.iceyleagons.frostedengineering.vegetation;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.vegetation.genes.types.ElmGenome;
import net.iceyleagons.frostedengineering.vegetation.genes.types.HickoryGenome;
import net.iceyleagons.frostedengineering.vegetation.genes.types.MagnoliaGenome;
import net.iceyleagons.frostedengineering.vegetation.genes.types.OakGenome;
import net.iceyleagons.frostedengineering.vegetation.genes.types.interfaces.IGenome;
import net.iceyleagons.frostedengineering.vegetation.parts.FractalGrowthPattern;
import net.iceyleagons.frostedengineering.vegetation.parts.FractalTreeBuilder;

public enum Genes {
	GIANT_TREE(GeneType.TREE, new ElmGenome(), 20), MEDIUM_TREE(GeneType.TREE, new OakGenome(), 15),
	SMALL_TREE(GeneType.TREE, new HickoryGenome(), 3), SAPLING(GeneType.TREE, new MagnoliaGenome(), 0);

	final IGenome genome;
	final GeneType geneType;
	static final Random random = new Random();
	public final int average;
	private Genes gene;

	private Genes(GeneType gT, IGenome genome, int average) {
		this.geneType = gT;
		this.genome = genome;
		this.average = average;
		this.gene = this;
	}

	@SuppressWarnings("deprecation")
	public void growPlant(Location location) {
		// We do this async cause it would cause EXTREME lag for the server.

		Bukkit.getScheduler().scheduleAsyncDelayedTask(Main.MAIN, new Runnable() {

			@Override
			public void run() {
				long timeNow = System.currentTimeMillis();
				new FractalTreeBuilder(null, location, new FractalGrowthPattern(genome).generateBlueprint())
						.growInstant();
				Main.debug("Populating chunk with vegetation took " + ((System.currentTimeMillis() - timeNow) / 1000)
						+ " seconds. On average generating a " + gene.toString() + " tree takes about " + average
						+ " seconds.");
			}

		});
	}

	public enum GeneType {
		TREE, PLANT;
	}

	public static Genes randomGene() {
		return Genes.values()[random.nextInt(Genes.values().length)];
	}
}
