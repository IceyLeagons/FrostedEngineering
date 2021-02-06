package net.iceyleagons.frostedengineering.api.other;

import net.iceyleagons.frostedengineering.api.IFrostedEngineering;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashSet;
import java.util.function.BiConsumer;

/**
 * @author TOTHTOMI
 */
public interface Interactable {

    boolean isLocked();
    BiConsumer<Player, PlayerInteractEvent> onInteract();
    OfflinePlayer getOwner();
    IFrostedEngineering getFrostedEngineering();
    HashSet<OfflinePlayer> getWhitelist();

}
