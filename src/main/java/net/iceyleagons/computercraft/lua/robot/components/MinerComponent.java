package net.iceyleagons.computercraft.lua.robot.components;

import lombok.SneakyThrows;
import net.iceyleagons.computercraft.lua.library.LibraryBuilder;
import net.iceyleagons.computercraft.lua.robot.Component;
import net.iceyleagons.computercraft.lua.robot.Robot;
import net.iceyleagons.icicle.wrapped.bukkit.WrappedCraftBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

/**
 * @author TOTHTOMI
 */
public class MinerComponent extends Component {


    public MinerComponent(Robot robot) {
        super(robot);
    }

    @Override
    protected void addLibraryFields(LibraryBuilder libraryBuilder) {
        libraryBuilder.addFunction("digDown", varargs ->
                generateReturn(digDown()))
                .addFunction("digUp", varargs -> generateReturn(digUp()))
                .addFunction("digStraight", varargs -> generateReturn(digStraight()));
    }

    @SneakyThrows
    public boolean digStraight() {
        Block block = getBlock(getBlockFace());
        if (block.isEmpty()) return false;
        if (!canBreak(block)) return false;

        doSync(() -> {
            WrappedCraftBlock wrappedCraftBlock = WrappedCraftBlock.from(block);
            wrappedCraftBlock.dropNaturally(new ItemStack(Material.NETHERITE_PICKAXE), true);

            getEntity().swingMainHand();
        });
        Thread.sleep(Robot.ACTION_TIMEOUT);
        return true;
    }

    @SneakyThrows
    public boolean digDown() {
        Block block = getBlock(BlockFace.DOWN);
        if (block.isEmpty()) return false;
        if (!canBreak(block)) return false;

        doSync(() -> {
            WrappedCraftBlock wrappedCraftBlock = WrappedCraftBlock.from(block);
            wrappedCraftBlock.dropNaturally(new ItemStack(Material.NETHERITE_PICKAXE), true);

            getEntity().swingMainHand();
        });
        Thread.sleep(Robot.ACTION_TIMEOUT);
        return true;
    }

    @SneakyThrows
    public boolean digUp() {
        Block block = getBlock(BlockFace.UP);
        if (block.isEmpty()) return false;
        if (!canBreak(block)) return false;

        doSync(() -> {
            WrappedCraftBlock wrappedCraftBlock = WrappedCraftBlock.from(block);
            wrappedCraftBlock.dropNaturally(new ItemStack(Material.NETHERITE_PICKAXE), true);

            getEntity().swingMainHand();
        });
        Thread.sleep(Robot.ACTION_TIMEOUT);
        return true;
    }
}
