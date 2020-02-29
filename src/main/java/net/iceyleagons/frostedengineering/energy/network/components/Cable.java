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

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.energy.network.EnergyNetwork;
import net.iceyleagons.frostedengineering.energy.network.ISecond;
import net.iceyleagons.frostedengineering.energy.network.ITick;
import net.iceyleagons.frostedengineering.energy.network.Unit;

public class Cable extends Unit implements ITick, ISecond {

	private float maxFP = 1.0f; // how many FP can it handle/tick
	private boolean insulated = false;
	
	public Cable(Location loc, EnergyNetwork network, float maxFP) {
		super(loc, network);
		this.maxFP = maxFP;
		Unit.tickListeners.add(this);
		Unit.secondListeners.add(this);
		Main.debug("Creating cable unit...");
	}
	
	@Override
	public void onTick() {
	}
	
	@Override
	public void onSecond() {
		if (insulated == false) {
			electrocute();
			//Main.debug("Electrocuting...");
		}
	}
	
	private void electrocute() {
		Collection<Entity> near = getLocation().getWorld().getNearbyEntities(getLocation(),1.5,1.5,1.5);
		
		near.forEach(e -> {
			if (e instanceof LivingEntity && getNetwork().getFP() > 1f) {
				
				LivingEntity le = (LivingEntity) e;
				if (le instanceof Player) {
					Player p = (Player) le;
					if (p.getGameMode().equals(GameMode.CREATIVE) || p.getGameMode().equals(GameMode.SPECTATOR)) return;
				}
				Main.debug("Electrocuting a " + le.getType().toString());
				float damageAmount = getNetwork().getFP()*0.7f;
				
				EntityDamageEvent damage = new EntityDamageEvent(le, DamageCause.LIGHTNING, damageAmount);
				le.damage(damageAmount);
				Bukkit.getPluginManager().callEvent(damage);
				e.setLastDamageCause(damage);
				
				getLocation().getWorld().playSound(getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 2L, 1L);
				getLocation().getWorld().spawnParticle(Particle.SMOKE_NORMAL, getLocation(), 10);
			}
		});
	}

	public float getMaxFP() {
		return maxFP;
	}

	

}
