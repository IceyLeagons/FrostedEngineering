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
package net.iceyleagons.frostedengineering.modules.builtin;


import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import net.iceyleagons.frostedengineering.modules.annotations.Module;
import net.iceyleagons.frostedengineering.modules.annotations.ModuleMethods;
import net.iceyleagons.frostedengineering.modules.annotations.ModuleReturns;
import net.iceyleagons.frostedengineering.modules.enums.MethodTypes;
import net.iceyleagons.frostedengineering.modules.enums.ReturnTypes;

@Module(authors = {"TOTHTOMI"}, description = {"An Example Module"}, name = "ExampleModule")
public class ExampleModule {

    @ModuleMethods(type = MethodTypes.ON_LOAD)
    public void onModuleLoads() {

    }

    @ModuleMethods(type = MethodTypes.ON_ENABLE)
    public void onModuleEnables() {
        System.out.println("Module enabled!");
    }

    @ModuleMethods(type = MethodTypes.ON_DISABLE)
    public void onModuleDisables() {
        System.out.println("Module disabled!");
    }

    @ModuleMethods(type = MethodTypes.ON_TICK)
    public void onTick() {

    }

    @ModuleReturns(type = ReturnTypes.SETTINGS_INVENTORY)
    public Inventory getSettingsInventory() {
        return Bukkit.createInventory(null, 54, "ExampleModule");
    }

}
