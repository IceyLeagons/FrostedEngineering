package net.iceyleagons.frostedengineering;

import net.iceyleagons.frostedengineering.api.IFrostedEngineering;
import net.iceyleagons.frostedengineering.addon.AddonGUI;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author TOTHTOMI
 */
public class Command implements CommandExecutor, TabCompleter {

    private final AddonGUI addonGUI;
    private final IFrostedEngineering iFrostedEngineering;

    public Command(AddonGUI addonGUI, IFrostedEngineering iFrostedEngineering) {
        this.addonGUI = addonGUI;
        this.iFrostedEngineering = iFrostedEngineering;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        String subcommand = args[0];
        if (subcommand.equalsIgnoreCase("about")) {
            for (String s : getAbout()) {
                sendMessage(sender, s);
            }
        } else if (subcommand.equalsIgnoreCase("debug")) {
            iFrostedEngineering.setDebugEnabled(!iFrostedEngineering.isDebuggingEnabled());
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fDebugging is now " + (iFrostedEngineering.isDebuggingEnabled() ? "&aenabled&f!" : "&cdisabled&f!")));
        } else if (subcommand.equalsIgnoreCase("addons")) {
            if (!(sender instanceof Player)) sender.sendMessage("Player only command!");
            else {
                addonGUI.openForPlayers((Player) sender);
            }
        } else {
            sendHelp(sender);
        }
        return true;
    }

    private void sendHelp(CommandSender sender) {
        sendMessage(sender, "Commands: \n&b/fe about\n&b/fe addons\n&b/fe debug");
    }

    private static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public String[] getAbout() {
        return new String[]{
                "&8&l&m====[&r&bFrosted&fEngineering&r&8&l&m]====",
                " &fVersion:        &bv"+iFrostedEngineering.getVersion(),
                " &fAuthors:        &bG4be_",
                "                   &bTOTHTOMI",
                " ",
                " &fWebsite:        &bhttps://iceyleagons.net/",
                " &fGithub:         &bhttps://github.iceyleagons.net/",
                " &fDiscord:        &bhttps://discord.iceyleagons.net/",
                "&8&l&m============[ ]============"

        };
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String alias, @NotNull String[] args) {
        final List<String> completions = new ArrayList<>();
        StringUtil.copyPartialMatches(args[0], Arrays.asList("about", "addons"), completions);
        Collections.sort(completions);
        return completions;
    }
}
