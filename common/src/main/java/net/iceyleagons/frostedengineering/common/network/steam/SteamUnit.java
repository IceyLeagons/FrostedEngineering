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

package net.iceyleagons.frostedengineering.common.network.steam;

import net.iceyleagons.frostedengineering.api.network.Network;
import net.iceyleagons.frostedengineering.api.network.Unit;
import net.iceyleagons.frostedengineering.api.network.exceptions.UnsupportedUnitType;
import net.iceyleagons.frostedengineering.common.Main;
import net.iceyleagons.frostedengineering.common.network.Tracer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author TOTHTOMI
 */
public class SteamUnit implements Unit {


    private Location location;
    private boolean destroyed;
    private SteamNetwork network;
    private UUID uuid;

    public SteamUnit(Location location, SteamNetwork network) throws UnsupportedUnitType {
        addUnit(this);
        this.location = location;
        this.network = network;
        this.network.addUnit(this);
        init();
        uuid = UUID.randomUUID();
    }

    public SteamUnit(Location location, SteamNetwork network, UUID uuid) throws UnsupportedUnitType {
        addUnit(this);
        this.location = location;
        this.network = network;
        this.uuid = uuid;
        init();
    }

    public void init() {
        Main.debug("Initializing Unit at location " + location.toString());
        Tracer.trace(this);
    }


    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public UUID getUUID() {
        return this.uuid;
    }

    @Override
    public void destroy() {
        Main.debug("Destorying unit at location " + location.toString());
        setDestroyed(true);
        Tracer.trace(this);
    }

    @Override
    public void destroy(Player p) {
        Main.debug("Destorying unit at location " + location.toString());
        setDestroyed(true);
        Tracer.trace(this);
    }

    @Override
    public void setDestroyed(boolean bool) {
        this.destroyed = bool;
    }

    @Override
    public boolean isDestroyed() {
        return this.destroyed;
    }

    @Override
    public Network getNetwork() {
        return this.network;
    }

    @Override
    public void setNetwork(Network net) {
        if (net == null) {
            this.network = null;
            return;
        }
        if (!(net instanceof SteamNetwork))
            throw new IllegalArgumentException("Only SteamNetworks can be supplied!");
        this.network = (SteamNetwork)net;
    }

    public List<SteamUnit> getSteamNeighbours() {return getNeighbours(getLocation());}

    @Override
    public List<Unit> getNeighbours() {
        List<Unit> units = new ArrayList<>(getNeighbours(getLocation()));
        return units;
    }


    private boolean visited = false;

    @Override
    public boolean isVisited() {
        return visited;
    }

    @Override
    public void visit() {
        visited = true;
    }

    @Override
    public void unvisit() {
        visited = false;
    }


    private static List<SteamUnit> units = new ArrayList<>();

    public static List<SteamUnit> getUnits() {
        return units;
    }

    public static List<SteamUnit> getNeighbours(Location loc) {
        Main.debug("Getting neighbour units for location " + loc.toString());
        List<SteamUnit> units = new ArrayList<>();

        SteamUnit target = null;

        target = getNeighbourAtLocation(loc, 0, 0, 1);
        if (target != null)
            units.add(target);

        target = getNeighbourAtLocation(loc, 0, 0, -1);
        if (target != null)
            units.add(target);

        target = getNeighbourAtLocation(loc, 1, 0, 0);
        if (target != null)
            units.add(target);

        target = getNeighbourAtLocation(loc, -1, 0, 0);
        if (target != null)
            units.add(target);

        target = getNeighbourAtLocation(loc, 0, 1, 0);
        if (target != null)
            units.add(target);

        target = getNeighbourAtLocation(loc, 0, -1, 0);
        if (target != null)
            units.add(target);

        return units;
    }

    public static void addUnit(Unit unit) {
        if (!(unit instanceof SteamUnit))
            throw new IllegalArgumentException("Only SteamUnits can be supplied!");
        units.add((SteamUnit) unit);
    }

    public static void removeUnit(Unit unit) {
        if (!(unit instanceof SteamUnit))
            throw new IllegalArgumentException("Only SteamUnits can be supplied!");
        units.remove((SteamUnit) unit);
    }


    private static SteamUnit getNeighbourAtLocation(Location start, int x, int y, int z) {
        Location target = start.clone().add(x, y, z);
        Main.debug("Getting unit at location " + start.toString());
        if (!target.equals(start)) {
            return getSteamUnitAtLocation(target);
        }
        return null;
    }

    public static SteamUnit getSteamUnitAtLocation(Location location) {
        Main.debug("Getting unit at location " + location.toString());
        for (SteamUnit unit : units)
            if (unit.getLocation().equals(location)) return unit;
        return null;
    }
}
