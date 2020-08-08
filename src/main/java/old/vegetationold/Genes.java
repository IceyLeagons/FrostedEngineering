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
package old.vegetationold;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.iceyleagons.frostedengineering.Main;
import old.vegetationold.genes.ElmGenome;
import old.vegetationold.genes.HickoryGenome;
import old.vegetationold.genes.MagnoliaGenome;
import old.vegetationold.genes.OakGenome;
import old.vegetationold.genes.types.IGenome;
import old.vegetationold.parts.FractalGrowthPattern;
import old.vegetationold.parts.FractalTreeBuilder;
import old.vegetationold.parts.Plant;
import old.vegetationold.themes.DeadTheme;
import old.vegetationold.themes.ITheme;
import old.vegetationold.themes.OakTheme;

public enum Genes {
    Elm(GeneType.TREE, new ElmGenome(), new OakTheme(), 2400L),
    Oak(GeneType.TREE, new OakGenome(), new OakTheme(), 1200L),
    Oak_Dead(GeneType.TREE, new OakGenome(), new DeadTheme(), 900L),
    Hickory_Dead(GeneType.TREE, new HickoryGenome(), new DeadTheme(), 800L),
    Hickory(GeneType.TREE, new HickoryGenome(), new OakTheme(), 600L),
    Magnolia(GeneType.BUSH, new MagnoliaGenome(), new OakTheme(), 300L), Wheat(GeneType.PLANT, 8, 504L);

    public static ConcurrentHashMap<Location, FractalTreeBuilder> saplings = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Location, Plant> plants = new ConcurrentHashMap<>();

    IGenome genome;
    public final GeneType geneType;
    static final Random random = new Random();
    private Genes gene;
    public ITheme theme;
    public final long growTime;
    public int growthStages;

    private Genes(GeneType gT, IGenome genome, ITheme theme, long growTime) {
        this.geneType = gT;
        this.genome = genome;
        this.gene = this;
        this.theme = theme;
        this.growTime = growTime;
    }

    private Genes(GeneType geneType, int growthStages, long growTime) {
        this.geneType = geneType;
        this.growTime = growTime;
        this.growthStages = growthStages;
    }

    public static Genes isSaplingItem(ItemStack item) {
        for (Genes g : values())
            if (g.getItem().getItemMeta().equals(item.getItemMeta()))
                return g;

        return null;
    }

    private static int i = 0;

    public static Genes[] values(GeneType... gT) {
        List<Genes> valuesList = Arrays.asList(Genes.values());
        List<GeneType> geneList = Arrays.asList(gT);

        valuesList.forEach((gene) -> {
            if (geneList.contains(gene.geneType)) {
                i++;
            }
        });

        Genes[] values = new Genes[i];

        i = 0;
        valuesList.forEach(gene -> {
            if (geneList.contains(gene.geneType))
                values[i] = gene;
        });
        i = 0;

        return values;
    }

    public ItemStack getItem() {
        ItemStack item = null;

        if (geneType == GeneType.BUSH || geneType == GeneType.TREE) {
            item = new ItemStack(Material.OAK_SAPLING);
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
        } else {
            item = new ItemStack(Material.WHEAT_SEEDS);
            ItemMeta im = item.getItemMeta();
            im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a" + this.name() + " seeds"));
            im.addEnchant(Enchantment.DURABILITY, 10, true);
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            ArrayList<String> lore = new ArrayList<>();
            lore.add("§7CUSTOM_ITEM");

            im.setLore(lore);
            item.setItemMeta(im);
        }

        return item;
    }

    public void growPlantPhased(Location location, long delay, boolean sound) {
        // We do this async cause it would cause EXTREME lag for the server. (It still does, but it's better now!)

        if (genome != null)
            new FractalGrowthPattern(genome).generateBlueprint().thenAcceptAsync(blueprint -> {
                long timeNow = System.currentTimeMillis();
                FractalTreeBuilder treeBuilder = new FractalTreeBuilder(gene, null, location, blueprint)
                        .setSound(sound);
                saplings.put(location, treeBuilder);
                treeBuilder.growPhased(delay);
                saplings.remove(location);
                Main.debug("Populating chunk with vegetation took " + ((System.currentTimeMillis() - timeNow) / 1000)
                        + " seconds.");
            });

        else {
            Main.debug("Real Plant#grow()");
            new Plant(location, this.growthStages, this, sound).grow();
            Main.debug("Finished.");
        }
    }

    public void growPlant(Location location) {
        // We do this async cause it would cause EXTREME lag for the server. (It still does, but it's better now!)

        if (genome != null)
            new FractalGrowthPattern(genome).generateBlueprint().thenAcceptAsync(blueprint -> {
                new FractalTreeBuilder(gene, null, location, blueprint).growInstant();
            });
        else
            growPlantPhased(location, 0L, false);
    }

    public enum GeneType {
        TREE, PLANT, BUSH;
    }

    public static Genes randomGene() {
        Genes[] genes = Genes.values();

        return genes[random.nextInt(genes.length)];
    }

    public static Genes randomGene(GeneType... gT) {
        Genes[] genes = Genes.values(gT);

        return genes[random.nextInt(genes.length)];
    }
}
