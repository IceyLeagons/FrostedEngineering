package net.iceyleagons.computercraft.lua.terminal;

import net.iceyleagons.computercraft.lua.LuaMachine;
import org.bukkit.entity.Player;

/**
 * @author TOTHTOMI
 */
public interface TerminalCommand {

    String getCommand();
    String[] getArgs();

    void onCommand(Player player, String[] args, LuaMachine luaMachine);

}
