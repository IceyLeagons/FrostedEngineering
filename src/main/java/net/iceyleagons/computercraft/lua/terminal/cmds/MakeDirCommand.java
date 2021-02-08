package net.iceyleagons.computercraft.lua.terminal.cmds;

import net.iceyleagons.computercraft.lua.LuaMachine;
import net.iceyleagons.computercraft.lua.terminal.TerminalCommand;
import org.bukkit.entity.Player;

import java.io.File;

/**
 * @author TOTHTOMI
 */
public class MakeDirCommand implements TerminalCommand {

    @Override
    public String getCommand() {
        return "mkdir";
    }

    @Override
    public String[] getArgs() {
        return new String[]{"<name>"};
    }

    @Override
    public void onCommand(Player player, String[] args, LuaMachine luaMachine) {
        File file = new File(luaMachine.getOpenDirectory(), args[0]);
        if (file.exists()) {
            player.sendMessage("Directory already exists!");
            return;
        }
        if (!file.mkdir()) {
            player.sendMessage("Could not create directory");
        } else {
            player.sendMessage("Created directory named: " + args[0]);
        }
    }
}
