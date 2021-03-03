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

package net.iceyleagons.frostedengineering.network.steam;

import lombok.NonNull;
import lombok.Setter;
import net.iceyleagons.frostedengineering.network.Network;
import net.iceyleagons.frostedengineering.network.Unit;
import net.iceyleagons.frostedengineering.network.energy.EnergyNetwork;
import net.iceyleagons.frostedengineering.network.energy.exceptions.UnsupportedUnitType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author TOTHTOMI
 */
public class SteamNetwork implements Network {

    private List<SteamUnit> units;
    @Setter
    private UUID uuid;

    public SteamNetwork() {
        this.units = new ArrayList<>();
    }

    @Override
    public void addUnit(Unit unit) throws UnsupportedUnitType {
        if (!(unit instanceof SteamUnit)) throw new UnsupportedUnitType();

        if (!units.contains(unit)) {
            units.add((SteamUnit)unit);
        }
    }

    @Override
    public void removeUnit(Unit unit) throws UnsupportedUnitType {
        if (!(unit instanceof SteamUnit)) throw new UnsupportedUnitType();
        units.remove(unit);
    }

    @Override
    public List<Unit> getUnits() {
        return new ArrayList<>(units);
    }

    @Override
    public UUID getUUID() {
        return this.uuid;
    }

    @Override
    public Network generateSameType() {
        return new SteamNetwork();
    }

    public static void serialize(@NonNull SteamNetwork network, @NonNull File file) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
                objectOutputStream.writeObject(network);
            }
        }
    }

    public static SteamNetwork deserialize(@NonNull File file) throws IOException, ClassNotFoundException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
                return (SteamNetwork)objectInputStream.readObject();
            }
        }
    }
}
