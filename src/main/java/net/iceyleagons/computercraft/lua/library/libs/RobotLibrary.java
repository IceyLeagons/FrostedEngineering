package net.iceyleagons.computercraft.lua.library.libs;

import lombok.Getter;
import net.iceyleagons.computercraft.lua.library.LibraryBuilder;
import net.iceyleagons.computercraft.lua.robot.Robot;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

/**
 * @author TOTHTOMI
 */
public class RobotLibrary {

    private final Robot robot;
    @Getter
    private final LibraryBuilder libraryBuilder;

    public RobotLibrary(Robot robot) {
        this.libraryBuilder = new LibraryBuilder();
        this.robot = robot;
    }

    public LuaTable get() {
        return libraryBuilder
                .addFunction("move", varargs -> LuaValue.varargsOf(new LuaValue[]{
                        LuaValue.valueOf(robot.move(varargs.arg1().checkint()))
                }))
                .addFunction("turnRight", varargs -> LuaValue.varargsOf(new LuaValue[]{
                        LuaValue.valueOf(robot.turnRight(varargs.arg1().checkint()))
                }))
                .addFunction("turnLeft", varargs -> LuaValue.varargsOf(new LuaValue[]{
                        LuaValue.valueOf(robot.turnLeft(varargs.arg1().checkint()))
                }))
                .addFunction("goUp", varargs -> LuaValue.varargsOf(new LuaValue[]{
                        LuaValue.valueOf(robot.goUp(varargs.arg1().checkint()))
                }))
                .addFunction("goDown", varargs -> LuaValue.varargsOf(new LuaValue[]{
                        LuaValue.valueOf(robot.goDown(varargs.arg1().checkint()))
                }))
                .addFunction("getId", varargs -> LuaValue.varargsOf(new LuaValue[]{
                        LuaValue.valueOf(robot.getId())
                }))
                .addFunction("useItem", varargs -> LuaValue.varargsOf(new LuaValue[]{
                        LuaValue.valueOf(robot.useItem())
                }))
                .addFunction("place", varargs -> LuaValue.varargsOf(new LuaValue[]{
                        LuaValue.valueOf(robot.place(varargs.arg1().checkint()))
                }))
                .addFunction("selectSlot", varargs -> LuaValue.varargsOf(new LuaValue[]{
                        LuaValue.valueOf(robot.selectSlot(varargs.arg1().checkint()))
                })).build();
    }

}
