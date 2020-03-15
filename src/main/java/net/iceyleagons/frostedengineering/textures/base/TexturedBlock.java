package net.iceyleagons.frostedengineering.textures.base;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import net.iceyleagons.frostedengineering.textures.TextureUtils;
import net.iceyleagons.frostedengineering.textures.WrappedMobSpawner;

public class TexturedBlock extends TexturedBase {

	protected String title;
	private static WrappedMobSpawner wms;
	private Sound placeSound = Sound.BLOCK_STONE_HIT;

	static {
		wms = new WrappedMobSpawner();
	}

	public TexturedBlock(JavaPlugin plugin, String name, String modelPath, String title, Material baseMaterial) {
		super(plugin, name, modelPath, baseMaterial);
		this.title = title;
	}

	public TexturedBlock(JavaPlugin plugin, String name, String modelPath, String title) {
		super(plugin, name, modelPath, Material.DIAMOND_HOE);
		this.title = title;
	}

	public void onInteract(PlayerInteractEvent event) {
		// Nothing, we let the user implement stuff like this.
	}

	public void onPlacement(BlockPlaceEvent event) {
		// Nothing, we let the user implement stuff like this.
	}

	public void onBroken(BlockBreakEvent event) {
		// Nothing, we let the user implement stuff like this.
	}

	/**
	 * Checks whether or not the specified block is the same type as this is.
	 * 
	 * @param block the block to compare it to
	 * @return whether or not the block is this kind of block.
	 */
	public boolean compare(Block block) {
		return wms.execute(block, this);
	}

	/**
	 * Gets the block in a placeable form.
	 * 
	 * @return the block in item form.
	 */
	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.DIAMOND_HOE);
		item.setItemMeta(TextureUtils.createItemMeta(item, title, getId()));

		return item;
	}

	/**
	 * Sets the place sound of the block.
	 * 
	 * @param sound the sound to set it to
	 */
	public void setPlaceSound(Sound sound) {
		this.placeSound = sound;
	}

	/**
	 * @see TexturedBlock#setBlock(Location)
	 */
	public void setBlock(World world, int x, int y, int z) {
		setBlock(new Location(world, x, y, z));
	}

	/**
	 * @see TexturedBlock#setBlock(Location)
	 */
	public void setBlock(Block block) {
		setBlock(block.getLocation());
	}

	/**
	 * Sets the block at specified location to this type of block.
	 * 
	 * @param location the location to place the block
	 */
	public void setBlock(Location location) {
		Block block = location.getWorld().getBlockAt(location);
		block.setType(Material.SPAWNER);

		wms.execute(block,
				"{MaxNearbyEntities:0s,RequiredPlayerRange:0s,SpawnData:{id:\"minecraft:armor_stand\",Invisible:1,Marker:1,ArmorItems:[{},{},{},{id:\"minecraft:"
						+ getBaseMaterial().name().toLowerCase() + "\",Count:1b,Damage:"
						+ (getBaseMaterial().getMaxDurability() - getId()) + "s,tag:{Unbreakable:1}}]}}");

		block.getWorld().playSound(location, placeSound, 1.f, 1.f);
	}

	/**
	 * Returns the current loot table for the block. When the player breaks the
	 * block, he gets this.
	 * 
	 * @return the loot table for the block.
	 */
	public ItemStack[] getLootTable() {
		return new ItemStack[] { getItem() };
	}

}
