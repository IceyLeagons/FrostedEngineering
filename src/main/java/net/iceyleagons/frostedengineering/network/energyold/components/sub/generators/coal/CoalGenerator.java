package net.iceyleagons.frostedengineering.network.energyold.components.sub.generators.coal;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.iceyleagons.frostedengineering.gui.InventoryFactory;
import net.iceyleagons.frostedengineering.gui.InventoryFactory.ClickRunnable;
import net.iceyleagons.frostedengineering.items.FrostedItems;
import net.iceyleagons.frostedengineering.network.energyold.EnergyNetwork;
import net.iceyleagons.frostedengineering.network.energyold.components.Generator;
import net.iceyleagons.frostedengineering.utils.ItemFactory;
import net.iceyleagons.frostedengineering.utils.ItemUtils;

public class CoalGenerator extends Generator {

    public CoalGenerator(Location loc, EnergyNetwork network, float generates) {
        super(loc, network, generates);
    }

    public CoalGenerator(Location loc, EnergyNetwork network, UUID uuid, float generates, List<ItemStack> itemsInside) {
        super(loc, network, uuid, generates, itemsInside);
    }

    @Override
    public void initInventory() {
        InventoryFactory fac = new InventoryFactory("Generator", 27, FrostedItems.INVENTORY_FILLER.asOne(), false);
        fac.setItem(new ItemFactory(Material.LIGHT_GRAY_STAINED_GLASS_PANE).hideAttributes()
                .setDisplayName("§fGenerating: §b0FP§7/§ftick").build(), 12);
        fac.setItem(new ItemStack(Material.AIR), 14);
        super.setInventoryFactory(fac);
        if (super.getLoadedItems() != null) {
            List<ItemStack> items = super.getLoadedItems();
            if (!items.isEmpty()) {
                for (int i = 0; i < items.size(); i++) {
                    if (i == 0) {
                        super.getInventoryFactory().setItem(items.get(i), 14);
                    }
                }
            }
        }
        updateInventory();
    }

    @Override
    public void updateInventory() {
        if (!super.isDestroyed()) {
            if (enabled)
                super.getInventoryFactory().setItem(new ItemFactory(Material.RED_STAINED_GLASS_PANE).hideAttributes()
                        .setDisplayName("§fGenerating: §b" + generates + " FP§7/§ftick").build(), 12);
            else
                super.getInventoryFactory().setItem(new ItemFactory(Material.GRAY_STAINED_GLASS_PANE).hideAttributes()
                        .setDisplayName("§fGenerating: §b0FP§7/§ftick").build(), 12, new ClickRunnable() {

                    @Override
                    public void run(InventoryClickEvent e) {
                        e.setCancelled(true);
                    }
                });

            int slot = 14;
            if (remainigBurn < 1) {
                ItemStack item = super.getInventoryFactory().getSourceInventory().getItem(slot);
                if (item != null) {
                    if (item.getType().isFuel()) {
                        remainigBurn = ItemUtils.getBurnTime(item) / 32;
                        if (item.getType() != Material.LAVA_BUCKET) {
                            if ((item.getAmount() - 1) > 0) {
                                super.getInventoryFactory().setItem(item.asQuantity(item.getAmount() - 1), slot);
                            } else {
                                super.getInventoryFactory().setItem(new ItemStack(Material.AIR), slot);
                            }
                        } else {
                            super.getInventoryFactory().setItem(new ItemStack(Material.BUCKET), slot);
                        }
                    }
                    super.getInventoryFactory().updateInventory();
                }
            }
        }
    }

    @Override
    public void onTick() {
        updateInventory();
        update();
        if (super.getLocation().getBlock().isBlockPowered()) {
            enabled = false;
        }
        if (enabled && !super.isDestroyed()) {
            ((EnergyNetwork) getNetwork()).generateFP(generates);
            remainigBurn--;
        }
    }


    private boolean playing = false;

    @Override
    public void onSecond() {
        if (!super.isDestroyed()) {
            if (enabled) {
                if (playing == false)
                    TexturedCoalGenerator.sound.play(super.getLocation(), true);
                playing = true;
                super.getLocation().getWorld().spawnParticle(Particle.SMOKE_NORMAL,
                        super.getLocation().getBlockX() + 0.5, super.getLocation().getBlockY() + 0.75,
                        super.getLocation().getBlockZ() + 0.5, 8, 0.2, 0.1, 0.2, 0, null, false);
            } else {
                TexturedCoalGenerator.sound.stop(super.getLocation());
                playing = false;
            }
        }
    }

    @Override
    public List<ItemStack> getItemsInside() {
        return Arrays.asList(super.getInventoryFactory().getSourceInventory().getItem(14));
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
