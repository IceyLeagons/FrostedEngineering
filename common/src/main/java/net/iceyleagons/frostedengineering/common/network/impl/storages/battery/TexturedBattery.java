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

package net.iceyleagons.frostedengineering.common.network.impl.storages.battery;

import lombok.NonNull;
import lombok.SneakyThrows;
import net.iceyleagons.frostedengineering.api.network.exceptions.UnsupportedUnitType;
import net.iceyleagons.frostedengineering.common.Main;
import net.iceyleagons.frostedengineering.common.network.ComponentManager;
import net.iceyleagons.frostedengineering.common.network.energy.EnergyNetwork;
import net.iceyleagons.frostedengineering.common.network.energy.EnergyUnit;
import net.iceyleagons.frostedengineering.common.textures.base.TexturedBlock;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.*;

public class TexturedBattery extends TexturedBlock {

    public static Map<Location, Battery> storages;

    static {
        storages = new HashMap<Location, Battery>();
    }

    public TexturedBattery() {
        super(Main.MAIN, "battery", "block/battery", "§rStorage");
        ComponentManager.registerComponent("fe:batterystorage", this);

    }

    public Battery generateNewInstanceAtLocation(@NonNull Location loc, @NonNull EnergyNetwork net, @NonNull float stores) throws UnsupportedUnitType {
        Battery g = new Battery(loc, net, stores);
        storages.put(loc, g);
        return g;
    }

    public Battery generateNewInstanceAtLocation(@NonNull Location loc, @NonNull EnergyNetwork net, @NonNull UUID uuid, @NonNull float stores, @Nullable List<ItemStack> itemsInside) throws UnsupportedUnitType {
        Battery g = new Battery(loc, net, uuid, stores, itemsInside);
        storages.put(loc, g);
        return g;
    }

    @Override
    public void onBroken(BlockBreakEvent e) {
        EnergyUnit u = EnergyUnit.getEnergyUnitAtLocation(e.getBlock().getLocation());
        if (u != null) {
            u.destroy(e.getPlayer());
        }
        updateMap();
    }

    @SneakyThrows
    @Override
    public void onPlacement(Block block, Player player) {
        generateNewInstanceAtLocation(block.getLocation(), new EnergyNetwork(), 500f);
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        updateMap();

        if (storages.get(Objects.requireNonNull(event.getClickedBlock()).getLocation()) != null) {
            storages.get(event.getClickedBlock().getLocation()).getInventoryFactory().openInventory(event.getPlayer());
        }
    }

    private void updateMap() {
        Iterator<Location> it = storages.keySet().iterator();
        while (it.hasNext()) {
            Location loc = it.next();
            if (loc != null)
                if (EnergyUnit.getEnergyUnitAtLocation(loc) == null)
                    it.remove();
        }
    }

}

