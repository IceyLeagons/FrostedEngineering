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
package net.iceyleagons.frostedengineering.generator.nether;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class LavaPopulator extends BlockPopulator {
    @Override
    public void populate(World world, Random random, Chunk chunk) {
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                int y;
                int z = chunk.getZ() * 16 + j;
                int x = chunk.getX() * 16 + i;
                if (world.getBlockAt(x, 17, z).getType() != Material.LAVA || random.nextInt(16000) >= 5)
                    continue;
                for (y = 60; world.getBlockAt(x, y, z).getType() == Material.AIR && y < 253; ++y) {
                }
                world.getBlockAt(x, y, z).setType(Material.LAVA);
                world.getBlockAt(x, y + 1, z).setType(Material.LAVA);
                world.getBlockAt(x, y + 2, z).setType(Material.LAVA);
                world.getBlockAt(x, y + 3, z).setType(Material.LAVA);
                world.getBlockAt(x, y + 4, z).setType(Material.LAVA);
                world.getBlockAt(x, y + 5, z).setType(Material.LAVA);
                world.getBlockAt(x, y + 6, z).setType(Material.LAVA);
                for (int k = 0; k < y; ++k) {
                    world.getBlockAt(x, k, z).setType(Material.LAVA);
                }
            }
        }
    }
}
