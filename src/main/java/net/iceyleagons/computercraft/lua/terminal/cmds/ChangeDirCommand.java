package net.iceyleagons.computercraft.lua.terminal.cmds;

import net.iceyleagons.computercraft.lua.LuaMachine;
import net.iceyleagons.computercraft.lua.terminal.TerminalCommand;
import org.bukkit.entity.Player;

import java.io.File;

/**
 * @author TOTHTOMI
 */
public class ChangeDirCommand implements TerminalCommand {

    @Override
    public String getCommand() {
        return "cd";
    }

    @Override
    public String[] getArgs() {
        return new String[]{ "<folder>" };
    }

    @Override
    public void onCommand(Player player, String[] args, LuaMachine luaMachine) {
        if (args[0].equalsIgnoreCase("..")) {
            if (luaMachine.getDirectory().equals(luaMachine.getOpenDirectory())) {
                player.sendMessage("Already in root, nowhere to go.");
                return;
            }
            File parent = luaMachine.getOpenDirectory().getParentFile();
            if (!parent.isDirectory()) {
                player.sendMessage("Not a directory.");
                return;
            }
            if (parent.equals(luaMachine.getDirectory()))
                player.sendMessage("Switched to root directory");
            else
                player.sendMessage("Switched to directory " + parent.getName());

            luaMachine.setOpenDirectory(parent);

        } else {
            File file = new File(luaMachine.getOpenDirectory(), args[0]);
            if (!file.exists()) {
                player.sendMessage("Missing directory!");
                return;
            }
            if (!file.isDirectory()) {
                player.sendMessage("Not directory.");
                return;
            }
            player.sendMessage("Switched to folder " + file.getName());
            luaMachine.setOpenDirectory(file);
        }
    }
}
