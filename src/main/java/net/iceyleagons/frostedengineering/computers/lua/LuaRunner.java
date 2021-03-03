/*
 *  Copyright (C) IceyLeagons(https://iceyleagons.net/)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package net.iceyleagons.frostedengineering.computers.lua;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

/**
 * @author TOTHTOMI
 */
public abstract class LuaRunner {

    public Globals getGlobals() {
        Globals globals = org.luaj.vm2.lib.jse.JsePlatform.standardGlobals();
        hideGlobals(globals);
        return globals;
    }

    public LuaValue loadCode(String code,Globals globals) {
        return globals.load(code);
    }

    private void hideGlobals(Globals globals) {
        globals.set( "collectgarbage", LuaValue.NIL );
        globals.set( "dofile", LuaValue.NIL );
        globals.set( "loadfile", LuaValue.NIL );
        globals.set( "module", LuaValue.NIL );
        globals.set( "require", LuaValue.NIL );
        globals.set( "package", LuaValue.NIL );
        globals.set( "io", LuaValue.NIL );
        globals.set( "os", LuaValue.NIL );
        globals.set( "print", LuaValue.NIL );
        globals.set( "luajava", LuaValue.NIL );
        globals.set( "debug", LuaValue.NIL );
        globals.set( "newproxy", LuaValue.NIL );
        globals.set( "__inext", LuaValue.NIL );
    }

}
