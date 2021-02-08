package net.iceyleagons.computercraft.lua.robot;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;
import net.iceyleagons.computercraft.lua.LuaMachine;
import net.iceyleagons.computercraft.lua.library.libs.RobotLibrary;
import net.iceyleagons.computercraft.lua.robot.components.InventoryComponent;
import net.iceyleagons.computercraft.lua.robot.components.MeleeComponent;
import net.iceyleagons.computercraft.lua.robot.components.MinerComponent;
import net.iceyleagons.frostedengineering.api.addon.Addon;
import net.iceyleagons.icicle.item.ItemUtils;
import net.iceyleagons.icicle.jnbt.ByteArrayTag;
import net.iceyleagons.icicle.jnbt.CompoundTag;
import net.iceyleagons.icicle.jnbt.IntTag;
import net.iceyleagons.icicle.jnbt.NBTInputStream;
import net.iceyleagons.icicle.jnbt.NBTOutputStream;
import net.iceyleagons.icicle.jnbt.StringTag;
import net.iceyleagons.icicle.jnbt.Tag;
import net.iceyleagons.icicle.location.BlockUtils;
import net.iceyleagons.icicle.location.LocationUtils;
import net.iceyleagons.icicle.math.MathUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * @author TOTHTOMI
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class Robot extends LuaMachine implements InventoryHolder, Listener {

    //TODO Inventory (smelt, craft)
    //TODO CLOSE INVENTORY WHEN SERVE CLOSES -> PREVENT duplication
    //TODO Block placement
    //TODO only one terminal open
    //TODO if zombie dies handle it (kill -> respawn)!

    public static final long ACTION_TIMEOUT = 250;
    public static List<Robot> robots = new ArrayList<>();


    /*=============================================================*/

    private Location location;
    private final JavaPlugin javaPlugin;
    private final Inventory inventory;
    private Zombie entity;
    private BlockFace blockFace;
    private final List<Component> components = new ArrayList<>();

    private String name;

    private int selectedSlot = 0;

    public Robot(Location location, Addon addon) {
        super(addon);
        robots.add(this);
        this.location = location;
        this.name = "#" + getId();
        this.javaPlugin = addon.getFrostedEngineering().getPlugin();
        this.inventory = Bukkit.createInventory(this, 27, "Robot");

        setupLocation(location);

        setup();
    }

    public Robot(Location location, Addon addon, int id, ItemStack[] inventory, String name, int notch, int slot) {
        super(addon, id);
        robots.add(this);
        this.javaPlugin = addon.getFrostedEngineering().getPlugin();
        this.name = name;
        this.inventory = Bukkit.createInventory(this, 27, "Robot");
        this.inventory.setContents(inventory);
        this.selectedSlot = slot;

        setupLocation(location);
        this.blockFace = BlockUtils.notchToFace(notch);

        setup();
    }

    private void setup() {
        doSync(() -> {
            this.entity = (Zombie) Objects.requireNonNull(location.getWorld()).spawnEntity(location, EntityType.ZOMBIE);
            setupEntity();
        });
        setupComponents();

        javaPlugin.getServer().getPluginManager().registerEvents(this, javaPlugin);
        setupLibrary();
    }

    private void setupComponents() {
        components.add(new MeleeComponent(this));
        components.add(new MinerComponent(this));
        components.add(new InventoryComponent(this));
    }

    private void setupLibrary() {
        RobotLibrary robotLibrary = new RobotLibrary(this);

        components.forEach(c -> c.addLibraryFields(robotLibrary.getLibraryBuilder()));
        addLibrary("robot", robotLibrary.get());
    }


    private void setupLocation(Location location) {
        this.location = new Location(location.getWorld(), location.getBlockX() + 0.5, location.getBlockY(), location.getBlockZ() + 0.5);
        this.location.setDirection(location.getDirection());
        this.blockFace = BlockUtils.AXIS[Math.round(location.getYaw() / 90f) & 0x3];
        this.blockFace = BlockUtils.rotate(blockFace, -2);
    }

    private void setupEntity() {
        EntityEquipment entityEquipment = Objects.requireNonNull(entity.getEquipment());
        entityEquipment.setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE));
        entityEquipment.setHelmet(new ItemStack(Material.WITHER_SKELETON_SKULL));
        entityEquipment.setItemInMainHand(new ItemStack(Material.AIR));

        entity.setBaby();
        entity.setAI(false);
        entity.setSilent(true);
        entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, true, false));
        entity.setCustomName(name);
        entity.setCollidable(false);
        entity.setCustomNameVisible(true);
        entity.setGravity(false);
        entity.setInvulnerable(true);
        entity.teleport(location);
        switchItemInHand();
    }

    public void switchItemInHand() {
        ItemStack itemStack = getInventory().getItem(selectedSlot);
        Material material = itemStack != null ? itemStack.getType() : Material.AIR;

        Objects.requireNonNull(entity.getEquipment()).setItemInMainHand(new ItemStack(material));
    }

    public boolean selectSlot(int slot) {
        slot -= 1;
        if (slot < 0) return false;
        if (inventory.getSize() <= slot) return false;
        selectedSlot = slot;
        switchItemInHand();
        return true;
    }

    public Block getBlock(BlockFace blockFace) {
        return location.clone().add(blockFace.getModX(), blockFace.getModY(), blockFace.getModZ()).getBlock();
    }

    public void doSync(Runnable runnable) {
        Bukkit.getScheduler().runTask(javaPlugin, runnable);
    }

    public boolean place(int direction) {
        ItemStack itemStack = this.inventory.getItem(selectedSlot);
        if (itemStack == null) return false;
        Material material = itemStack.getType();
        int amount = itemStack.getAmount();

        Block block;
        if (direction == 2) block = getBlock(BlockFace.UP);
        else if (direction == 3) block = getBlock(BlockFace.DOWN);
        else block = getBlock(blockFace);

        if (!block.isEmpty()) return false;


        if (material.isBlock()) {
            doSync(() -> block.setType(material));

            if (amount - 1 <= 0) {
                this.inventory.setItem(selectedSlot, new ItemStack(Material.AIR));
            }
            itemStack.setAmount(amount - 1);
            switchItemInHand();
            return true;
        } else if (material == Material.WATER_BUCKET) {
            doSync(() -> block.setType(Material.WATER));
            this.inventory.setItem(selectedSlot, new ItemStack(Material.BUCKET));
            switchItemInHand();
            return true;
        } else if (material == Material.LAVA_BUCKET) {
            doSync(() -> block.setType(Material.LAVA));
            this.inventory.setItem(selectedSlot, new ItemStack(Material.BUCKET));
            switchItemInHand();
            return true;
        }

        return false;
    }

    private void lookAt(Location before, Location after) {
        Vector vector = MathUtils.getVector(before, after);
        before.setDirection(vector);
        entity.teleport(before);
    }

    protected void move(int x, int y, int z) {
        final Location before = location.clone();
        location = location.add(x, y, z);
        lookAt(before, location);
        entity.teleport(location);
    }

    @SneakyThrows
    public boolean goUp(int blocks) {
        BlockFace blockFace = BlockFace.UP;
        for (int i = 0; i < blocks; i++) {

            Block above = getBlock(blockFace);
            if (!above.isEmpty()) return false;
            if (above.getLocation().getY() >= Objects.requireNonNull(above.getLocation().getWorld()).getMaxHeight())
                return false;

            move(blockFace.getModX(), blockFace.getModY(), blockFace.getModZ());
            Thread.sleep(ACTION_TIMEOUT);
        }
        return true;
    }

    @SneakyThrows
    public boolean goDown(int blocks) {
        BlockFace blockFace = BlockFace.DOWN;
        for (int i = 0; i < blocks; i++) {

            Block above = getBlock(blockFace);
            if (!above.isEmpty()) return false;

            move(blockFace.getModX(), blockFace.getModY(), blockFace.getModZ());
            Thread.sleep(ACTION_TIMEOUT);
        }
        return true;
    }

    @SneakyThrows
    public boolean move(int blocks) {
        for (int i = 0; i < blocks; i++) {
            Block front = getBlock(this.blockFace);
            if (front.getType().isSolid()) return false;
            move(this.blockFace.getModX(), this.blockFace.getModY(), this.blockFace.getModZ());

            Thread.sleep(ACTION_TIMEOUT);
        }
        return true;
    }

    @SneakyThrows
    public boolean turnRight(int times) {
        for (int i = 0; i < times; i++) {
            blockFace = BlockUtils.rotate(blockFace, 2);
            lookAt(location, location.clone().add(blockFace.getModX(), blockFace.getModY(), blockFace.getModZ()));
            Thread.sleep(ACTION_TIMEOUT);
        }
        return true;
    }

    @SneakyThrows
    public boolean turnLeft(int times) {
        for (int i = 0; i < times; i++) {
            blockFace = BlockUtils.rotate(blockFace, -2);
            lookAt(location, location.clone().add(blockFace.getModX(), blockFace.getModY(), blockFace.getModZ()));
            Thread.sleep(ACTION_TIMEOUT);
        }
        return true;
    }

    public boolean canBreak(Block block) {
        return !(block.getType() == Material.BEDROCK || block.getType() == Material.COMMAND_BLOCK);
    }

    //Bone meal, flint and steel, buckets etc.
    @SneakyThrows
    public boolean useItem() {
        ItemStack itemStack = inventory.getItem(selectedSlot);
        Block front = getBlock(this.blockFace);
        if (itemStack == null || itemStack.getType().isAir()) return false;
        CompletableFuture<Boolean> result = new CompletableFuture<>();


        doSync(() -> {
            switch (itemStack.getType()) {
                case BUCKET:
                    if (!front.isLiquid()) {
                        result.complete(false);
                        break;
                    }
                    if (front.getType() == Material.WATER) {
                        if (itemStack.getAmount() == 1)
                            itemStack.setType(Material.WATER_BUCKET);
                        else {
                            itemStack.setAmount(itemStack.getAmount() - 1);
                            this.inventory.addItem(new ItemStack(Material.WATER_BUCKET));
                        }
                        front.setType(Material.AIR);
                        switchItemInHand();
                        result.complete(true);
                        break;
                    } else if (front.getType() == Material.LAVA) {
                        if (itemStack.getAmount() == 1)
                            itemStack.setType(Material.LAVA_BUCKET);
                        else {
                            itemStack.setAmount(itemStack.getAmount() - 1);
                            this.inventory.addItem(new ItemStack(Material.LAVA_BUCKET));
                        }
                        front.setType(Material.AIR);
                        switchItemInHand();
                        result.complete(true);
                        break;
                    }
                case BONE_MEAL:
                    if (!front.isEmpty()) {
                        front.applyBoneMeal(BlockFace.UP);
                        switchItemInHand();
                        result.complete(true);
                        break;
                    }
                    front.getRelative(BlockFace.DOWN).applyBoneMeal(BlockFace.UP);
                    switchItemInHand();
                    result.complete(true);
                    break;
                default:
                    result.complete(false);
                    break;
            }
        });

        Thread.sleep(ACTION_TIMEOUT);
        Boolean res = result.join();
        return res != null ? res : false;
    }


    @NotNull
    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    @EventHandler(priority =  EventPriority.MONITOR)
    public void onInventoryClick(InventoryClickEvent event) {
        if (Objects.equals(event.getClickedInventory(), getInventory())) {
            switchItemInHand();
        } else if (Objects.equals(event.getInventory(), getInventory())) {
            switchItemInHand();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRobotClicked(PlayerInteractAtEntityEvent event) {
        Entity entity = event.getRightClicked();
        Player player = event.getPlayer();

        if (!(entity.getLocation().distance(location) < 1.2)) return;

        if (player.isSneaking()) {
            super.getTerminal().add(player);
            return;
        }
        player.openInventory(inventory);
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("loc", LocationUtils.serializeLocation(getLocation(), ';')); //String
        jsonObject.put("id", getId()); //int
        jsonObject.put("name", getName()); //String
        jsonObject.put("notch", BlockUtils.faceToNotch(getBlockFace())); //int
        jsonObject.put("slot", getSelectedSlot()); //int
        jsonObject.put("inv", ItemUtils.toBase64(getInventory().getContents())); //String
        return jsonObject.toString();
    }

    public void serialize(File file) throws IOException {
        Map<String, Tag> tags = new HashMap<>();
        tags.put("loc", new StringTag("loc", LocationUtils.serializeLocation(getLocation(), ';')));
        tags.put("id", new IntTag("id", getId()));
        tags.put("name", new StringTag("name", getName()));
        tags.put("notch", new IntTag("notch", BlockUtils.faceToNotch(getBlockFace())));
        tags.put("slot", new IntTag("slot", getSelectedSlot()));
        tags.put("inv", new ByteArrayTag("inv", ItemUtils.toByteArray(getInventory().getContents())));

        CompoundTag compoundTag = new CompoundTag("data", tags);

        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            try (NBTOutputStream nbtOutputStream = new NBTOutputStream(fileOutputStream)) {
                nbtOutputStream.writeTag(compoundTag);
            }
        }
    }

    public static void load(File file, Addon addon) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            try (NBTInputStream nbtInputStream = new NBTInputStream(fileInputStream)) {
                Tag tag = nbtInputStream.readTag();
                if (!(tag instanceof CompoundTag)) throw new IllegalArgumentException("Not valid Robot NBT");

                CompoundTag compoundTag = (CompoundTag) tag;

                String serLoc = Objects.requireNonNull(getChildTag(compoundTag.getValue(), "loc", StringTag.class)).getValue();
                serLoc = serLoc.replaceAll(",", "."); //somehow we get commas instead of dots :(

                Location location = LocationUtils.deserializeLocation(
                        serLoc, ";");

                int id = Objects.requireNonNull(getChildTag(compoundTag.getValue(), "id", IntTag.class)).getValue();
                String name = Objects.requireNonNull(getChildTag(compoundTag.getValue(), "name", StringTag.class)).getValue();

                int notch = Objects.requireNonNull(getChildTag(compoundTag.getValue(), "notch", IntTag.class)).getValue();
                int slot = Objects.requireNonNull(getChildTag(compoundTag.getValue(), "slot", IntTag.class)).getValue();

                byte[] inventory = Objects.requireNonNull(getChildTag(compoundTag.getValue(), "inv", ByteArrayTag.class)).getValue();
                ItemStack[] content = ItemUtils.fromByteArray(inventory);

                new Robot(location, addon, id, content, name, notch, slot);
            }
        }
    }

    /**
     * @param items    the values of a Tag
     * @param key      the key to get
     * @param expected the wanted type
     * @param <T>      the wanted type
     * @return if the result can be casted to the wanted type it will return that otherwise null
     */
    private static <T> T getChildTag(Map<String, Tag> items, String key, Class<T> expected) {
        if (!items.containsKey(key)) return null;

        Tag tag = items.get(key);
        if (!expected.isInstance(tag))
            throw new IllegalArgumentException(key + " tag is not of tag type " + expected.getName());

        return expected.cast(tag);
    }
}
