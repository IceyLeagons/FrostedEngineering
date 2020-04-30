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

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.material.Chest;
import org.bukkit.material.Ladder;

import net.iceyleagons.frostedengineering.utils.PluginUtils;

@SuppressWarnings("deprecation")
public class WorldManager {

    public static boolean roof = true;

    // Glowstone
    public static void smallVein(final World world, final int originX, final int originY, final int originZ) {
        world.getBlockAt(originX, originY, originZ).setType(Material.GLOWSTONE);
        world.getBlockAt(originX, originY, originZ + 1).setType(Material.GLOWSTONE);
        world.getBlockAt(originX + 1, originY, originZ + 1).setType(Material.GLOWSTONE);
        world.getBlockAt(originX + 1, originY, originZ + 2).setType(Material.GLOWSTONE);
        world.getBlockAt(originX + 1, originY + 1, originZ + 1).setType(Material.GLOWSTONE);
        world.getBlockAt(originX + 1, originY + 1, originZ + 2).setType(Material.GLOWSTONE);
        world.getBlockAt(originX + 2, originY, originZ + 2).setType(Material.GLOWSTONE);
        world.getBlockAt(originX + 2, originY + 2, originZ + 1).setType(Material.GLOWSTONE);
    }

    public static void bigVein(final World world, final int originX, final int originY, final int originZ) {
        world.getBlockAt(originX, originY, originZ).setType(Material.GLOWSTONE);
        world.getBlockAt(originX, originY, originZ + 1).setType(Material.GLOWSTONE);
        world.getBlockAt(originX + 1, originY, originZ + 1).setType(Material.GLOWSTONE);
        world.getBlockAt(originX + 1, originY, originZ + 2).setType(Material.GLOWSTONE);
        world.getBlockAt(originX + 1, originY + 1, originZ + 1).setType(Material.GLOWSTONE);
        world.getBlockAt(originX + 1, originY + 1, originZ + 2).setType(Material.GLOWSTONE);
        world.getBlockAt(originX + 2, originY, originZ + 2).setType(Material.GLOWSTONE);
        world.getBlockAt(originX + 2, originY + 2, originZ + 1).setType(Material.GLOWSTONE);
        world.getBlockAt(originX + 2, originY + 2, originZ + 2).setType(Material.GLOWSTONE);
        world.getBlockAt(originX + 2, originY + 2, originZ - 1).setType(Material.GLOWSTONE);
        world.getBlockAt(originX + 2, originY - 2, originZ + 1).setType(Material.GLOWSTONE);
        world.getBlockAt(originX + 2, originY - 1, originZ + 1).setType(Material.GLOWSTONE);
        world.getBlockAt(originX - 1, originY - 2, originZ + 1).setType(Material.GLOWSTONE);
        world.getBlockAt(originX - 2, originY + 2, originZ - 1).setType(Material.GLOWSTONE);
        world.getBlockAt(originX - 2, originY - 2, originZ - 2).setType(Material.GLOWSTONE);
    }

