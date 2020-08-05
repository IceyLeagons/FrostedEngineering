package net.iceyleagons.frostedengineering.network.energyold.components.sub.storages.battery;

import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.type.Comparator;
import org.bukkit.block.data.type.RedstoneWire;
import org.bukkit.block.data.type.Repeater;
import org.bukkit.inventory.ItemStack;

import net.iceyleagons.frostedengineering.gui.InventoryFactory;
import net.iceyleagons.frostedengineering.items.FrostedItems;
import net.iceyleagons.frostedengineering.network.energyold.EnergyNetwork;
import net.iceyleagons.frostedengineering.network.energyold.components.Storage;
import net.iceyleagons.frostedengineering.utils.ItemFactory;

public class BatteryStorage extends Storage {

    float zero = 0f;
    float twentyfive;
    float half;
    float seventyfive;
    float fifteen;

    public BatteryStorage(Location loc, EnergyNetwork network, UUID uuid, float maxStores, List<ItemStack> itemsInside) {
        super(loc, network, uuid, maxStores, itemsInside);
    }

    public BatteryStorage(Location loc, EnergyNetwork network, float maxStores) {
        super(loc, network, maxStores);
        twentyfive = maxStores * 0.25f;
        half = maxStores * 0.5f;
        seventyfive = maxStores * 0.75f;
        fifteen = maxStores * 0.15f;
    }

    @Override
    public float addPower(float fp) {
        if ((stores + fp) <= maxStores) {
            stores += fp;
            updateInventory();
            return 0f;
        } else {
            stores = maxStores;
            updateInventory();
            return maxStores - (stores + fp);
        }
    }

    @Override
    public void initInventory() {
        InventoryFactory fac = new InventoryFactory("Storage", 27, FrostedItems.INVENTORY_FILLER, true);
        for (int i = 10; i < 15; i++)
            fac.setItem(new ItemFactory(Material.WHITE_STAINED_GLASS_PANE).hideAttributes().setDisplayName("§f" + stores + "/" + maxStores + " FP").build(), i);
        super.setInventoryFactory(fac);
        updateInventory();
    }

    @Override
    public void updateInventory() {
        if (super.getInventoryFactory() == null) initInventory();
        InventoryFactory fac = super.getInventoryFactory();

        DecimalFormat df = new DecimalFormat("###,###.##");


        String text = "§f" + df.format(stores) + "/" + df.format(maxStores) + " FP";

        for (int i = 10; i < 15; i++)
            fac.setItem(new ItemFactory(fac.getSourceInventory().getItem(i).getType()).hideAttributes().setDisplayName(text).build(), i);

        if (stores >= zero)
            fac.setItem(new ItemFactory(Material.GREEN_STAINED_GLASS_PANE).hideAttributes().setDisplayName(text).build(), 10);
        if (stores >= twentyfive)
            fac.setItem(new ItemFactory(Material.GREEN_STAINED_GLASS_PANE).hideAttributes().setDisplayName(text).build(), 11);
        if (stores >= half)
            fac.setItem(new ItemFactory(Material.GREEN_STAINED_GLASS_PANE).hideAttributes().setDisplayName(text).build(), 12);
        if (stores >= seventyfive)
            fac.setItem(new ItemFactory(Material.GREEN_STAINED_GLASS_PANE).hideAttributes().setDisplayName(text).build(), 13);
        if (stores == maxStores)
            fac.setItem(new ItemFactory(Material.GREEN_STAINED_GLASS_PANE).hideAttributes().setDisplayName(text).build(), 14);

        if (stores <= zero)
            fac.setItem(new ItemFactory(Material.WHITE_STAINED_GLASS_PANE).hideAttributes().setDisplayName(text).build(), 10);
        if (stores <= twentyfive)
            fac.setItem(new ItemFactory(Material.WHITE_STAINED_GLASS_PANE).hideAttributes().setDisplayName(text).build(), 11);
        if (stores <= half)
            fac.setItem(new ItemFactory(Material.WHITE_STAINED_GLASS_PANE).hideAttributes().setDisplayName(text).build(), 12);
        if (stores <= seventyfive)
            fac.setItem(new ItemFactory(Material.WHITE_STAINED_GLASS_PANE).hideAttributes().setDisplayName(text).build(), 13);
        if (stores < maxStores)
            fac.setItem(new ItemFactory(Material.WHITE_STAINED_GLASS_PANE).hideAttributes().setDisplayName(text).build(), 14);
    }

    @Override
    public float consumePower(float fp) {
        if ((stores - fp) < 0) {
            stores -= maxStores;
            return fp - maxStores;
        }

        if ((stores - fp) >= 0) {
            stores -= fp;
            return 0f;
        }
        return 0f;
    }

    @Override
    public List<ItemStack> getItemsInside() {
        return null;
    }

    @Override
    public void onTick() {
        if (stores == maxStores) {
            for (Location loc : super.getLocationsAroundBlock()) {
                if (loc.getBlock().getType() == Material.REDSTONE_WIRE) {
                    RedstoneWire rw = (RedstoneWire) loc.getBlock().getBlockData();
                    rw.setPower(15);
                    loc.getBlock().setBlockData(rw, true);
                } else if (loc.getBlock().getType() == Material.REPEATER) {
                    Repeater r = (Repeater) loc.getBlock().getBlockData();
                    r.setPowered(true);
                    loc.getBlock().setBlockData(r, true);
                } else if (loc.getBlock().getType() == Material.COMPARATOR) {
                    Comparator c = (Comparator) loc.getBlock().getBlockData();
                    c.setPowered(true);
                    loc.getBlock().setBlockData(c, true);
                }
            }
        }
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
}
