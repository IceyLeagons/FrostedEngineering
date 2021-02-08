package net.iceyleagons.computercraft.image;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

/**
 * @author TOTHTOMI
 */
public class MapRenderer extends org.bukkit.map.MapRenderer {

    private BufferedImage image;

    public void render(BufferedImage bufferedImage) {
        this.image = bufferedImage;
    }

    @Override
    public void render(@NotNull MapView map, @NotNull MapCanvas canvas, @NotNull Player player) {
        if (image != null) {
            canvas.drawImage(0, 0, image);
            image = null; //to save memory
        }
    }
}
