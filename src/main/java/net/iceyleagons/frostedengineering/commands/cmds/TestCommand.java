package net.iceyleagons.frostedengineering.commands.cmds;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.iceyleagons.frostedengineering.commands.CommandManager.Cmd;
import net.iceyleagons.frostedengineering.commands.CommandManager.CommandFinished;
import net.iceyleagons.frostedengineering.entity.Registry;
import net.iceyleagons.frostedengineering.gui.installer.Install;

public class TestCommand {
	
	@Cmd(cmd = "test",
			args = "",
			argTypes = {},
			help = "Test.",
			longhelp = "Test.",
			permission = net.iceyleagons.frostedengineering.other.permission.Permissions.COMMAND_DEBUG)
	public static CommandFinished cmdTest(CommandSender sender, Object[] args) {
		Registry.spawnMob(Registry.CUSTOM_ZOMBIE, ((Player)sender).getLocation());
		return CommandFinished.DONE;
	}

}
