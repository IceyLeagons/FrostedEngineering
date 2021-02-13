package net.iceyleagons.worldgenerator;

import net.iceyleagons.worldgenerator.generator.FrostedChunkGenerator;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.LeavesDecayEvent;

public class Listeners implements Listener {

    @EventHandler
    public void onIceFade(BlockFadeEvent event) {
        if (event.getBlock().getWorld().getGenerator() instanceof FrostedChunkGenerator)
            event.setCancelled(true);
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event) {
        if (event.getBlock().getWorld().getGenerator() instanceof FrostedChunkGenerator)
            event.setCancelled(true);
    }

    @EventHandler
    public void onBlockForm(BlockFormEvent event) {
        if (event.getBlock().getWorld().getGenerator() instanceof FrostedChunkGenerator) {
            for (Entity nearbyEntity : event.getBlock().getWorld().getNearbyEntities(event.getBlock().getLocation(), 4, 4, 4))
                if (nearbyEntity instanceof LivingEntity)
                    if (((LivingEntity) nearbyEntity).getEquipment().getBoots().getEnchantments().containsKey(Enchantment.FROST_WALKER))
                        return;

            event.setCancelled(true);
        }
    }

}
