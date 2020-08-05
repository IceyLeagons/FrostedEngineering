package net.iceyleagons.frostedengineering.network.energyold.components.sub.storages.battery;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.network.energyold.EnergyNetwork;
import net.iceyleagons.frostedengineering.network.energy.EnergyUnit;
import net.iceyleagons.frostedengineering.network.energy.ComponentManager;
import net.iceyleagons.frostedengineering.textures.base.TexturedBlock;

public class TexturedBatteryStorage extends TexturedBlock {

    public static Map<Location, BatteryStorage> storages;

    static {
        storages = new HashMap<Location, BatteryStorage>();
    }

    public TexturedBatteryStorage() {
        super(Main.MAIN, "battery", "block/battery", "§rStorage");
        ComponentManager.registerComponent("fe:batterystorage", this);
    }

    public BatteryStorage generateNewInstanceAtLocation(Location loc, EnergyNetwork net, float stores) {
        BatteryStorage g = new BatteryStorage(loc, net, stores);
        storages.put(loc, g);
        return g;
    }

    public BatteryStorage generateNewInstanceAtLocation(Location loc, EnergyNetwork net, UUID uuid, float stores, List<ItemStack> itemsInside) {
        BatteryStorage g = new BatteryStorage(loc, net, uuid, stores, itemsInside);
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

    @Override
    public void onPlacement(Block block, Player player) {
        generateNewInstanceAtLocation(block.getLocation(), new EnergyNetwork(), 500f);
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        updateMap();

        if (storages.get(event.getClickedBlock().getLocation()) != null) {
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
