package net.iceyleagons.frostedengineering.commands.cmds;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.commands.CommandManager.Cmd;
import net.iceyleagons.frostedengineering.commands.CommandManager.CommandFinished;

public class BlockTestCommand {
	@Cmd(cmd = "blocktest", args = "", argTypes = {}, help = "Gives you a test block.", longhelp = "Gives you a test block.", permission = net.iceyleagons.frostedengineering.other.permission.Permissions.COMMAND_DEBUG)
	public static CommandFinished cmdTest(CommandSender sender, Object[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;

			player.getInventory().addItem(Main.customBases.get(0).getItem());
			return CommandFinished.DONE;
		}
		return CommandFinished.NOCONSOLE;
	}

}