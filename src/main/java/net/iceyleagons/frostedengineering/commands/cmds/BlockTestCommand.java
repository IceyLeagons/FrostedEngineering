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

import net.iceyleagons.frostedengineering.vegetation.Tree;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.iceyleagons.frostedengineering.commands.CommandManager.Cmd;
import net.iceyleagons.frostedengineering.commands.CommandManager.CommandFinished;
import net.iceyleagons.frostedengineering.network.energy.ComponentManager;

public class BlockTestCommand {
    @Cmd(cmd = "blocktest", args = "", argTypes = {}, help = "Gives you a test block.", longhelp = "Gives you a test block.", permission = net.iceyleagons.frostedengineering.other.permission.Permissions.COMMAND_DEBUG)
    public static CommandFinished cmdTest(CommandSender sender, Object[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            Block b = player.getTargetBlock(10);
            if (b != null)
                new Tree(2, 2, 3, 3, 8, 10)
                        .place(b, new Tree.TreeParameters(Material.OAK_LOG, Material.OAK_LEAVES, Material.OAK_LOG,
                                3, 3, 100, 5, 3, true));
            //.getInventory().addItem(((TexturedCoalGenerator) ComponentManager.getComponent("fe:coalgenerator")).getItem());
            return CommandFinished.DONE;
        }
        return CommandFinished.NOCONSOLE;
    }

}
