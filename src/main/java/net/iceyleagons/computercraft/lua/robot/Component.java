package net.iceyleagons.computercraft.lua.robot;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.iceyleagons.computercraft.lua.LuaMachine;
import net.iceyleagons.computercraft.lua.library.LibraryBuilder;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.Inventory;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

/**
 * @author TOTHTOMI
 */
@RequiredArgsConstructor
public abstract class Component {

    @Getter
    private final Robot robot;

    protected Block getBlock(BlockFace blockFace) {
        return robot.getBlock(blockFace);
    }

    protected BlockFace getBlockFace() {
        return robot.getBlockFace();
    }

    protected Inventory getInventory() {
        return robot.getInventory();
    }

    protected boolean canBreak(Block block) {
        return robot.canBreak(block);
    }

    protected void doSync(Runnable runnable) {
        robot.doSync(runnable);
    }

    protected Zombie getEntity() {
        return robot.getEntity();
    }

    protected Varargs generateReturn(Object... returnArgs) {

        LuaValue[] luaValues = new LuaValue[returnArgs.length];
        for (int i = 0; i < returnArgs.length; i++) {
            luaValues[i] = LuaMachine.toValue(returnArgs[i]);
        }

        return LuaValue.varargsOf(luaValues);
    }

    protected abstract void addLibraryFields(LibraryBuilder libraryBuilder);


}
