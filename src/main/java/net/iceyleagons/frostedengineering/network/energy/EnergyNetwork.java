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

package net.iceyleagons.frostedengineering.network.energy;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.iceyleagons.frostedengineering.network.Network;
import net.iceyleagons.frostedengineering.network.Unit;
import net.iceyleagons.frostedengineering.network.energy.components.ChargableComponent;
import net.iceyleagons.frostedengineering.network.energy.exceptions.UnsupportedUnitType;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class EnergyNetwork implements Network, java.io.Serializable {

    @Setter
    private List<EnergyUnit> units;
    @Getter
    @Setter
    private EnergyNetworkType type;
    @Setter
    private UUID uuid;

    public EnergyNetwork() {
        this.units = new ArrayList<>();
        type = EnergyNetworkType.LOW_VOLTAGE;
    }

    @Override
    public List<Unit> getUnits() {
        return new ArrayList<>(units);
    }

    public Map<Float, ChargableComponent> getChargableComponents() {
        Map<Float, ChargableComponent> chargableComponents = new HashMap<>();
        for (EnergyUnit unit : units) {
            if (unit instanceof ChargableComponent) {
                ChargableComponent chargableComponent = (ChargableComponent) unit;
                chargableComponents.put(chargableComponent.getStored(), chargableComponent);
            }
        }
        return chargableComponents;
    }


    public float getEnergy(@NonNull Map<Float, ChargableComponent> ChargableComponents) {
        AtomicReference<Float> energyTotal = new AtomicReference<>((float) 0);
        ChargableComponents.keySet().forEach(stored -> energyTotal.updateAndGet(fp -> fp + stored));
        return energyTotal.get();
    }

    public void depositEnergy(float energy) {
        Map<Float, ChargableComponent> chargableComponents = getChargableComponents();
        distributeEnergy(chargableComponents, energy);
        
    }

    private void distributeEnergy(@NonNull Map<Float, ChargableComponent> ChargableComponents,float energy) {
        float toAdd = energy;

        int i = 0;
        List<Float> keys = new ArrayList<>(ChargableComponents.keySet());
        Collections.sort(keys);
        while (toAdd >= 0) {
            ChargableComponent chargableComponent = ChargableComponents.get(keys.get(i));
            if ((chargableComponent.getMaxStorage() - chargableComponent.getStored()) >= toAdd) chargableComponent.addEnergy(toAdd);
            else {
                float max = (chargableComponent.getMaxStorage() - chargableComponent.getStored());
                chargableComponent.addEnergy(max);
                toAdd -= max;
            }
        }

    }

    public float withdrawEnergy(float needed) {
        Map<Float, ChargableComponent> chargableComponents = getChargableComponents();
        float storedEnergy = getEnergy(chargableComponents);
        if (storedEnergy > needed) {
            removeEnergy(chargableComponents, needed);
            return needed;
        } else if (storedEnergy <= needed) {
            clearEnergy(chargableComponents);
            return storedEnergy;
        }
        return 0f;
    }

    private static void removeEnergy(@NonNull Map<Float, ChargableComponent> ChargableComponents,float removal) {
        float toRemove = removal;
        List<Float> keys = new ArrayList<>(ChargableComponents.keySet());
        Collections.sort(keys);
        Collections.reverse(keys);
        int i = 0;
        while (toRemove >= 0)
            toRemove = ChargableComponents.get(keys.get(i)).addEnergy(toRemove);
    }

    public static void clearEnergy(@NonNull Map<Float, ChargableComponent> chargableComponentMap) {
        chargableComponentMap.values().forEach(s -> s.consumeEnergy(s.getStored()));
    }

    @Override
    public void addUnit(@NonNull Unit unit) throws UnsupportedUnitType {
        if (!(unit instanceof EnergyUnit)) throw new UnsupportedUnitType();

        if (!units.contains(unit)) {
            units.add((EnergyUnit) unit);
        }
    }

    @Override
    public void removeUnit(@NonNull Unit unit) throws UnsupportedUnitType {
        if (!(unit instanceof EnergyUnit)) throw new UnsupportedUnitType();

        units.remove(unit);
    }

    @Override
    public UUID getUUID() {
        return this.uuid;
    }

    @Override
    public Network generateSameType() {
        return new EnergyNetwork();
    }

    public static void serialize(@NonNull EnergyNetwork network, @NonNull File file) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
                objectOutputStream.writeObject(network);
            }
        }
    }

    public static EnergyNetwork deserialize(@NonNull File file) throws IOException, ClassNotFoundException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
                return (EnergyNetwork)objectInputStream.readObject();
            }
        }
    }
}
