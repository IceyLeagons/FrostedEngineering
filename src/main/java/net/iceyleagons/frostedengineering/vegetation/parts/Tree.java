package net.iceyleagons.frostedengineering.vegetation.parts;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import net.iceyleagons.frostedengineering.vegetation.themes.ITheme;
import net.iceyleagons.frostedengineering.vegetation.themes.OakTheme;

public abstract class Tree implements ITree {

	private static Random rand = new Random();
	protected Location seed;
	protected Vector origin;
	protected ITheme theme = new OakTheme();
	protected World world;
	protected Random random = Tree.rand;
	protected Player planter;

	public Tree(Player planter, Location seed) {
		this.planter = planter;
		this.origin = seed.toVector();
		this.seed = seed;
		this.world = seed.getWorld();
	}

	public boolean hasPlanter() {
		return planter != null;
	}

	public Player getPlanter() {
		return planter;
	}

}