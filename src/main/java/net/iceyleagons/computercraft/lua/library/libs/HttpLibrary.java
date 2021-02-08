package net.iceyleagons.computercraft.lua.library.libs;

import net.iceyleagons.computercraft.lua.library.LibraryBuilder;
import org.luaj.vm2.LuaValue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * @author TOTHTOMI
 */
public class HttpLibrary {

    public static LuaValue get() {
        return new LibraryBuilder().addFunction("get", varargs -> {
            LuaValue success = LuaValue.NIL;
            LuaValue error = LuaValue.NIL;

            try {
                URL url = new URL(varargs.arg1().checkjstring());
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                    StringBuilder stringBuilder = new StringBuilder();

                    String input;
                    while ((input = bufferedReader.readLine()) != null) stringBuilder.append(input).append("\n");

                    success = LuaValue.valueOf(stringBuilder.toString());
                }
            } catch (Exception ex) {
                error = LuaValue.valueOf("Error! " + ex.getMessage());
            }

            return LuaValue.varargsOf(new LuaValue[]{success, error});
        }).build();
    }
}
