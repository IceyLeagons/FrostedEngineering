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

package net.iceyleagons.frostedengineering.network;

import lombok.NonNull;
import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.network.energy.exceptions.UnsupportedUnitType;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Used for merging, splitting {@link Network}s using the Depth First Search algorithm.
 * Due to the probability of heavy calculations it's run asynchronously with {@link java.util.concurrent.ExecutorService}
 *
 * @author TOTHTOMI
 */
public class Tracer {

    /**
     * This will start the tracing, and decide between merging and splitting.
     * It is decided with the {@link Unit#isDestroyed()} boolean, therefore if the Unit got broken
     * set {@link Unit#setDestroyed(boolean)} to true before running this method.
     *
     * @param unit {@link Unit} to start from, basically which got broken/placed
     */
    public static void trace(@NonNull Unit unit) {
        Main.executor.execute(() -> {
            if (unit.isDestroyed())
                traceSplit(unit);
            else
                traceMerge(unit);
        });
    }

    /**
     * This will start the merging process.
     *
     * @param unit {@link Unit} to start from, basically which got placed
     */
    private static void traceMerge(@NonNull Unit unit) {
        List<Unit> connections = dfs(unit);
        Network network = unit.getNetwork().generateSameType();
        connections.forEach(u -> {
            try {
                addToNetwork(u, network);
            } catch (UnsupportedUnitType unsupportedUnitType) {
                unsupportedUnitType.printStackTrace();
            }
            u.unvisit();
        });
    }

    /**
     * This will start the splitting process.
     *
     * @param unit {@link Unit} to start from, basically which got broken
     */
    private static void traceSplit(@NonNull Unit unit) {
        List<Unit> neighbours = unit.getNeighbours();
        for (Unit neighbour : neighbours) {
            List<Unit> connections = dfs(neighbour);
            Network network = unit.getNetwork().generateSameType();
            connections.forEach(u -> {
                try {
                    addToNetwork(u, network);
                } catch (UnsupportedUnitType unsupportedUnitType) {
                    unsupportedUnitType.printStackTrace();
                }
                u.unvisit();
            });
        }
    }

    /**
     * This is a helper function to add Units to networks with one simple method rather than multiple.
     *
     * @param unit {@link Unit} to add
     * @param network {@link Network} to add to
     * @throws UnsupportedUnitType if the provided {@link Network} doesn't support that {@link Unit} ex. you can only add an {@link net.iceyleagons.frostedengineering.network.energy.EnergyUnit} to an {@link net.iceyleagons.frostedengineering.network.energy.EnergyNetwork}
     */
    public static void addToNetwork(@NonNull Unit unit, @NonNull Network network) throws UnsupportedUnitType {
        unit.setNetwork(network);
        network.addUnit(unit);
    }


    /**
     * Depth First Search algorithm implementation using {@link Stack}
     * This will return all the connections that we can use later on.
     *
     * @param unit {@link Unit} to start from
     * @return a List of {@link Unit}s that are connected together in some way.
     */
    private static List<Unit> dfs(@NonNull Unit unit) {
        Stack<Unit> stack = new Stack<>();
        List<Unit> graph = new ArrayList<>();
        stack.add(unit);

        while (!stack.isEmpty()) {
            Unit element = stack.pop();
            if (!element.isVisited()) {
                element.visit();
                graph.add(element);
            }

            List<Unit> neighbours = element.getNeighbours();
            for (Unit n : neighbours) {
                if (n != null && !n.isVisited() && !n.isDestroyed())
                    stack.push(n);
            }
        }
        return graph;
    }


}
