package net.iceyleagons.frostedengineering.api.textures.types;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;

public interface IInteractive {

    void onInteract(Player player, Action action, int slot, EquipmentSlot equipmentSlot);

}
