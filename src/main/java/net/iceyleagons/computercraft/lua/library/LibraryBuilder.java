package net.iceyleagons.computercraft.lua.library;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author TOTHTOMI
 */
public class LibraryBuilder {

    private final List<Map.Entry<String, VarArgFunction>> varArgFunctions = new ArrayList<>();

    public LibraryBuilder addFunction(String functionName, VarArgFunction varArgFunction) {
        varArgFunctions.add(new AbstractMap.SimpleEntry<>(functionName,varArgFunction));
        return this;
    }

    public LibraryBuilder addFunction(String functionName, Function function) {
        VarArgFunction varArgFunction = new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs varargs) {
                return function.call(varargs);
            }
        };

        varArgFunctions.add(new AbstractMap.SimpleEntry<>(functionName,varArgFunction));
        return this;
    }

    /**
     * Builds the library
     *
     * @return the built {@link LuaTable} to set in {@link org.luaj.vm2.Globals}
     */
    public LuaTable build() {
        LuaTable luaValue = LuaValue.tableOf();
        varArgFunctions.forEach(entry -> luaValue.set(entry.getKey(), entry.getValue()));

        return luaValue;
    }

    /**
     * Basic wrapping of {@link VarArgFunction} for lambda support in the builder
     */
    public interface Function {
        Varargs call(Varargs varargs);
    }
}
