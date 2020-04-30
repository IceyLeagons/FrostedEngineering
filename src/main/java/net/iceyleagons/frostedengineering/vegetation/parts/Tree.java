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
package net.iceyleagons.frostedengineering.vegetation.parts;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import net.iceyleagons.frostedengineering.vegetation.Genes;
import net.iceyleagons.frostedengineering.vegetation.themes.ITheme;

public abstract class Tree implements ITree {

    private static Random rand = new Random();
    protected Location seed;
    protected Vector origin;
    protected ITheme theme;
    protected World world;
    protected Random random = Tree.rand;
    protected Player planter;

    public Tree(Genes gene, Player planter, Location seed) {
        this.planter = planter;
        this.origin = seed.toVector();
        this.seed = seed;
        this.world = seed.getWorld();
        this.theme = gene.theme;
    }

    public boolean hasPlanter() {
        return planter != null;
    }

    public Player getPlanter() {
        return planter;
    }

}
