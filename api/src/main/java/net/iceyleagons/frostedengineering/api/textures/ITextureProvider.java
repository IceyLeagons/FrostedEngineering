package net.iceyleagons.frostedengineering.api.textures;

import lombok.NonNull;
import net.iceyleagons.frostedengineering.api.textures.types.ITextured;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

/**
 * @author Gábe
 */
public interface ITextureProvider {

    void registerTexture(@NonNull JavaPlugin registrar, @NonNull ITextured iTextured);

    boolean isTaken(@NonNull String id);

    @Nullable
    ITextured getById(@NonNull String id);

    @Nullable
    ITextured getByName(@NonNull String name);

    void placeBlock(@NonNull Location location, @NonNull ITextured textured, @Nullable Player player);

}
