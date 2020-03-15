package net.iceyleagons.frostedengineering.commands.cmds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.commands.CommandManager.Arg.ArgInteger;
import net.iceyleagons.frostedengineering.commands.CommandManager.Arg.ArgString;
import net.iceyleagons.frostedengineering.commands.CommandManager.Cmd;
import net.iceyleagons.frostedengineering.commands.CommandManager.CommandFinished;
import net.iceyleagons.frostedengineering.utils.Cuboid;
import net.iceyleagons.frostedengineering.utils.festruct.FEStructSaver;

public class SaveStructCommand {

	@Cmd(cmd = "savestruct", args = "<name> <rarity> [monsters]", argTypes = { ArgString.class, ArgInteger.class,
			ArgString.class }, help = "Save the selected region as an festruct file.", longhelp = "Save the selected region with all it's required details in order to save it to an festurct file so we can use it in dungeons etc.", permission = net.iceyleagons.frostedengineering.other.permission.Permissions.COMMAND_SAVESTRUCT)
	public static CommandFinished cmdSaveStruct(CommandSender sender, Object[] args) {
		if (!(sender instanceof Player))
			return CommandFinished.NOCONSOLE;

		Player p = (Player) sender;

		WorldEditPlugin wep = Main.MAIN.getWorldEdit();
		if (wep == null) {
			sender.sendMessage("Please install WorldEdit on your server first!");
			return CommandFinished.DONE;
		}
		int rarity = (int)args[1];
		List<String> entities = new ArrayList<String>();
		if (args.length == 3) {
			if (args[1] != null) {
				String s = (String) args[2];
				String[] ents = s.split(",");
				for (int i = 0; i < ents.length; i++) {
					try {
						if (EntityType.valueOf(ents[i].toUpperCase()) != null) {
							entities.add("minecraft:" + ents[i]);
						}
					} catch (IllegalArgumentException ex) {
						sender.sendMessage(ents[i] + " is not a valid entity!");
						return CommandFinished.DONE;
					}
				}
			}
		}
		String name = (String) args[0];
		try {

			Region sel = wep.getSession(p).getSelection(BukkitAdapter.adapt(p.getWorld()));
			BlockVector3 min = sel.getMinimumPoint();
			BlockVector3 max = sel.getMaximumPoint();

			Location minl = new Location(p.getWorld(), min.getBlockX(), min.getBlockY(), min.getBlockZ());
			Location maxl = new Location(p.getWorld(), max.getBlockX(), max.getBlockY(), max.getBlockZ());

			Cuboid c = new Cuboid(minl, maxl);

			FEStructSaver saver = new FEStructSaver(c, name, entities,rarity);
			saver.save(p);

		} catch (IncompleteRegionException e) {
			sender.sendMessage("Please select a structure with worldedit!");
			return CommandFinished.DONE;
		}

		return CommandFinished.DONE;
	}
}
