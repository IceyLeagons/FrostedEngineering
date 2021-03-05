package net.iceyleagons.frostedengineering.api.textures.abstracts;

import net.iceyleagons.frostedengineering.api.textures.types.ITextured;
import org.bukkit.Material;

public class TexturedSound implements ITextured {
    private final String name;
    private final String location;

    public TexturedSound(String name, String location) {
        this.name = name;
        this.location = location;
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
        return null;
    }
}
