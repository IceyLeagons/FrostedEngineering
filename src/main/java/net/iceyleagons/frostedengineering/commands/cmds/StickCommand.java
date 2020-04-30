/*******************************************************************************
 * Copyright (C) IceyLeagons(https://iceyleagons.net/) 
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iceyleagons.frostedengineering.commands.cmds;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.iceyleagons.frostedengineering.commands.CommandManager.Cmd;
import net.iceyleagons.frostedengineering.commands.CommandManager.CommandFinished;
import net.iceyleagons.frostedengineering.utils.ItemFactory;

public class StickCommand {

    public static ItemStack stick = new ItemFactory(Material.STICK).setDisplayName("TOTHTOMI's Debug stick").setUnbreakable(true).build();

    @Cmd(cmd = "stick", args = "", argTypes = {}, help = "Gives you a stick.", longhelp = "Gives you a debug stick.", permission = net.iceyleagons.frostedengineering.other.permission.Permissions.COMMAND_DEBUG)
    public static CommandFinished cmdStick(CommandSender sender, Object[] args) {
        if (!(sender instanceof Player))
            return CommandFinished.NOCONSOLE;

        Player p = (Player) sender;
        p.getInventory().addItem(stick.asOne());

        return CommandFinished.DONE;
    }

}
