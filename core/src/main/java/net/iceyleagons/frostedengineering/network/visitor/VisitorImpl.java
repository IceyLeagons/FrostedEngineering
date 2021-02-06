package net.iceyleagons.frostedengineering.network.visitor;

import net.iceyleagons.frostedengineering.api.network.visitor.Visitable;
import net.iceyleagons.frostedengineering.api.network.visitor.Visitor;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * @author TOTHTOMI
 */
public class VisitorImpl implements Visitor {

    @Override
    public Set<Visitable> findConnections(Visitable startingPoint) {
        Stack<Visitable> stack = new Stack<>();
        Set<Visitable> connections = new HashSet<>();

        stack.add(startingPoint);

        while (!stack.isEmpty()) {
            Visitable visitable = stack.pop();
            connections.add(visitable);

            for (Visitable neighbor : visitable.getNeighbors()) {
                if (neighbor != null && !connections.contains(neighbor))
                    stack.push(neighbor);
            }

        }
        return connections;
    }
}
