package net.iceyleagons.frostedengineering.commands.cmds;

import java.io.File;
import java.io.FileNotFoundException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.iceyleagons.frostedengineering.commands.CommandManager.Arg.ArgInteger;
import net.iceyleagons.frostedengineering.commands.CommandManager.Cmd;
import net.iceyleagons.frostedengineering.commands.CommandManager.CommandFinished;
import net.iceyleagons.frostedengineering.utils.festruct.FEStruct;

public class TestCommand {
	
	@Cmd(cmd = "test",
			args = "<id>",
			argTypes = {ArgInteger.class},
			help = "Test.",
			longhelp = "Test.",
			permission = net.iceyleagons.frostedengineering.other.permission.Permissions.COMMAND_DEBUG)
	public static CommandFinished cmdTest(CommandSender sender, Object[] args) {
		//Registry.spawnMob(Registry.CUSTOM_ZOMBIE, ((Player)sender).getLocation());
		int id = (int) args[0];
		try {
			Player p = (Player)sender;
			FEStruct s = new FEStruct(new File("consept.festruct")).load();
			if (id == 1) {
				s.pasteToLocationBlockByBlock(p.getLocation());
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
