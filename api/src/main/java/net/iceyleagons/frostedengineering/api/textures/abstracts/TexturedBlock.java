package net.iceyleagons.frostedengineering.api.textures.abstracts;

import net.iceyleagons.frostedengineering.api.textures.types.IBreakable;
import net.iceyleagons.frostedengineering.api.textures.types.IPlaceable;
import net.iceyleagons.frostedengineering.api.textures.types.ITextured;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TexturedBlock implements ITextured, IPlaceable, IBreakable {
    private final String location;
    private final String name;
    private final Size size;
    private final int idealBreakTime;
    private final Material idealTool;
    private final Material material;

    public TexturedBlock(Size size, String name, String location, Material material) {
        this(size, name, location, material, Material.AIR, 10);
    }

    public TexturedBlock(Size size, String name, String location, Material material, Material idealTool, int breakTime) {
        this.size = size;
        this.name = name;
        this.location = location;
        this.idealTool = idealTool;
        this.idealBreakTime = breakTime;
        this.material = material;
    }

    @Override
    public int getIdealBreakTime() {
        return idealBreakTime;
    }

    @Override
    public Material getIdealTool() {
        return idealTool;
    }

    @Override
    public ItemStack[] getLootTable() {
        return new ItemStack[0];
    }

    @Override
    public Sound getBreakSound() {
        return Sound.BLOCK_STONE_BREAK;
    }

    @Override
    public Size getSize() {
        return null;
    }

    @Override
    public Sound getPlaceSound() {
        return Sound.BLOCK_STONE_PLACE;
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

    @Override
    public void onBreak(Player player, Location location) {
        // We let them implement this.
    }

    @Override
    public void onPlace(Player player, Location location) {
        // We let them implement this.
    }
}
