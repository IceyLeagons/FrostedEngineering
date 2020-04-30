package net.iceyleagons.frostedengineering.quests;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Villager.Type;
import org.bukkit.inventory.MerchantRecipe;

public class QuestNPC {

    private static Villager villager = null;
    private static ArmorStand text = null;
    private static World world;
    private static Location loc;

    static {
        world = Bukkit.getServer().getWorld("island");
        loc = new Location(world, -38.538, 69.00, -50.236, -159.2f, 6.4f);
    }

    private static boolean loaded = false;

    public static void spawn() {
        if (loaded)
            return;
        loaded = true;
        villager = (Villager) world.spawnEntity(loc, EntityType.VILLAGER);
        villager.setSilent(true);
        villager.setAI(false);
        villager.setAdult();
        villager.setCanPickupItems(false);
        villager.setInvulnerable(true);
        villager.setProfession(Profession.LIBRARIAN);
        villager.setVillagerType(Type.PLAINS);
        villager.setRecipes(new ArrayList<MerchantRecipe>());
        villager.setCustomNameVisible(true);
        villager.setCustomName("Mason");

        text = (ArmorStand) world.spawnEntity(loc.add(0, 3.5, 0), EntityType.ARMOR_STAND);
        text.setInvulnerable(true);
        text.setVisible(false);
        text.setCustomNameVisible(true);
        text.setCustomName("§cGet a quest here!");

    }

    public static void despawn() {
        villager.remove();
        text.remove();
    }

}
