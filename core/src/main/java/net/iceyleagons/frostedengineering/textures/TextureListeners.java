package net.iceyleagons.frostedengineering.textures;

import lombok.AllArgsConstructor;
import net.iceyleagons.frostedengineering.api.IFrostedEngineering;
import net.iceyleagons.icicle.wrapped.packet.WrappedPacketPlayOutBlockBreakAnimation;
import net.iceyleagons.icicle.wrapped.world.WrappedBlockPosition;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

@AllArgsConstructor
public class TextureListeners implements Listener {

    IFrostedEngineering frostedEngineering;

    @EventHandler
    public void onBlockBreakStart(BlockDamageEvent event) {
        if (!event.getInstaBreak())
            for (Entity nearbyEntity : event.getBlock().getWorld().getNearbyEntities(event.getBlock().getLocation(), 10, 10, 10))
                if (nearbyEntity instanceof Player)
                    // Damage: 0-11
                    new WrappedPacketPlayOutBlockBreakAnimation(0, new WrappedBlockPosition(event.getBlock().getLocation()), 7).send((Player) nearbyEntity);
    }

    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (frostedEngineering.getTextureProvider().getResourcePackUrl() != null && frostedEngineering.getTextureProvider().getResourcePackHashBytes() != null)
                    event.getPlayer().setResourcePack(frostedEngineering.getTextureProvider().getResourcePackUrl(), frostedEngineering.getTextureProvider().getResourcePackHashBytes());
            }
        }.runTaskLater(frostedEngineering.getPlugin(), 60L);
    }

}