    // Tower
    public static void spawn(final World world, final int originX, int originY, final int originZ) {
        final int offsetY = originY + 13;
        System.out.println("got this far");
        final int randomNumber = PluginUtils.reusableRandom.nextInt(4);
        originY -= 3;
        for (int i = originY - 5; i < offsetY; ++i) {
            world.getBlockAt(originX + 2, i, originZ - 1).setType(Material.NETHER_BRICKS);
            if (i >= originY + 3 && i <= originY + 6) {
                if (randomNumber == 3 && i <= originY + 4) {
                    world.getBlockAt(originX + 2, i, originZ).setType(Material.AIR);
                } else if (randomNumber == 3 || i == originY + 3) {
                    world.getBlockAt(originX + 2, i, originZ).setType(Material.NETHER_BRICKS);
                } else {
                    world.getBlockAt(originX + 2, i, originZ).setType(Material.NETHER_BRICK_FENCE);
                }
            } else {
                world.getBlockAt(originX + 2, i, originZ).setType(Material.NETHER_BRICKS);
            }
            world.getBlockAt(originX + 2, i, originZ + 1).setType(Material.NETHER_BRICKS);
            world.getBlockAt(originX - 2, i, originZ - 1).setType(Material.NETHER_BRICKS);
            if (i >= originY + 3 && i <= originY + 6) {
                if (randomNumber == 2 && i <= originY + 4) {
                    world.getBlockAt(originX - 2, i, originZ).setType(Material.AIR);
                } else if (randomNumber == 2 || i == originY + 3) {
                    world.getBlockAt(originX - 2, i, originZ).setType(Material.NETHER_BRICKS);
                } else {
                    world.getBlockAt(originX - 2, i, originZ).setType(Material.NETHER_BRICK_FENCE);
                }
            } else {
                world.getBlockAt(originX - 2, i, originZ).setType(Material.NETHER_BRICKS);
            }
            world.getBlockAt(originX - 2, i, originZ + 1).setType(Material.NETHER_BRICKS);
            world.getBlockAt(originX - 1, i, originZ + 2).setType(Material.NETHER_BRICKS);
            if (i >= originY + 3 && i <= originY + 6) {
                if (randomNumber == 1 && i <= originY + 4) {
                    world.getBlockAt(originX, i, originZ + 2).setType(Material.AIR);
                } else if (randomNumber == 1 || i == originY + 3) {
                    world.getBlockAt(originX, i, originZ + 2).setType(Material.NETHER_BRICKS);
                } else {
                    world.getBlockAt(originX, i, originZ + 2).setType(Material.NETHER_BRICK_FENCE);
                }
            } else {
                world.getBlockAt(originX, i, originZ + 2).setType(Material.NETHER_BRICKS);
            }
            world.getBlockAt(originX + 1, i, originZ + 2).setType(Material.NETHER_BRICKS);
            world.getBlockAt(originX - 1, i, originZ - 2).setType(Material.NETHER_BRICKS);
            if (i >= originY + 3 && i <= originY + 6) {
                if (randomNumber == 0 && i <= originY + 4) {
                    world.getBlockAt(originX, i, originZ - 2).setType(Material.AIR);
                } else if (randomNumber == 0 || i == originY + 3) {
                    world.getBlockAt(originX, i, originZ - 2).setType(Material.NETHER_BRICKS);
                } else {
                    world.getBlockAt(originX, i, originZ - 2).setType(Material.NETHER_BRICK_FENCE);
                }
            } else {
                world.getBlockAt(originX, i, originZ - 2).setType(Material.NETHER_BRICKS);
            }
            world.getBlockAt(originX + 1, i, originZ - 2).setType(Material.NETHER_BRICKS);
            if (i >= originY + 3) {
                if (i == originY + 8 || i == originY + 14) {
                    if (i == originY + 8) {
                        world.getBlockAt(originX + 1, i, originZ + 1).setType(Material.LADDER);
                        final BlockState a2 = world.getBlockAt(originX + 1, i, originZ + 1).getState();
                        ((Ladder) a2.getData()).setFacingDirection(BlockFace.SOUTH);
                        a2.update(true);
                        world.getBlockAt(originX + 1, i, originZ - 1).setType(Material.LADDER);
                        final BlockState a3 = world.getBlockAt(originX + 1, i, originZ - 1).getState();
                        ((Ladder) a3.getData()).setFacingDirection(BlockFace.SOUTH);
                        a3.update(true);
                    } else {
                        world.getBlockAt(originX + 1, i, originZ + 1).setType(Material.NETHER_BRICKS);
                        world.getBlockAt(originX + 1, i, originZ - 1).setType(Material.NETHER_BRICKS);
                    }
                    world.getBlockAt(originX - 1, i, originZ - 1).setType(Material.NETHER_BRICKS);
                    world.getBlockAt(originX - 1, i, originZ + 1).setType(Material.NETHER_BRICKS);
                    if (i == originY + 14) {
                        final Block block2 = world.getBlockAt(originX - 1, i, originZ);
                        block2.setType(Material.SPAWNER);
                        final CreatureSpawner spawner = (CreatureSpawner) block2.getState();
                        spawner.setSpawnedType(PluginUtils.randomEntity());
                        block2.getState().update();
                    } else {
                        world.getBlockAt(originX - 1, i, originZ).setType(Material.NETHER_BRICKS);
                    }
                    world.getBlockAt(originX + 1, i, originZ).setType(Material.NETHER_BRICKS);
                    world.getBlockAt(originX, i, originZ - 1).setType(Material.NETHER_BRICKS);
                    world.getBlockAt(originX, i, originZ + 1).setType(Material.NETHER_BRICKS);
                    if (i > originY + 8 && i <= originY + 14) {
                        world.getBlockAt(originX, i, originZ).setType(Material.LADDER);
                        final BlockState a4 = world.getBlockAt(originX, i, originZ).getState();
                        ((Ladder) a4.getData()).setFacingDirection(BlockFace.EAST);
                        a4.update(true);
                    } else {
                        world.getBlockAt(originX, i, originZ).setType(Material.NETHER_BRICKS);
                    }
                } else {
                    if (i <= originY + 8 && i >= originY + 3) {
                        world.getBlockAt(originX + 1, i, originZ + 1).setType(Material.LADDER);
                        world.getBlockAt(originX + 1, i, originZ - 1).setType(Material.LADDER);
                        final BlockState a4 = world.getBlockAt(originX + 1, i, originZ - 1).getState();
                        ((Ladder) a4.getData()).setFacingDirection(BlockFace.SOUTH);
                        a4.update(true);
                    } else {
                        world.getBlockAt(originX + 1, i, originZ + 1).setType(Material.AIR);
                        world.getBlockAt(originX + 1, i, originZ - 1).setType(Material.AIR);
                    }
                    if (i == originY + 9) {
                        final Block block2 = world.getBlockAt(originX - 1, i, originZ - 1);
                        final Block block3 = world.getBlockAt(originX - 1, i, originZ + 1);
                        block2.setType(Material.CHEST);
                        final BlockState a2 = block2.getState();
                        ((Chest) a2.getData()).setFacingDirection(BlockFace.EAST);
                        a2.update(true);
                        block3.setType(Material.CHEST);
                        final BlockState a3 = block3.getState();
                        ((Chest) a3.getData()).setFacingDirection(BlockFace.EAST);
                        a3.update(true);
                    } else {
                        world.getBlockAt(originX - 1, i, originZ - 1).setType(Material.AIR);
                        world.getBlockAt(originX - 1, i, originZ + 1).setType(Material.AIR);
                    }
                    if (i >= originY + 8 && i <= originY + 14) {
                        world.getBlockAt(originX - 1, i, originZ).setType(Material.NETHER_BRICKS);
                    } else {
                        world.getBlockAt(originX - 1, i, originZ).setType(Material.AIR);
                    }
                    world.getBlockAt(originX + 1, i, originZ).setType(Material.AIR);
                    world.getBlockAt(originX, i, originZ - 1).setType(Material.AIR);
                    world.getBlockAt(originX, i, originZ + 1).setType(Material.AIR);
                    if (i > originY + 8 && i <= originY + 14) {
                        world.getBlockAt(originX, i, originZ).setType(Material.LADDER);
                        final BlockState a4 = world.getBlockAt(originX, i, originZ).getState();
                        ((Ladder) a4.getData()).setFacingDirection(BlockFace.EAST);
                        a4.update(true);
                    } else {
                        world.getBlockAt(originX, i, originZ).setType(Material.AIR);
                    }
                }
            } else {
                world.getBlockAt(originX + 1, i, originZ + 1).setType(Material.NETHER_BRICKS);
                world.getBlockAt(originX + 1, i, originZ - 1).setType(Material.NETHER_BRICKS);
                world.getBlockAt(originX - 1, i, originZ - 1).setType(Material.NETHER_BRICKS);
                world.getBlockAt(originX - 1, i, originZ + 1).setType(Material.NETHER_BRICKS);
                world.getBlockAt(originX - 1, i, originZ).setType(Material.NETHER_BRICKS);
                world.getBlockAt(originX + 1, i, originZ).setType(Material.NETHER_BRICKS);
                world.getBlockAt(originX, i, originZ - 1).setType(Material.NETHER_BRICKS);
                world.getBlockAt(originX, i, originZ + 1).setType(Material.NETHER_BRICKS);
                if (i == originY + 2) {
                    world.getBlockAt(originX, i, originZ).setType(Material.GLOWSTONE);
                } else {
                    world.getBlockAt(originX, i, originZ).setType(Material.NETHER_BRICKS);
                }
            }
        }
    }
}
