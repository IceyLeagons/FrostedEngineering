package net.iceyleagons.computercraft.lua.terminal.cmds;

import net.iceyleagons.computercraft.lua.LuaMachine;
import net.iceyleagons.computercraft.lua.terminal.TerminalCommand;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Objects;

/**
 * @author TOTHTOMI
 */
public class ListFilesCommand implements TerminalCommand {

    @Override
    public String getCommand() {
        return "ls";
    }

    @Override
    public String[] getArgs() {
        return new String[0];
    }

    @Override
    public void onCommand(Player player, String[] args, LuaMachine luaMachine) {
        if (luaMachine.getOpenDirectory() == null) {
            player.sendMessage("This error should not be happening, try restarting the server!");
            return;
        }

        if (luaMachine.getOpenDirectory().listFiles() == null) {
            player.sendMessage("Directory is empty!");
            return;
        }

        File[] files = Objects.requireNonNull(luaMachine.getOpenDirectory().listFiles());
        if (files.length == 0) {
            player.sendMessage("Directory is empty!");
            return;
        }
        String[] names = new String[files.length];
        for (int i = 0; i < files.length; i++) names[i] = files[i].getName();

        player.sendMessage(String.join(", ", names));
    }
}
