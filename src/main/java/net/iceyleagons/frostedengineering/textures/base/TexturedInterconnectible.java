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

package net.iceyleagons.frostedengineering.textures.base;

import net.iceyleagons.frostedengineering.textures.Textures;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public abstract class TexturedInterconnectible extends TexturedBlock {

    public enum Direction {
        UP(1, 7), DOWN(2), EAST(3), WEST(4), SOUTH(5), NORTH(6);

        final int[] ids;

        Direction(int... ids) {
            this.ids = ids;
        }

        static Direction getNext(Direction origin) {
            for (Direction value : Direction.values())
                for (int id : value.ids)
                    if (id == origin.ids[0] + 1)
                        return value;

            return null;
        }

        static Direction get(int id) {
            for (Direction value : Direction.values())
                for (int jd : value.ids)
                    if (jd == id)
                        return value;

            return null;
        }
    }

    public TexturedInterconnectible(JavaPlugin plugin, String name, String modelPath, String title, Material baseMaterial) {
        super(plugin, name, modelPath, title, baseMaterial);
    }

    public TexturedInterconnectible(JavaPlugin plugin, String name, String modelPath, String title) {
        super(plugin, name, modelPath, title);
    }

    private HashMap<List<Direction>, TexturedBlock> registerMap = new HashMap<>();

    public Map<List<Direction>, TexturedBlock> getRegisterMap() {
        if (registerMap.isEmpty()) {
            HashMap<List<Direction>, TexturedBlock> finalMap = registerMap;
            for (int j = 0; j < 6; j++)
                for (int i = 0; i < 7; i++) {
                    StringBuilder model = new StringBuilder(getModel());
                    StringBuilder name = new StringBuilder(getName());
                    List<Direction> directions = new ArrayList<>();
                    for (int k = j; k < i; k++) {
                        Direction direction;
                        directions.add(direction = Direction.get(k + 1));
                        assert direction != null : "stupid";
                        name.append("_").append(direction.name().toLowerCase());
                        model.append("_").append(direction.name().toLowerCase());
                    }

                    finalMap.put(directions, new TexturedBlock(getPlugin(), name.toString(), model.toString(), getTitle(), getBaseMaterial()));
                }

            registerMap = finalMap;
        }

        return registerMap;
    }

    boolean compareCollection(Block block, Collection<TexturedBlock> texturedBlocks) {
        return texturedBlocks.contains(Textures.getBlock(block));
    }

    TexturedBlock compareList(Map<List<Direction>, TexturedBlock> directionsMap, List<Direction> directions) {
        for (Map.Entry<List<Direction>, TexturedBlock> entry : directionsMap.entrySet())
            if (entry.getKey().equals(directions))
                return entry.getValue();

        return this;
    }

    List<Direction> a(Block block, Player player) {
        Map<List<Direction>, TexturedBlock> directionVariants = getRegisterMap();
        List<Direction> directions = new ArrayList<>(6);

        Block up = block.getLocation().add(0, 1, 0).getBlock();
        if (compareCollection(up, directionVariants.values()))
            directions.add(Direction.UP);

        Block down = block.getLocation().add(0, -1, 0).getBlock();
        if (compareCollection(down, directionVariants.values()))
            directions.add(Direction.DOWN);

        Block east = block.getLocation().add(1, 0, 0).getBlock();
        if (compareCollection(east, directionVariants.values()))
            directions.add(Direction.EAST);

        Block west = block.getLocation().add(-1, 0, 0).getBlock();
        if (compareCollection(west, directionVariants.values()))
            directions.add(Direction.WEST);

        Block south = block.getLocation().add(0, 0, 1).getBlock();
        if (compareCollection(south, directionVariants.values()))
            directions.add(Direction.SOUTH);

        Block north = block.getLocation().add(0, 0, -1).getBlock();
        if (compareCollection(north, directionVariants.values()))
            directions.add(Direction.NORTH);

        TexturedBlock finalB = compareList(directionVariants, directions);
        finalB.setBlock(block, player);

        return directions;
    }

    @Override
    public void setBlock(Block block, Player player) {
        for (Direction direction : a(block, player))
            switch (direction) {
                case UP:
                    a(block.getLocation().add(0, 1, 0).getBlock(), null);
                    break;
                case DOWN:
                    a(block.getLocation().add(0, -1, 0).getBlock(), null);
                    break;
                case EAST:
                    a(block.getLocation().add(1, 0, 0).getBlock(), null);
                    break;
                case WEST:
                    a(block.getLocation().add(-1, 0, 0).getBlock(), null);
                    break;
                case NORTH:
                    a(block.getLocation().add(0, 0, -1).getBlock(), null);
                    break;
                case SOUTH:
                    a(block.getLocation().add(0, 0, 1).getBlock(), null);
                    break;
            }
    }

    @Override
    public void onBroken(BlockBreakEvent event) {
        Block block = event.getBlock();

        for (Direction direction : Direction.values())
            switch (direction) {
                case UP:
                    a(block.getLocation().add(0, 1, 0).getBlock(), null);
                    break;
                case DOWN:
                    a(block.getLocation().add(0, -1, 0).getBlock(), null);
                    break;
                case EAST:
                    a(block.getLocation().add(1, 0, 0).getBlock(), null);
                    break;
                case WEST:
                    a(block.getLocation().add(-1, 0, 0).getBlock(), null);
                    break;
                case NORTH:
                    a(block.getLocation().add(0, 0, -1).getBlock(), null);
                    break;
                case SOUTH:
                    a(block.getLocation().add(0, 0, 1).getBlock(), null);
                    break;
            }
    }
}
