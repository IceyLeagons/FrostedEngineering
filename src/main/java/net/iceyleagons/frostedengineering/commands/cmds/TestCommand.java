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

import java.io.File;
import java.io.FileNotFoundException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.iceyleagons.frostedengineering.commands.CommandManager.Arg.ArgInteger;
import net.iceyleagons.frostedengineering.commands.CommandManager.Cmd;
import net.iceyleagons.frostedengineering.commands.CommandManager.CommandFinished;
import old.particles.ParticleManager;
import net.iceyleagons.frostedengineering.utils.festruct.FEStruct;

public class TestCommand {

    @Cmd(cmd = "test",
            args = "<id> [id - only effects]",
            argTypes = {ArgInteger.class},
            help = "Test.",
            longhelp = "Test.",
            permission = net.iceyleagons.frostedengineering.other.permission.Permissions.COMMAND_DEBUG)
    public static CommandFinished cmdTest(CommandSender sender, Object[] args) {
        //Registry.spawnMob(Registry.CUSTOM_ZOMBIE, ((Player)sender).getLocation());
        int id = (int) args[0];
        try {
            Player p = (Player) sender;
            FEStruct s = new FEStruct(new File("consept.festruct")).load();
            if (id == 1) {
                ParticleManager.run(Integer.parseInt((String) args[1]), p.getLocation());
            } else if (id == 2) {
                s.pasteToLocation(p.getLocation());
            } else {
                sender.sendMessage("There is no test with this id!");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return CommandFinished.DONE;
    }

}
