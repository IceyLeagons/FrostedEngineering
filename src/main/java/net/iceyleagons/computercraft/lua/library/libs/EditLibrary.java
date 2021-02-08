package net.iceyleagons.computercraft.lua.library.libs;


import net.iceyleagons.computercraft.lua.library.LibraryBuilder;
import net.iceyleagons.computercraft.web.WebIDE;
import org.luaj.vm2.LuaValue;

import java.io.File;

/**
 * @author TOTHTOMI
 */
public class EditLibrary {

    public static LuaValue get() {
        return new LibraryBuilder().addFunction("load", varargs -> {
            String fileName = varargs.checkjstring(1);
            File file = new File(fileName);
            if (file.exists()) {
                //send default
                return LuaValue.varargsOf(new LuaValue[]{LuaValue.NIL});
            }
            WebIDE.luaFileFromBytebin(fileName);
            return LuaValue.varargsOf(new LuaValue[]{LuaValue.valueOf("Successfully wrote file " + fileName + ".net.iceyleagons.computercraft.lua")});
        }).addFunction("open", varargs -> LuaValue.varargsOf(new LuaValue[]{LuaValue.NIL})).build();
    }

}
