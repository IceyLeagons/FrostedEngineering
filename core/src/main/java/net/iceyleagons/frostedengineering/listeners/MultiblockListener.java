package net.iceyleagons.frostedengineering.listeners;

import lombok.AllArgsConstructor;
import net.iceyleagons.frostedengineering.api.IFrostedEngineering;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * @author TOTHTOMI
 */
@AllArgsConstructor
public class MultiblockListener implements Listener {

    private final IFrostedEngineering iFrostedEngineering;

    @EventHandler
    public void onBlockPlacement(BlockPlaceEvent event) {
        iFrostedEngineering.getMultiblockRegistry().getRegistered().forEach(pattern -> {
            if (pattern.isValid(event.getBlock())) {
                event.getPlayer().sendMessage("Created multiblock structure!");
            }
        });
    }
}
