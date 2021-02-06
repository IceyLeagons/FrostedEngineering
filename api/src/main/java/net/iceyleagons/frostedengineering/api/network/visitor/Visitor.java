package net.iceyleagons.frostedengineering.api.network.visitor;

import java.util.Set;

/**
 * @author TOTHTOMI
 */
public interface Visitor {

    Set<Visitable> findConnections(Visitable startingPoint);

}
