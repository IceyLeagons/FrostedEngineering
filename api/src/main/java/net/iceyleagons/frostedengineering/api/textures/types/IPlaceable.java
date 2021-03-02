package net.iceyleagons.frostedengineering.api.textures.types;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public interface IPlaceable {

    Size getSize();

    Sound getPlaceSound();

    void onPlace(Player player, Location location);

    /**
     * Currently only FULL_BLOCK is supported.
     */
    public enum Size {
        PLANT, HALF_BLOCK, FULL_BLOCK, MULTIBLOCK;
    }

}
