package net.iceyleagons.frostedengineering.listeners;

import lombok.RequiredArgsConstructor;
import net.iceyleagons.frostedengineering.api.IFrostedEngineering;
import net.iceyleagons.frostedengineering.api.other.Interactable;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Optional;

/**
 * @author TOTHTOMI
 */
@RequiredArgsConstructor
public class InteractListener implements Listener {

    private final IFrostedEngineering iFrostedEngineering;


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        Location location = event.getClickedBlock().getLocation();
        Optional<Interactable> optional =
                iFrostedEngineering.getInteractableRegistry().getOptionally(location);
        if (optional.isPresent()) {
            event.setCancelled(true);
            Interactable interactable = optional.get();
            handleOpening(interactable, event.getPlayer(), event);
        }
    }

    private void handleOpening(Interactable interactable, Player player, PlayerInteractEvent event) {
        if (interactable.isLocked()) {
            if (interactable.getOwner().equals(event.getPlayer()) || interactable.getWhitelist().contains(player))
                interactable.onInteract().accept(event.getPlayer(), event);
            else {
                player.playSound(player.getLocation(), Sound.BLOCK_CHEST_LOCKED, 1f, 1f);
                player.sendMessage("You don't have permission to interact with this! Ask the owner " + interactable.getOwner().getName());
            }
        } else {
            interactable.onInteract().accept(event.getPlayer(), event);
        }
    }
}
