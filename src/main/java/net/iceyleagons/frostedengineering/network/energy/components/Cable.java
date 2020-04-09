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
package net.iceyleagons.frostedengineering.network.energy.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.interfaces.ISecond;
import net.iceyleagons.frostedengineering.interfaces.ITick;
import net.iceyleagons.frostedengineering.network.energy.EnergyNetwork;
import net.iceyleagons.frostedengineering.network.energy.EnergyNetworkType;
import net.iceyleagons.frostedengineering.network.energy.EnergyUnit;
import net.iceyleagons.frostedengineering.network.energy.interfaces.ExplodableComponent;

public class Cable extends EnergyUnit implements ITick, ISecond, ExplodableComponent {

	private EnergyNetworkType capable = EnergyNetworkType.LOW_VOLTAGE; // (how many FP can it handle/tick)
	private boolean insulated = false;
	
	/**
	 * @param loc is the {@link Location} of the Cable
	 * @param network is the {@link EnergyNetwork} of this {@link EnergyUnit}
	 * @param insulated is a boolean if true the cable won't electrocute
	 */
	public Cable(Location loc, EnergyNetwork network, EnergyNetworkType capable, boolean insulated) {
		super(loc, network);
		this.capable = capable;
		Main.registerISecond(this);
		Main.registerITick(this);
		Main.debug("Creating cable unit...");
		this.insulated = insulated;
	}
	
	/**
	 * @param loc is the {@link Location} of the Cable
	 * @param network is the {@link EnergyNetwork} of this {@link EnergyUnit}
	 * @param uuid is the uuid of the Unit (used when loading)
	 * @param insulated is a boolean if true the cable won't electrocute
	 */
	public Cable(Location loc, EnergyNetwork network, UUID uuid,EnergyNetworkType capable, boolean insulated) {
		super(loc, network,uuid,new ArrayList<ItemStack>());
		this.capable = capable;
		Main.registerISecond(this);
		Main.registerITick(this);
		Main.debug("Creating cable unit...");
		this.insulated = insulated;
	}
	
	@Override
	public void onTick() {
	}
	
	@Override
	public void onSecond() {
		if (EnergyNetworkType.doExplode(getCapable(), ((EnergyNetwork)getNetwork()).getType())) this.explode();
		if (insulated == false) {
			electrocute();
		}
	}
	
	/**
	 * Used to shock entities around the cable
	 */
	private void electrocute() {
		Collection<Entity> near = getLocation().getWorld().getNearbyEntities(getLocation(),1.5,1.5,1.5);
		
		near.forEach(e -> {
			if (e instanceof LivingEntity && ((EnergyNetwork)getNetwork()).getFP() > 1f) {
				
				LivingEntity le = (LivingEntity) e;
				if (le instanceof Player) {
					Player p = (Player) le;
					if (p.getGameMode().equals(GameMode.CREATIVE) || p.getGameMode().equals(GameMode.SPECTATOR)) return;
				}
				Main.debug("Electrocuting a " + le.getType().toString());
				float damageAmount = ((EnergyNetwork)getNetwork()).getFP()*0.7f;
				
				EntityDamageEvent damage = new EntityDamageEvent(le, DamageCause.LIGHTNING, damageAmount);
				le.damage(damageAmount);
				Bukkit.getPluginManager().callEvent(damage);
				e.setLastDamageCause(damage);
				
				getLocation().getWorld().playSound(getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 2L, 1L);
				getLocation().getWorld().spawnParticle(Particle.SMOKE_NORMAL, getLocation(), 10);
			}
		});
	}

	/**
	 * @return the {@link EnergyNetworkType} it can handle
	 */
	public EnergyNetworkType getCapable() {
		return capable;
	}

	@Override
	public void explode() {
		this.destroy();
		this.getLocation().getWorld().playSound(this.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1L, 1L);
		this.getLocation().getWorld().playEffect(this.getLocation(), Effect.SMOKE, 5L);
	}

	@Override
	public List<ItemStack> getItemsInside() {
		return null;
	}

	

}
