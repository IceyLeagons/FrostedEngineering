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
			permission = net.iceyleagons.frostedengineering.other.permission.Permissions.NO_PERM)
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
