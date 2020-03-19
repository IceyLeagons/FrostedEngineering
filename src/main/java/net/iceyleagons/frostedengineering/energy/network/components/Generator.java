/*******************************************************************************
 * Copyright (C) IceyLeagons(https://iceyleagons.net/) 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iceyleagons.frostedengineering.energy.network.components;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.energy.interfaces.ISecond;
import net.iceyleagons.frostedengineering.energy.interfaces.ITick;
import net.iceyleagons.frostedengineering.energy.network.EnergyNetwork;
import net.iceyleagons.frostedengineering.energy.network.Unit;
import net.iceyleagons.frostedengineering.gui.InventoryFactory;
import net.iceyleagons.frostedengineering.gui.InventoryFactory.ClickRunnable;
import net.iceyleagons.frostedengineering.items.FrostedItems;
import net.iceyleagons.frostedengineering.utils.ItemFactory;
import net.iceyleagons.frostedengineering.utils.ItemUtils;

public class Generator extends Unit implements ITick, ISecond {

	private float generates = 1.0f; // how many FP it generates/tick
	private boolean enabled = true;
	private int remainigBurn = 0;

	/**
	 * @param loc       is the {@link Location} of the Generator
	 * @param network   is the {@link EnergyNetwork} of this {@link Unit}
	 * @param generates is the amount of FrostedPower it generates on every single
	 *                  game tick
	 */
	public Generator(Location loc, EnergyNetwork network, float generates) {
		super(loc, network);
		this.generates = generates;
		Unit.tickListeners.add(this);
		Unit.secondListeners.add(this);
		Main.debug("Creating generator...");
		initInventory();
	}

	/**
	 * @return the amount of FrostedPower it generates on every single game tick
	 */
	public float getGenerates() {
		return generates;
	}

	/**
	 * @return true if the {@link Generator} is working
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled is a boolean to set the Generator's state to, aka. to enable
	 *                or disable it
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * This function is used to create our inventory.
	 */
	public void initInventory() {
		InventoryFactory fac = new InventoryFactory("Generator", 27,FrostedItems.INVENTORY_FILLER.asOne(), false);
		fac.setItem(new ItemFactory(Material.LIGHT_GRAY_STAINED_GLASS_PANE).hideAttributes()
				.setDisplayName("§fGenerating: §b0FP§7/§ftick").build(), 12);
		fac.setItem(new ItemStack(Material.AIR), 14);
		super.setInventoryFactory(fac);
		updateInventory();
	}

	/**
	 * This function is used to update our inventory. However it only runs 1/1s to
	 * not freak bukkit out.
	 */
	public void updateInventory() {
		if (!destroy) {
			if (enabled)
				super.getInventoryFactory().setItem(new ItemFactory(Material.RED_STAINED_GLASS_PANE)
						.hideAttributes().setDisplayName("§fGenerating: §b" + generates + " FP§7/§ftick").build(), 12);
			else
				super.getInventoryFactory().setItem(new ItemFactory(Material.GRAY_STAINED_GLASS_PANE).hideAttributes()
						.setDisplayName("§fGenerating: §b0FP§7/§ftick").build(), 12, new ClickRunnable() {

							@Override
							public void run(InventoryClickEvent e) {
								e.setCancelled(true);
							}
				});

			int slot = 14;
			ItemStack item = super.getInventoryFactory().getSourceInventory().getItem(slot);
			if (item != null) {
				if (item.getType().isFuel()) {
					remainigBurn = ItemUtils.getBurnTime(item);
					System.out.println(remainigBurn);
					super.getInventoryFactory().setItem(new ItemStack(Material.AIR), slot);
				}
				super.getInventoryFactory().updateInventory();
			}
		}
	}

	public void open(Player p) {

		if (!destroy) {
			if (super.getInventoryFactory() == null)
				initInventory();
			super.getInventoryFactory().openInventory(p);
		}
	}

	public void update() {
		enabled = (remainigBurn != 0) ? true : false;
	}

	@Override
	public void onTick() {
		updateInventory();
		update();
		if (enabled && !destroy) {
			getNetwork().generateFP(generates);
			remainigBurn--;
		}
	}

	@Override
	public void onSecond() {
		if (enabled && !destroy) {
			super.getLocation().getWorld().spawnParticle(Particle.SMOKE_LARGE, super.getLocation(), 1, 0.1, 0.5, 0);
		}
	}

}
