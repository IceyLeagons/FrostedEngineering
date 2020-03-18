package net.iceyleagons.frostedengineering.commands.cmds;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.commands.CommandManager.Arg.ArgString;
import net.iceyleagons.frostedengineering.commands.CommandManager.Cmd;
import net.iceyleagons.frostedengineering.commands.CommandManager.CommandFinished;

public class GiveCommand {

	@Cmd(cmd = "give", args = "[item_name]", argTypes = {
			ArgString.class }, help = "Gives you the specified item.", longhelp = "Gives you an item of the specified type.", permission = net.iceyleagons.frostedengineering.other.permission.Permissions.COMMAND_GIVE)
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
