package net.iceyleagons.frostedengineering.commands.cmds;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.iceyleagons.frostedengineering.commands.CommandManager.Cmd;
import net.iceyleagons.frostedengineering.commands.CommandManager.CommandFinished;
import net.iceyleagons.frostedengineering.commands.CommandManager.CommandOnly;
import net.iceyleagons.frostedengineering.items.CreativeMode;

public class CreativeCommand {
	@Cmd(cmd = "creative", args = "", argTypes = {}, help = "Sets your gamemode to Creative FE.", longhelp = "Sets your gamemode to Creative FE.", permission = net.iceyleagons.frostedengineering.other.permission.Permissions.COMMAND_GIVE, only = CommandOnly.PLAYER)
	public static CommandFinished cmd(CommandSender sender, Object[] args) {
		CreativeMode.open((Player) sender);
		return CommandFinished.DONE;
	}
}
