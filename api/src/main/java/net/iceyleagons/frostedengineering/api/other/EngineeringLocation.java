package net.iceyleagons.frostedengineering.api.other;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import net.iceyleagons.frostedengineering.api.IFrostedEngineering;
import net.iceyleagons.frostedengineering.api.network.Unit;
import net.iceyleagons.icicle.location.Cuboid;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 * This class is basically Bukkit's {@link org.bukkit.Location} but with more features that can come handy.
 * Some of the functions are brought here from {@link Location} for easier access.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since 1.0.0
 */
@EqualsAndHashCode
public class EngineeringLocation implements Cloneable {

    @Getter
    private final Location origin;
    private final EngineeringLocation[] around;

    @Getter
    private final IFrostedEngineering frostedEngineering;

    /**
     * @param from the location origin
     */
    public EngineeringLocation(@NonNull final Location from) {
        this(from, null);
    }

    /**
     * @param from the location origin
     * @param iFrostedEngineering the instance of {@link IFrostedEngineering} used for methods like {@link #getUnit()}
     */
    public EngineeringLocation(@NonNull final Location from, @NonNull IFrostedEngineering iFrostedEngineering) {
        this.frostedEngineering = iFrostedEngineering;
        origin = from;
        around = findAroundLocations(this);
    }

    /**
     * Will return the unit if the constructor {@link #EngineeringLocation(Location, IFrostedEngineering)} is used and if there's a known
     * unit at that location
     *
     * @return the {@link Unit} or null
     */
    public Unit getUnit() {
        if (frostedEngineering == null) return null;
        return frostedEngineering.getUnitManager().getUnitAtLocation(this);
    }

    public Interactable getInteractable() {
        //TODO
        return null;
    }

    /**
     * Will return true if the constructor {@link #EngineeringLocation(Location, IFrostedEngineering)} is used and if there's a known
     * custom block at that location.
     *
     * @return the true if the conditions are met above otherwise false
     */
    public boolean isCustomBlock() {
        //TODO
        return false;
    }
    
    /**
     * @see World#playSound(Location, Sound, float, float) 
     */
    public void playSound(Sound sound, float volume, float pitch) {
        getWorld().playSound(origin, sound, volume, pitch);
    }

    public EngineeringLocation[] getLocationsAround() {
        return around;
    }

    /**
     * @see World#getHighestBlockAt(Location) 
     */
    public Location getHighestBlock() {
        return getWorld().getHighestBlockAt(origin).getLocation();
    }

    /*
     * Methods from Location for easier access
     */

    /**
     * @see Location#getWorld()
     */
    public World getWorld() {
        return origin.getWorld();
    }

    /**
     * @see Location#getBlock()
     */
    public Block getBlock() {
        return origin.getBlock();
    }

    /**
     * @see Location#getChunk()
     */
    public Chunk getChunk() {
        return origin.getChunk();
    }

    /**
     * @see Location#getBlockX()
     */
    public int getBlockX() {
        return origin.getBlockX();
    }

    /**
     * @see Location#getBlockY()
     */
    public int getBlockY() {
        return origin.getBlockY();
    }

    /**
     * @see Location#getBlockZ()
     */
    public int getBlockZ() {
        return origin.getBlockZ();
    }

    /**
     * @see Location#getX()
     */
    public double getX() {
        return origin.getX();
    }

    /**
     * @see Location#getY()
     */
    public double getY() {
        return origin.getY();
    }

    /**
     * @see Location#getZ()
     */
    public double getZ() {
        return origin.getZ();
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /*
     * Statics
     */
    private static EngineeringLocation[] findAroundLocations(EngineeringLocation location) {
        Cuboid cuboid = new Cuboid(location.getOrigin().clone().add(1,1,1), location.getOrigin().clone().subtract(1,1,1));
        EngineeringLocation[] around = new EngineeringLocation[cuboid.getTotalBlockSize()];
        Block[] blocks = cuboid.blockList().toArray(new Block[0]);
        for (int i = 0; i < blocks.length; i++) {
            around[i] = new EngineeringLocation(blocks[i].getLocation(), location.getFrostedEngineering());
        }
        return around;
    }
}
