package net.iceyleagons.computercraft.lua.terminal;

import lombok.Getter;
import net.iceyleagons.computercraft.lua.LuaMachine;
import net.iceyleagons.computercraft.lua.terminal.cmds.ChangeDirCommand;
import net.iceyleagons.computercraft.lua.terminal.cmds.CurrentDirCommand;
import net.iceyleagons.computercraft.lua.terminal.cmds.ListFilesCommand;
import net.iceyleagons.computercraft.lua.terminal.cmds.MakeDirCommand;
import net.iceyleagons.frostedengineering.api.IFrostedEngineering;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author TOTHTOMI
 */
public class Terminal implements Listener {

    private static final Map<Player, Terminal> interactingStatic = new HashMap<>();

    private boolean lua = false;
    @Getter
    private final LuaMachine luaMachine;
    private final List<Player> interacting = new ArrayList<>();
    private final Map<String, TerminalCommand> commands = new HashMap<>();

    public Terminal(final LuaMachine luaMachine, IFrostedEngineering iFrostedEngineering) {
        this.luaMachine = luaMachine;
        addCommands(new ChangeDirCommand(), new CurrentDirCommand(), new ListFilesCommand(), new MakeDirCommand());
        iFrostedEngineering.registerEventListener(this);
        Bukkit.getScheduler().runTaskTimer(iFrostedEngineering.getPlugin(), () -> {
            Location location = luaMachine.getLocation().clone().add(0, 2, 0);
            interacting.forEach(p ->
                    p.spawnParticle(Particle.VILLAGER_HAPPY, location, 3));

        }, 0L, 60L);
    }


    private void addCommands(TerminalCommand... terminalCommands) {
        for (TerminalCommand terminalCommand : terminalCommands)
            commands.put(terminalCommand.getCommand(), terminalCommand);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (interacting.contains(player)) {
            handleInput(event.getMessage(), player);
            event.setCancelled(true);
        }
        event.getRecipients().clear();
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        players.removeAll(interacting);
        players.forEach(p -> event.getRecipients().add(p));

    }

    public void add(Player player) {
        if (interactingStatic.containsKey(player)) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&fPlease close the terminal of &b" +
                    interactingStatic.get(player).getLuaMachine().getId() + " &ffirst!"));
            return;
        }

        clearChatForPlayer(player);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2&l&m==========&r&2&l[&fTerminal&r&2&l]&2&l&m==========\n"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&'," &f&lTo exit &r&7type: &r&bexit &r&7(in non-lua mode)"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&'," &7To &f&lenter lua &r&7mode type: &r&blua\n"));
        interacting.add(player);
        interactingStatic.put(player, this);
    }

    public void remove(Player player) {
        clearChatForPlayer(player);
        player.sendMessage("Exited terminal!\n\n");
        interacting.remove(player);
        interactingStatic.remove(player);
    }

    private void clearChatForPlayer(Player player) {
        for(int i=0; i < 30; i++) {
            player.sendMessage("");
        }
    }

    public void handleInput(String command, Player player) {
        player.sendMessage(" > " + command);

        String[] args = command.split(" ");
        if (lua) {
            if (command.equalsIgnoreCase("exit()")) {
                lua = false;
                player.sendMessage(" ");
                return;
            }
            luaMachine.runCommand(args[0], player, buildArguments(args));
            return;
        }

        if (args[0].equalsIgnoreCase("lua")) {
            player.sendMessage("Entered lua mode! To exit type exit()");
            lua = true;
        } else if (args[0].equalsIgnoreCase("exit")) {
            remove(player);
        } else if (commands.containsKey(args[0])){
            TerminalCommand terminalCommand = commands.get(args[0]);
            String[] supplyArgs = Arrays.copyOfRange(args, 1, args.length);
            String[] requiredArgs = terminalCommand.getArgs();

            if (supplyArgs.length < requiredArgs.length) {
                player.sendMessage("Too few parameters! Usage: \n" + terminalCommand.getCommand() + " " + String.join(" ", requiredArgs));
            } else if (supplyArgs.length > requiredArgs.length) {
                player.sendMessage("Too many parameters! Usage: \n" + terminalCommand.getCommand() + " " + String.join(" ", requiredArgs));
            } else {
                terminalCommand.onCommand(player, supplyArgs, getLuaMachine());
            }
        } else {
            if (new File(luaMachine.getDirectory(), args[0]).exists()) {
                player.sendMessage(luaMachine.runFile(args[0], player, buildArguments(args)));
            } else {
                player.sendMessage("File " + args[0] + " does not exist!");
            }
        }
    }

    private String buildArguments(String[] args) {
        StringBuilder arguments = new StringBuilder();

        //We don't want the first argument since that's the command/program itself
        for (int i = 1; i < args.length; i++) {
            arguments.append(args[i]).append(",");
        }

        return arguments.length() == 0 ? null : arguments.toString();
    }
}
