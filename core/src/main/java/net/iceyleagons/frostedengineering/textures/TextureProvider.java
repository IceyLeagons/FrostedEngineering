package net.iceyleagons.frostedengineering.textures;

import lombok.Getter;
import lombok.NonNull;
import net.iceyleagons.frostedengineering.api.textures.ITextureProvider;
import net.iceyleagons.frostedengineering.api.textures.abstracts.TexturedBlock;
import net.iceyleagons.frostedengineering.api.textures.abstracts.TexturedItem;
import net.iceyleagons.frostedengineering.api.textures.abstracts.TexturedSound;
import net.iceyleagons.frostedengineering.api.textures.types.ITextured;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class TextureProvider implements ITextureProvider {

    private final Map<String, ITextured> blockMap = new HashMap<>(10);
    private final Map<String, ITextured> itemMap = new HashMap<>(10);
    private final Map<String, ITextured> soundMap = new HashMap<>(10);

    /**
     * Indicates whether or not the textures need to be rebuilt.
     */
    public boolean needsTextureRebuild = false;

    @Override
    public void registerTexture(@NonNull JavaPlugin registrar, @NonNull ITextured iTextured) {
        String id = String.format("%s:%s", StringUtils.deleteWhitespace(registrar.getName().toLowerCase()), iTextured.getName().toLowerCase().replace(" ", "_"));
        if (!isTaken(id)) {
            // TODO: initialization.

            // Registration
            if (iTextured instanceof TexturedBlock)
                blockMap.put(id, iTextured);

            if (iTextured instanceof TexturedItem)
                itemMap.put(id, iTextured);

            if (iTextured instanceof TexturedSound)
                soundMap.put(id, iTextured);

            needsTextureRebuild = true;
        }
    }

    @Override
    public boolean isTaken(@NonNull String id) {
        return blockMap.containsKey(id) ? true : itemMap.containsKey(id) ? true : soundMap.containsKey(id);
    }

    @Nullable
    @Override
    public ITextured getById(@NonNull String id) {
        return blockMap.getOrDefault(id, itemMap.getOrDefault(id, soundMap.getOrDefault(id, null)));
    }

    @Nullable
    @Override
    public ITextured getByName(@NonNull String name) {
        return blockMap.values().stream().filter(block -> block.getName() == name).findFirst().orElse(itemMap.values().stream().filter(item -> item.getName() == name).findFirst().orElse(soundMap.values().stream().filter(sound -> sound.getName() == name).findFirst().orElse(null)));
    }

    @Override
    public void placeBlock(@NonNull Location location, @NonNull ITextured textured, @Nullable Player player) {
        // TODO: placement.
    }
}
