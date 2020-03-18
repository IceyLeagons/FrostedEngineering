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
