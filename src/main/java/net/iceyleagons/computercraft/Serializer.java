package net.iceyleagons.computercraft;

import net.iceyleagons.computercraft.lua.robot.Robot;
import net.iceyleagons.frostedengineering.api.addon.Addon;
import org.bukkit.entity.HumanEntity;

import java.io.File;
import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.Objects;

/**
 * @author TOTHTOMI
 */
public class Serializer {

    public static void serializeRobots(Addon addon) {
        File toSave = new File(addon.getDataFolder(), "computers"+File.separator+"data");
        if (!toSave.exists()) {
            if (!toSave.mkdirs()) throw new IllegalStateException("Could not create computer data folder!");
        }

        Robot.robots.forEach(robot -> {
            try {
                serializeRobot(robot, toSave);
            } catch (IOException e) {
                throw new IllegalStateException("Could not save robot (" + robot.getId() + ") data!", e);
            }
        });

    }

    public static void loadRobots(Addon addon) {
        File toSave = new File(addon.getDataFolder(), "computers"+File.separator+"data");
        if (!toSave.exists()) return; //no saved data, no need to continue

        for (File file : Objects.requireNonNull(toSave.listFiles())) {
            if (!file.exists()) continue; //making sure it exists!
            try {
                Robot.load(file, addon);
            } catch (IOException e) {
                addon.getFrostedEngineering().getLogger().warning("Could not load in robot!");
                e.printStackTrace();
            }
        }
    }

    private static void serializeRobot(Robot robot, File folder) throws IOException {
        File robotData = new File(folder, robot.getId()+".dat");
        if (!robotData.exists()) {
            if (!robotData.createNewFile()) throw new IllegalStateException("Could not create computer data folder!");
        }

        robot.getEntity().remove();
        try {
            robot.getInventory().getViewers().forEach(HumanEntity::closeInventory);
        } catch (ConcurrentModificationException ignored) {} //this exception will happen almost every time, but it's needed to prevent duping

        robot.serialize(robotData);
    }
}
