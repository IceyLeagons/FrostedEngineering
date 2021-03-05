package net.iceyleagons.frostedengineering.api.textures.abstracts;

import lombok.Getter;
import net.iceyleagons.frostedengineering.api.textures.types.IInteractive;
import net.iceyleagons.frostedengineering.api.textures.types.ITextured;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;

public class TexturedItem implements ITextured, IInteractive {
    @Getter
    private final String[] lore;
    @Getter
    private final String title;
    private final String name;
    private final String location;
    private final Material material;

    public TexturedItem(Material material, String name, String location) {
        this(material, name, location, name, null);
    }

    public TexturedItem(Material material, String name, String location, String title, String[] lore) {
        this.name = name;
        this.location = location;
        this.title = title;
        this.lore = lore;
        this.material = material;
    }

    public TexturedItem(Material material, String name, String location, String title) {
        this(material, name, location, title, null);
    }

    public TexturedItem(Material material, String name, String location, String[] lore) {
        this(material, name, location, name, lore);
    }

    @Override
    public void onInteract(Player player, Action action, int slot, EquipmentSlot equipmentSlot) {
        // We let them implement this.
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public Material getBaseMaterial() {
        return material;
    }
}
