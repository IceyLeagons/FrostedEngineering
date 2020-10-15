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

package net.iceyleagons.frostedengineering.common.network.impl.generators.coal;

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

import java.util.*;

public class TexturedCoalGenerator extends TexturedBlock {

    public static Map<Location, CoalGenerator> generators;
    //public static CoalGeneratorSound sound;
    public static float generating = 10;

    static {
        generators = new HashMap<Location, CoalGenerator>();
        //sound = new CoalGeneratorSound(Main.MAIN);
    }

    public TexturedCoalGenerator() {
        super(Main.MAIN, "coal_generator", "block/coal_generator", "§rCoal Generator");
        //Textures.register(sound);
        ComponentManager.registerComponent("fe:coalgenerator", this);
    }

    public CoalGenerator generateNewInstanceAtLocation(Location loc, EnergyNetwork net, float generates) throws UnsupportedUnitType {
        CoalGenerator g = new CoalGenerator(loc, net);
        generators.put(loc, g);
        return g;
    }

    public CoalGenerator generateNewInstanceAtLocation(Location loc, EnergyNetwork net, UUID uuid, float generates,
                                                       List<ItemStack> itemsInside) throws UnsupportedUnitType {
        CoalGenerator g = new CoalGenerator(loc, net, uuid, itemsInside);
        generators.put(loc, g);
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
        generateNewInstanceAtLocation(block.getLocation(), new EnergyNetwork(), generating);
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        updateMap();

        if (generators.get(Objects.requireNonNull(event.getClickedBlock()).getLocation()) != null) {
            generators.get(event.getClickedBlock().getLocation()).open(event.getPlayer());
        }
    }

    private void updateMap() {
        Iterator<Location> it = generators.keySet().iterator();
        while (it.hasNext()) {
            Location loc = it.next();
            if (loc != null)
                if (EnergyUnit.getEnergyUnitAtLocation(loc) == null)
                    it.remove();
        }
    }

}

