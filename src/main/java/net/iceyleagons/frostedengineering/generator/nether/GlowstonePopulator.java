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
package net.iceyleagons.frostedengineering.generator.nether;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

import net.iceyleagons.frostedengineering.generator.nether.WorldManager;

public class GlowstonePopulator extends BlockPopulator {
	@Override
	public void populate(World world, Random random, Chunk chunk) {
		for (int i = 0; i < 16; ++i) {
			for (int j = 0; j < 16; ++j) {
				int y;
				int n2 = random.nextInt(4000);
				int z = chunk.getZ() * 16 + j;
				int x = chunk.getX() * 16 + i;
				if (n2 >= 13)
					continue;
				int n5 = random.nextInt(2);
				for (y = 60; world.getBlockAt(x, y, z).getType() == Material.AIR && y < 128; ++y) {
					// Do nothing... let it count up...
				}
				if (n5 == 0) {
					WorldManager.smallVein(world, x, y, z);
					continue;
				}
				if (n5 != 1)
					continue;
				WorldManager.bigVein(world, x, y, z);
			}
		}
	}
}
