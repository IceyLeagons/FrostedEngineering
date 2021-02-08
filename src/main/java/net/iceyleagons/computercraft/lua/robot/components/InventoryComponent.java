package net.iceyleagons.computercraft.lua.robot.components;

import lombok.SneakyThrows;
import net.iceyleagons.computercraft.lua.library.LibraryBuilder;
import net.iceyleagons.computercraft.lua.robot.Component;
import net.iceyleagons.computercraft.lua.robot.Robot;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * @author TOTHTOMI
 */
public class InventoryComponent extends Component {

    public InventoryComponent(Robot robot) {
        super(robot);
    }

    @Override
    protected void addLibraryFields(LibraryBuilder libraryBuilder) {
        libraryBuilder.addFunction("transferItems", varargs -> generateReturn(transferItems())
        ).addFunction("takeItems", varargs -> generateReturn(takeItems())
        ).addFunction("suckItems", varargs -> generateReturn(suckItems()));
    }

    @SneakyThrows
    private boolean transferItemsToContainer(Container container) {
        ItemStack[] items = getInventory().getContents();
        for (ItemStack item : items) {
            if (item != null) {
                final int amount = item.getAmount();
                for (int i = 1; i <= amount; i++) {
                    ItemStack itemStack = item.clone();
                    itemStack.setAmount(1);
                    container.getInventory().addItem(itemStack);
                    Thread.sleep(10);
                }

            }
        }
        getInventory().clear();
        return true;
    }

    @SneakyThrows
    private boolean transferItemsFromContainer(Container container) {
        ItemStack[] items = container.getInventory().getContents();
        for (ItemStack item : items) {
            if (item != null) {
                final int amount = item.getAmount();
                for (int i = 1; i <= amount; i++) {
                    ItemStack itemStack = item.clone();
                    itemStack.setAmount(1);
                    getInventory().addItem(itemStack);
                    Thread.sleep(10);
                }

            }
        }
        container.getInventory().clear();
        getRobot().switchItemInHand();
        return true;
    }

    private void getContainer(Block block, CompletableFuture<Container> result) {
        Bukkit.getScheduler().runTask(getRobot().getJavaPlugin(), bukkitTask -> {
            if (block.getState() instanceof Container) {
                result.complete((Container) block.getState());
            } else {
                result.complete(null);
            }
        });
    }

    private boolean suckItems() {
        doSync(() -> {
            Collection<Entity> entityCollection = Objects.requireNonNull(getEntity().getLocation().getWorld()).getNearbyEntities(getEntity().getLocation(), 1.4, 1.4, 1.4, entity -> entity instanceof Item);
            entityCollection.forEach(e -> {
                Item item = (Item) e;
                getInventory().addItem(item.getItemStack());
                item.remove();
            });
        });
        getRobot().switchItemInHand();
        return true;
    }

    private boolean takeItems() {
        Block block = getBlock(getBlockFace());
        CompletableFuture<Container> result = new CompletableFuture<>();
        getContainer(block, result);

        Container container = result.join();
        if (container == null) return false;

        return transferItemsFromContainer(container);
    }

    private boolean transferItems() {
        Block block = getBlock(getBlockFace());
        CompletableFuture<Container> result = new CompletableFuture<>();
        getContainer(block, result);

        Container container = result.join();
        if (container == null) return false;

        return transferItemsToContainer(container);
    }

}
