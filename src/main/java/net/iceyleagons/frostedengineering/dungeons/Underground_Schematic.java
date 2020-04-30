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
package net.iceyleagons.frostedengineering.dungeons;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;

public class Underground_Schematic {

    public class IWorld {
        World world;

        public IWorld(World world) {
            this.world = world;
        }

        public void setBlock(Location loc, Material mat) {
            loc.getBlock().setType(mat);
        }

        public void setBlock(Location loc, BlockData data) {
            loc.getBlock().setBlockData(data);
        }
    }

    public void generate(Location corner) {
        IWorld world = new IWorld(corner.getWorld());

        Location offset = corner.clone();

        for (int y = 1; y <= 7; y++) {
            for (int x = 1; x <= 13; x++) {
                for (int z = 1; z <= 13; z++) {
                    world.setBlock(offset, Material.ANDESITE);
                    switch (y) {
                        case 1: {
                            switch (x) {
                                case 1: {
                                    switch (z) {
                                        case 1:
                                        case 2:
                                        case 3:
                                        case 4:
                                        case 5:
                                            break;
                                        case 6: {
                                            world.setBlock(offset, Material.POLISHED_GRANITE);
                                            break;
                                        }
                                        case 7: {
                                            world.setBlock(offset, Material.POLISHED_GRANITE);
                                            break;
                                        }
                                        case 8: {
                                            world.setBlock(offset, Material.POLISHED_GRANITE);
                                            break;
                                        }
                                        case 9:
                                        case 10:
                                        case 11:
                                        case 12:
                                            break;
                                        case 13: {
                                            offset.add(new Vector(0, 0, -14));
                                        }
                                        default:
                                            break;
                                    }
                                }
                                case 2: {
                                    switch (z) {
                                        case 1:
                                        case 2:
                                        case 3:
                                        case 4: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 5: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 6: {
                                            world.setBlock(offset, Material.POLISHED_GRANITE);
                                            break;
                                        }
                                        case 7: {
                                            world.setBlock(offset, Material.POLISHED_GRANITE);
                                            break;
                                        }
                                        case 8: {
                                            world.setBlock(offset, Material.POLISHED_GRANITE);
                                            break;
                                        }
                                        case 9: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 10: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 11:
                                        case 12:
                                            break;
                                        case 13: {
                                            offset.add(new Vector(0, 0, -14));
                                        }
                                        default:
                                            break;
                                    }
                                }
                                case 3: {
                                    switch (z) {
                                        case 1:
                                        case 2:
                                        case 3: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 4: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 5: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 6: {
                                            world.setBlock(offset, Material.POLISHED_GRANITE);
                                            break;
                                        }
                                        case 7: {
                                            world.setBlock(offset, Material.POLISHED_GRANITE);
                                            break;
                                        }
                                        case 8: {
                                            world.setBlock(offset, Material.POLISHED_GRANITE);
                                            break;
                                        }
                                        case 9: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 10: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 11: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 12:
                                            break;
                                        case 13: {
                                            offset.add(new Vector(0, 0, -14));
                                        }
                                        default:
                                            break;
                                    }
                                }
                                case 4: {
                                    switch (z) {
                                        case 1:
                                        case 2: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 3: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 4: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 5: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 6: {
                                            world.setBlock(offset, Material.POLISHED_GRANITE);
                                            break;
                                        }
                                        case 7: {
                                            world.setBlock(offset, Material.POLISHED_GRANITE);
                                            break;
                                        }
                                        case 8: {
                                            world.setBlock(offset, Material.POLISHED_GRANITE);
                                            break;
                                        }
                                        case 9: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 10: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 11: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 12: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 13: {
                                            offset.add(new Vector(0, 0, -14));
                                        }
                                        default:
                                            break;
                                    }
                                }
                                case 5: {
                                    switch (z) {
                                        case 1:
                                        case 2: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 3: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 4: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 5: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 6: {
                                            world.setBlock(offset, Material.POLISHED_GRANITE);
                                            break;
                                        }
                                        case 7: {
                                            world.setBlock(offset, Material.POLISHED_GRANITE);
                                            break;
                                        }
                                        case 8: {
                                            world.setBlock(offset, Material.POLISHED_GRANITE);
                                            break;
                                        }
                                        case 9: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 10: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 11: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 12: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 13: {
                                            offset.add(new Vector(0, 0, -14));
                                        }
                                        default:
                                            break;
                                    }
                                }
                                case 6: {
                                    world.setBlock(offset, Material.POLISHED_GRANITE);
                                }
                                case 7: {
                                    world.setBlock(offset, Material.POLISHED_GRANITE);
                                }
                                case 8: {
                                    world.setBlock(offset, Material.POLISHED_GRANITE);
                                }
                                case 9: {
                                    switch (z) {
                                        case 1:
                                        case 2: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 3: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 4: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 5: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 6: {
                                            world.setBlock(offset, Material.POLISHED_GRANITE);
                                            break;
                                        }
                                        case 7: {
                                            world.setBlock(offset, Material.POLISHED_GRANITE);
                                            break;
                                        }
                                        case 8: {
                                            world.setBlock(offset, Material.POLISHED_GRANITE);
                                            break;
                                        }
                                        case 9: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 10: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 11: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 12: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 13: {
                                            offset.add(new Vector(0, 0, -14));
                                        }
                                        default:
                                            break;
                                    }
                                }
                                case 10: {
                                    switch (z) {
                                        case 1:
                                        case 2: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 3: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 4: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 5: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 6: {
                                            world.setBlock(offset, Material.POLISHED_GRANITE);
                                            break;
                                        }
                                        case 7: {
                                            world.setBlock(offset, Material.POLISHED_GRANITE);
                                            break;
                                        }
                                        case 8: {
                                            world.setBlock(offset, Material.POLISHED_GRANITE);
                                            break;
                                        }
                                        case 9: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 10: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 11: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 12: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 13: {
                                            offset.add(new Vector(0, 0, -14));
                                        }
                                        default:
                                            break;
                                    }
                                }
                                case 11: {
                                    switch (z) {
                                        case 1:
                                        case 2:
                                        case 3: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 4: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 5: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 6: {
                                            world.setBlock(offset, Material.POLISHED_GRANITE);
                                            break;
                                        }
                                        case 7: {
                                            world.setBlock(offset, Material.POLISHED_GRANITE);
                                            break;
                                        }
                                        case 8: {
                                            world.setBlock(offset, Material.POLISHED_GRANITE);
                                            break;
                                        }
                                        case 9: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 10: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 11: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 12:
                                            break;
                                        case 13: {
                                            offset.add(new Vector(0, 0, -14));
                                        }
                                        default:
                                            break;
                                    }
                                }
                                case 12: {
                                    switch (z) {
                                        case 1:
                                        case 2:
                                        case 3:
                                        case 4: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 5: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 6: {
                                            world.setBlock(offset, Material.POLISHED_GRANITE);
                                            break;
                                        }
                                        case 7: {
                                            world.setBlock(offset, Material.POLISHED_GRANITE);
                                            break;
                                        }
                                        case 8: {
                                            world.setBlock(offset, Material.POLISHED_GRANITE);
                                            break;
                                        }
                                        case 9: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 10: {
                                            world.setBlock(offset, Material.ANDESITE);
                                            break;
                                        }
                                        case 11:
                                        case 12:
                                            break;
                                        case 13: {
                                            offset.add(new Vector(0, 0, -14));
                                        }
                                        default:
                                            break;
                                    }
                                }
                                case 13: {
                                    switch (z) {
                                        case 1:
                                        case 2:
                                        case 3:
                                        case 4:
                                        case 5:
                                            break;
                                        case 6: {
                                            world.setBlock(offset, Material.POLISHED_GRANITE);
                                            break;
                                        }
                                        case 7: {
                                            world.setBlock(offset, Material.POLISHED_GRANITE);
                                            break;
                                        }
                                        case 8: {
                                            world.setBlock(offset, Material.POLISHED_GRANITE);
                                            break;
                                        }
                                        case 9:
                                        case 10:
                                        case 11:
                                        case 12:
                                            break;
                                        case 13: {
                                            offset.add(new Vector(0, 0, -14));
                                        }
                                        default:
                                            break;
                                    }
                                }
                            }
                        }
                        case 2: {

                        }
                    }

                    offset.add(new Vector(1, 0, 0));
                }
                switch (x) {
                    case 13:
                        offset.add(new Vector(-14, 0, 0));
                    default:
                        break;
                }

                offset.add(new Vector(0, 0, 1));
            }
            switch (y) {
                case 13:
                    offset.add(new Vector(0, -14, 0));
                default:
                    break;
            }

            offset.add(new Vector(0, 1, 0));
        }

    }

}
