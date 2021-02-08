package net.iceyleagons.computercraft.lua.terminal.cmds;

import net.iceyleagons.computercraft.lua.LuaMachine;
import net.iceyleagons.computercraft.lua.terminal.TerminalCommand;
import org.bukkit.entity.Player;

/**
 * @author TOTHTOMI
 */
public class CurrentDirCommand implements TerminalCommand {

    @Override
    public String getCommand() {
        return "dir";
    }

    @Override
    public String[] getArgs() {
        return new String[0];
    }

    @Override
    public void onCommand(Player player, String[] args, LuaMachine luaMachine) {
        if (luaMachine.getDirectory().equals(luaMachine.getOpenDirectory())) {
            player.sendMessage("Currently in: root");
            return;
        }
        player.sendMessage("Currently in: " + luaMachine.getOpenDirectory().getName());
    }
}
