package net.iceyleagons.computercraft;

import lombok.SneakyThrows;
import net.iceyleagons.computercraft.api.APIProvider;
import net.iceyleagons.computercraft.api.ComputersAPI;
import net.iceyleagons.computercraft.lua.robot.Robot;
import net.iceyleagons.computercraft.web.WebIDE;
import net.iceyleagons.frostedengineering.api.addon.impl.FrostedAddon;
import net.iceyleagons.icicle.misc.commands.CommandUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author TOTHTOMI
 */
public class Main extends FrostedAddon {

    @SneakyThrows
    @Override
    public void onEnable() {
        System.out.println("Loaded computercraft addon!");
        WebIDE.init(super.getFrostedEngineering().getPlugin());
        CommandUtils.injectCommand("spawn", (commandSender, command, s, strings) -> {
            if (!(commandSender instanceof Player)) return true;

            if (command.getName().equalsIgnoreCase(("spawn"))) {
                new Robot(((Player)commandSender).getLocation(), this);
                //new Robot(((Player)commandSender).getLocation(), this);
            }
            return false;
        });
        getFrostedEngineering().registerAPI(ComputersAPI.class, new APIProvider(), this);

        Serializer.loadRobots(this);
    }

    @Override
    public void onDisable() {
        Serializer.serializeRobots(this);
    }

    @Override
    public Material getIcon() {
        return Material.COMMAND_BLOCK;
    }

    @Override
    public void openSettingsMenu(Player player) {

    }


}
