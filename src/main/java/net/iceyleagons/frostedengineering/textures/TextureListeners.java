package net.iceyleagons.frostedengineering.textures;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.textures.base.TexturedBlock;
import net.iceyleagons.frostedengineering.textures.base.TexturedItem;

public class TextureListeners implements Listener {

	@EventHandler
	public void onBreakBlock(BlockBreakEvent e) {
		Block b = e.getBlock();
		if (Textures.isTexturedBlock(b)) {
			TexturedBlock cb = Textures.getBlock(b);
			cb.onBroken(e);
			if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
				for (ItemStack i : cb.getLootTable()) {
					if (i != null && i.getType() != Material.AIR)
						e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), i);
				}
			}

			try {
				BlockStorage ws = Textures.getBlockStorage(e.getBlock().getWorld());
				ws.handleEvent(e);
			} catch (NullPointerException ignored) {
				// We ignore this, 'cause even if this happens, it just means that the block storage has not been initialized yet. No worries there!
			}

			e.setExpToDrop(0);
		}
	}

	@EventHandler
	public void onMiddleClick(InventoryCreativeEvent e) {
		if (e.getClick() == ClickType.MIDDLE) {
			if (e.getCurrentItem() != null && e.getWhoClicked() != null)
				if (Textures.isTexturedBlock(e.getCurrentItem())) {
					e.getWhoClicked().getInventory().addItem(Textures.getTexturedBlock(e.getCurrentItem()).getItem());
				}
		}
	}

	@EventHandler
	public void onCraft(PrepareItemCraftEvent e) {
		List<ItemStack> stackList = new ArrayList<>();
		for (int i = 1; i < e.getInventory().getSize(); i++)
			stackList.add(e.getInventory().getContents()[i]);
		int hoe = 0;
		for (ItemStack stack : stackList) {
			if (Textures.isTexturedItem(stack))
				hoe++;
		}
		if (e.isRepair() && hoe >= 2)
			e.getInventory().setResult(null);
	}

	@EventHandler
	public void onInteractEntity(PlayerInteractEntityEvent e) {
		if (e.getHand().equals(EquipmentSlot.HAND)) {
			TexturedItem ci = TextureUtils.getMainOrOffHandItem(e);
			if (ci != null)
				ci.onInteractEntity(e);
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		e.getPlayer().setResourcePack(Textures.getData("resourcepack-link"), Textures.getData("resourcepack-sha1"));
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (Textures.isTexturedBlock(e.getClickedBlock())) {
				e.setCancelled(true);
				Textures.getBlock(e.getClickedBlock()).onInteract(e);
			}
		}
		if (((e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)
				&& e.getHand().equals(EquipmentSlot.HAND))
				|| (e.getAction() == Action.RIGHT_CLICK_AIR && e.getHand().equals(EquipmentSlot.OFF_HAND))) {

			TexturedItem ci = TextureUtils.getMainOrOffHandItem(e);
			if (ci != null) {
				ci.onInteract(e);
				e.setCancelled(true);
			}

			if (e.getItem() != null && Textures.isTexturedBlock(e.getItem())
					&& (e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
				e.setCancelled(true);
				Main.debug("" + ((Damageable) e.getItem().getItemMeta()).getDamage());
				TexturedBlock cb = Textures.getTexturedBlock(e.getItem());
				int x = e.getClickedBlock().getX() + e.getBlockFace().getModX();
				int y = e.getClickedBlock().getY() + e.getBlockFace().getModY();
				int z = e.getClickedBlock().getZ() + e.getBlockFace().getModZ();
				Block b = new Location(e.getClickedBlock().getWorld(), x, y, z).getBlock();
				if (b.isEmpty())
					cb.setBlock(b, e.getPlayer());

				if (e.getPlayer().getGameMode() != GameMode.CREATIVE)
					e.getItem().setAmount(e.getItem().getAmount() - 1);

			}
		}

	}

}
