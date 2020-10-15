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
package net.iceyleagons.frostedengineering.commands.cmds;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.commands.CommandManager.Arg.ArgString;
import net.iceyleagons.frostedengineering.commands.CommandManager.Cmd;
import net.iceyleagons.frostedengineering.commands.CommandManager.CommandFinished;

public class GiveCommand {

    @Cmd(cmd = "give", args = "[item_name]", argTypes = {
            ArgString.class}, help = "Gives you the specified item.", longhelp = "Gives you an item of the specified type.", permission = net.iceyleagons.frostedengineering.common.other.permission.Permissions.COMMAND_GIVE)
    public static CommandFinished cmd(CommandSender sender, Object[] args) {
        Main.customBases.forEach((base) -> {
            if (base.getName().equalsIgnoreCase((String) args[0])) {
                if (sender instanceof Player) {
                    ((Player) sender).getInventory().addItem(base.getItem());
                }
            }
        });

        return CommandFinished.DONE;
    }
}
