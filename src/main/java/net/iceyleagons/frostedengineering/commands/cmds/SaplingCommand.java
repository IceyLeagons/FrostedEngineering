package net.iceyleagons.frostedengineering.commands.cmds;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.commands.CommandManager.Arg.ArgString;
import net.iceyleagons.frostedengineering.commands.CommandManager.Cmd;
import net.iceyleagons.frostedengineering.commands.CommandManager.CommandFinished;
import net.iceyleagons.frostedengineering.vegetation.Genes;

public class SaplingCommand {
	@Cmd(cmd = "sapling", args = "[sapling_type]", argTypes = {
			ArgString.class }, help = "Gives you a sapling.", longhelp = "Gives you a sapling of the specified tree.", permission = net.iceyleagons.frostedengineering.other.permission.Permissions.COMMAND_DEBUG)
	public static CommandFinished cmdTest(CommandSender sender, Object[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;

			player.getInventory().addItem(Genes.valueOf((String) args[0]).getItem());
			return CommandFinished.DONE;
		}
		return CommandFinished.NOCONSOLE;
	}

	public static void rightClick(BlockPlaceEvent e) {
		Location loc = e.getBlock().getLocation();
		Genes gene = Genes.isSaplingItem(e.getPlayer().getInventory().getItemInMainHand());
		if (gene != null) {
			Random random = new Random(1234567L);

			@SuppressWarnings("deprecation")
			int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.MAIN, new BukkitRunnable() {

				@Override
				public void run() {
					if (loc.getBlock().getType() != Material.OAK_SAPLING)
						this.cancel();
					else {

						loc.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc.getX() + .5D, loc.getY() + .5D,
								loc.getZ() + .5D, 20, 0.5D, 0.5D, 0.5D, 0, Material.OAK_SAPLING.createBlockData());

						loc.getWorld().playSound(loc, Sound.BLOCK_GRASS_BREAK, 1.f,
								random.nextFloat() * (.5f - 0.f) + 0.f);
					}
				}

			}, 0L, 20L);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.MAIN, new Runnable() {

				@Override
				public void run() {
					Bukkit.getScheduler().cancelTask(taskId);
					if (loc.getBlock().getType() == Material.OAK_SAPLING)
						gene.growPlantPhased(loc.add(new Vector(0, -1, 0)), 1L);
				}

			}, 200L);
		}
	}

}