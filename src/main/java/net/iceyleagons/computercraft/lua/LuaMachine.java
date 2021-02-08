package net.iceyleagons.computercraft.lua;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.iceyleagons.computercraft.lua.library.libs.EditLibrary;
import net.iceyleagons.computercraft.lua.library.libs.HttpLibrary;
import net.iceyleagons.computercraft.lua.terminal.Terminal;
import net.iceyleagons.frostedengineering.api.addon.Addon;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

/**
 * @author TOTHTOMI
 */
@EqualsAndHashCode
public abstract class LuaMachine {


    private static int count = 1;
    @Getter
    private final Terminal terminal;
    private final Globals globals;

    @Getter
    private final int id;

    @Getter
    private File directory;
    @Getter
    @Setter
    private File openDirectory;

    public LuaMachine(Addon addon) {
        id = count++;
        terminal = new Terminal(this, addon.getFrostedEngineering());
        globals = JsePlatform.debugGlobals();
        init(addon);
    }

    public LuaMachine(Addon addon, int id) {
        this.id = id;
        terminal = new Terminal(this, addon.getFrostedEngineering());
        globals = JsePlatform.debugGlobals();
        init(addon);
    }

    protected Globals getGlobals() {
        return globals;
    }

    protected LuaValue call(LuaValue luaValue, @Nullable String args) throws LuaError {
        if (args == null)
            return luaValue.call();
        else
            return luaValue.call(toValue(args));

    }

    public abstract Location getLocation();

    public void init(Addon addon) {

        directory = new File(addon.getDataFolder(), "computers"+File.separator+id);
        if (!directory.exists()) {
            if (!directory.mkdirs()) throw new IllegalArgumentException("Could not create computer directory for machine with ID " + id);
        }
        openDirectory = directory;

        globals.set("collectgarbage", LuaValue.NIL);
        globals.set("dofile", LuaValue.NIL); //TODO custom wrapper
        globals.set("loadfile", LuaValue.NIL); //TODO custom wrapper
        globals.set("module", LuaValue.NIL);
        globals.set("require", LuaValue.NIL);
        globals.set("package", LuaValue.NIL);
        globals.set("io", LuaValue.NIL);
        globals.set("os", LuaValue.NIL);
        globals.set("luajava", LuaValue.NIL);
        globals.set("debug", LuaValue.NIL);
        globals.set("newproxy", LuaValue.NIL);
        globals.set("__inext", LuaValue.NIL);

        globals.set("http", HttpLibrary.get());
        globals.set("editor", EditLibrary.get());
        globals.set("print", new Print());
        globals.set("sleep", new Sleep());

        //TODO JSON
        //TODO Websocket
        //TODO git clone

    }

    public void addLibrary(String name, LuaTable luaTable) {
        globals.set(name, luaTable);
    }

    static class Sleep extends OneArgFunction {

        @Override
        public LuaValue call(LuaValue luaValue) {
            try {
                Thread.sleep((long) luaValue.checkdouble() * 1000L);
                return LuaValue.valueOf(true);
            } catch (Exception e) {
                return LuaValue.valueOf(false);
            }
        }
    }

    static class Print extends OneArgFunction {
        @Override
        public LuaValue call(LuaValue luaValue) {
            if (luaValue.istable()) {
                LuaTable luaTable = luaValue.checktable();
                StringBuilder stringBuilder = new StringBuilder("[");
                for (LuaValue luaValue1 : luaTable.keys()) {
                    stringBuilder.append(luaValue1.tojstring()).append(",");
                }
                Bukkit.broadcastMessage(stringBuilder.subSequence(0, stringBuilder.length() - 1) + "]");
                return LuaValue.valueOf(true);
            }
            Bukkit.broadcastMessage(luaValue.tojstring());
            return LuaValue.valueOf(true);
        }
    }

    public String runFile(String fileName, Player player, @Nullable String args) {
        new Thread(() -> {
            try {
                LuaValue luaValue = getGlobals().loadfile(fileName);
                call(luaValue, args);
            } catch (LuaError error) {
                player.sendMessage(error.getMessage());
            }
        }).start();
        return "";
    }

    public String runCommand(String input, Player player, @Nullable String args) {
        new Thread(() -> {
            try {
                LuaValue luaValue = getGlobals().load(input);
                call(luaValue, args);
            } catch (LuaError error) {
                player.sendMessage(error.getMessage());
            }
        }).start();
        return "";
    }

    public static LuaValue toValue(Object object) {
        if (object == null) return LuaValue.NIL;
        else if (object instanceof Number) {
            double d = ((Number) object).doubleValue();
            return LuaValue.valueOf(d);

        } else if (object instanceof Boolean) {
            boolean b = (Boolean) object;
            return LuaValue.valueOf(b);

        } else if (object instanceof String) {
            String s = object.toString();
            return LuaValue.valueOf(s);
        } else if (object instanceof byte[]) {
            byte[] b = (byte[]) object;
            return LuaValue.valueOf(Arrays.copyOf(b, b.length));
        } else if (object instanceof Map) {
            LuaValue table = new LuaTable();

            for (Map.Entry<?, ?> pair : ((Map<?, ?>) object).entrySet()) {
                LuaValue key = toValue(pair.getKey());
                LuaValue value = toValue(pair.getValue());
                if (!key.isnil() && !value.isnil()) {
                    table.set(key, value);
                }
            }

            return table;
        } else {
            return LuaValue.NIL;
        }
    }
}
