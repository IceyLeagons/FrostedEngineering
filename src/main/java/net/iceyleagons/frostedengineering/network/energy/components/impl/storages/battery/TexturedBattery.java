package net.iceyleagons.frostedengineering.network.energy.components.impl.storages.battery;

import lombok.NonNull;
import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.network.energy.EnergyNetwork;
import net.iceyleagons.frostedengineering.network.energy.EnergyUnit;
import net.iceyleagons.frostedengineering.network.energy.ComponentManager;
import net.iceyleagons.frostedengineering.network.energy.exceptions.UnsupportedUnitType;
import net.iceyleagons.frostedengineering.textures.base.TexturedBlock;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
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

    @Override
    public void onPlacement(Block block, Player player) {
        try {
            generateNewInstanceAtLocation(block.getLocation(), new EnergyNetwork(), 500f);
        } catch (UnsupportedUnitType unsupportedUnitType) {
            unsupportedUnitType.printStackTrace();
        }
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

