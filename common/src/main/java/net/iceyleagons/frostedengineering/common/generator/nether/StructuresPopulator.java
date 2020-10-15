/*
 *  Copyright (C) IceyLeagons(https://iceyleagons.net/)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package net.iceyleagons.frostedengineering.common.generator.nether;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class StructuresPopulator extends BlockPopulator {
    @Override
    public void populate(World world, Random random, Chunk chunk) {
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                int z = chunk.getZ() * 16 + j;
                int x = chunk.getX() * 16 + i;
                int y = 0;
                y = 0;
                while (world.getBlockAt(x, y, z).getType() != Material.AIR) {
                    ++y;
                }
                if (world.getBlockAt(x, y - 1, z).getType() == Material.NETHERRACK && random.nextInt(12800) == 441)
                    WorldManager.spawn(world, x, y, z);
            }
        }
    }
}
