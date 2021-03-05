package net.iceyleagons.frostedengineering.api.textures;

import lombok.NonNull;
import net.iceyleagons.frostedengineering.api.textures.types.IPlaceable;
import net.iceyleagons.frostedengineering.api.textures.types.ITextured;
import net.iceyleagons.icicle.location.block.BlockStorage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * @author Gábe
 */
public interface ITextureProvider {

    void registerTexture(@NonNull JavaPlugin registrar, @NonNull ITextured iTextured);

    boolean isTaken(@NonNull String id);

    @Nullable
    String getResourcePackUrl();

    void setResourcePackUrl(@Nullable String url);

    void setResourcePackHash(@Nullable String hash);

    void setResourcePackHashBytes(@Nullable byte[] bytes);

    @Nullable
    String getResourcePackHash();

    @Nullable
    byte[] getResourcePackHashBytes();

    @Nullable
    ITextured getById(@NonNull String id);

    @Nullable
    ITextured getByName(@NonNull String name);

    @NonNull
    Map<String, ITextured> getBlockMap();

    @NonNull
    Map<String, ITextured> getItemMap();

    @NonNull
    Map<String, ITextured> getSoundMap();

    @NonNull
    List<JavaPlugin> getRegistrars();

    @NonNull
    List<Material> getUsedMaterials();

    @NonNull
    Map<World, BlockStorage> getStorageMap();

    boolean needsTextureRebuild();

    void placeBlock(@NonNull Location location, @NonNull IPlaceable placeable, @Nullable Player player);

}
