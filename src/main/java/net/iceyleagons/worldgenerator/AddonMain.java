package net.iceyleagons.worldgenerator;

import lombok.SneakyThrows;
import net.iceyleagons.frostedengineering.api.addon.impl.FrostedAddon;
import net.iceyleagons.icicle.misc.commands.CommandUtils;
import net.iceyleagons.worldgenerator.api.APIProvider;
import net.iceyleagons.worldgenerator.api.WorldGeneratorAPI;
import net.iceyleagons.worldgenerator.generator.FrostedChunkGenerator;
import net.iceyleagons.worldgenerator.trees.StandardClusters;
import net.iceyleagons.worldgenerator.trees.Trees;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class AddonMain extends FrostedAddon implements IMain, CommandExecutor {

    @SneakyThrows
    @Override
    public void onLoad() {
        WorldGenerator.MAIN = this;

        WorldGenerator.overwriteGenerator(Bukkit.getServer(), "world");

        getFrostedEngineering().registerAPI(WorldGeneratorAPI.class, new APIProvider(), this);
    }

    @SneakyThrows
    @Override
    public void onEnable() {
        registerListener(new Listeners());
        CommandUtils.injectCommand("test", this);
    }

    @Override
    public ChunkGenerator getDefaultChunkGenerator(String worldName, String id) {
        return new FrostedChunkGenerator();
    }

    @Override
    public void onDisable() {
    }

    @Override
    public Material getIcon() {
        return Material.GRASS_BLOCK;
    }

    @Override
    public void openSettingsMenu(Player player) {

    }

    @Override
    public JavaPlugin getPlugin() {
        return getFrostedEngineering().getPlugin();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player))
            return false;

        Trees.create(((Player) sender).getLocation(), Trees.TreeConstructor.Builder.create(StandardClusters.OAK).build()).place();

        return true;
    }
}
