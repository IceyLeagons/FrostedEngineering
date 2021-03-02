package net.iceyleagons.frostedengineering.api.textures.types;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IBreakable {

    int getIdealBreakTime();

    Material getIdealTool();

    ItemStack[] getLootTable();

    Sound getBreakSound();

    void onBreak(Player player, Location location);

}
