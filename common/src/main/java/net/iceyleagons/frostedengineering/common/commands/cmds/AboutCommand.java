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

import net.iceyleagons.frostedengineering.commands.CommandManager.Cmd;
import net.iceyleagons.frostedengineering.commands.CommandManager.CommandFinished;

public class AboutCommand {

    @Cmd(cmd = "about",
            args = "",
            argTypes = {},
            help = "About page.",
            longhelp = "Want to know more about this plugin? Use this command!",
            permission = net.iceyleagons.frostedengineering.common.other.permission.Permissions.NO_PERM)
    public static CommandFinished cmdAbout(CommandSender sender, Object[] args) {
        sender.sendMessage(" ");
        sender.sendMessage("§1§l-§9§l=§1§l-§9§l=§1§l-§9§l=§b§l[§r§cAbout§b§l]§1§l-§9§l=§1§l-§9§l=§b§l§1§l-§9§l=");
        sender.sendMessage(" ");
        sender.sendMessage("§9- §bDeveloped and maintained by: §cG4be_§b & §cTOTHTOMI");
        sender.sendMessage("§9- §bLead bug tester: §cG4be_");
        sender.sendMessage("§9- §bWebsite guy: §cTOTHTOMI");
        sender.sendMessage("§9- §bQuality assurance: §cG4be_");

        return CommandFinished.DONE;
    }

}
