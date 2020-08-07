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

public class Tracer {

    public static void trace(@NonNull Unit unit) {
        Main.executor.execute(() -> {
            if (unit.isDestroyed())
                traceSplit(unit);
            else
                traceMerge(unit);
        });
    }

    private static void traceMerge(@NonNull Unit unit) {
        System.out.println("tracing");
        unit.getNeighbours().forEach(System.out::println);

        List<Unit> connections = dfs(unit);
        connections.forEach(System.out::println);
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

    public static void addToNetwork(@NonNull Unit unit, @NonNull Network network) throws UnsupportedUnitType {
        unit.setNetwork(network);
        network.addUnit(unit);
    }

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
