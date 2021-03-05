package net.iceyleagons.frostedengineering.textures;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.iceyleagons.frostedengineering.api.IFrostedEngineering;
import net.iceyleagons.frostedengineering.api.textures.ITextureProvider;
import net.iceyleagons.frostedengineering.api.textures.abstracts.TexturedBlock;
import net.iceyleagons.frostedengineering.api.textures.abstracts.TexturedItem;
import net.iceyleagons.frostedengineering.api.textures.abstracts.TexturedSound;
import net.iceyleagons.frostedengineering.api.textures.types.IPlaceable;
import net.iceyleagons.frostedengineering.api.textures.types.ITextured;
import net.iceyleagons.frostedengineering.utils.serialization.ISerializable;
import net.iceyleagons.frostedengineering.utils.serialization.SerializationBuilder;
import net.iceyleagons.frostedengineering.utils.serialization.SerializationFieldGetter;
import net.iceyleagons.icicle.location.block.BlockStorage;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextureProvider implements ITextureProvider, ISerializable {

    public static boolean USE_PACK_IMAGE = true;
    public static String PACK_IMAGE_URL = "";
    public static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Getter
    @Setter
    private String resourcePackUrl;

    @Getter
    @Setter
    private byte[] resourcePackHashBytes;

    @Getter
    @Setter
    private String resourcePackHash;

    @Getter
    private final Map<World, BlockStorage> storageMap = new HashMap<>(3);

    @Getter
    private final List<JavaPlugin> registrars = new ArrayList<>(2);
    @Getter
    private final List<Material> usedMaterials = new ArrayList<>(5);

    @Getter
    private final Map<String, ITextured> blockMap = new HashMap<>(10);
    @Getter
    private final Map<String, ITextured> itemMap = new HashMap<>(10);
    @Getter
    private final Map<String, ITextured> soundMap = new HashMap<>(10);

    private boolean needsTextureRebuild = false;

    public TextureProvider() {

    }

    @Override
    public void registerTexture(@NonNull JavaPlugin registrar, @NonNull ITextured iTextured) {
        if (!registrars.contains(registrar))
            registrars.add(registrar);
        String id = String.format("%s:%s", StringUtils.deleteWhitespace(registrar.getName().toLowerCase()), iTextured.getName().toLowerCase().replace(" ", "_"));
        if (!isTaken(id)) {
            // TODO: initialization.

            // Registration
            if (iTextured instanceof TexturedBlock || iTextured instanceof TexturedItem)
                if (!usedMaterials.contains(iTextured.getBaseMaterial()))
                    usedMaterials.add(iTextured.getBaseMaterial());

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
        return blockMap.containsKey(id) || (itemMap.containsKey(id) || soundMap.containsKey(id));
    }

    @Nullable
    @Override
    public ITextured getById(@NonNull String id) {
        return blockMap.getOrDefault(id, itemMap.getOrDefault(id, soundMap.getOrDefault(id, null)));
    }

    @Nullable
    @Override
    public ITextured getByName(@NonNull String name) {
        return blockMap.values().stream().filter(block -> block.getName().equals(name)).findFirst().orElse(itemMap.values().stream().filter(item -> item.getName() == name).findFirst().orElse(soundMap.values().stream().filter(sound -> sound.getName() == name).findFirst().orElse(null)));
    }

    @Override
    public boolean needsTextureRebuild() {
        return needsTextureRebuild;
    }

    @Override
    public void placeBlock(@NonNull Location location, @NonNull IPlaceable placeable, @Nullable Player player) {

    }

    @Override
    public void serialize(SerializationBuilder serializationBuilder) {
        serializationBuilder.set("url", resourcePackUrl);
        serializationBuilder.set("hash", resourcePackHash);
        serializationBuilder.set("hash-bytes", resourcePackHashBytes);
    }

    @Override
    public void deserialize(IFrostedEngineering iFrostedEngineering, SerializationFieldGetter serializationFieldGetter) {
        setResourcePackUrl(serializationFieldGetter.get("url", String.class));
        setResourcePackHash(serializationFieldGetter.get("hash", String.class));
        setResourcePackHashBytes(serializationFieldGetter.get("hash-bytes", byte[].class));
    }
}
