package net.iceyleagons.frostedengineering.entity;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_14_R1.EntityInsentient;
import net.minecraft.server.v1_14_R1.NavigationAbstract;
import net.minecraft.server.v1_14_R1.PathfinderGoal;

public class AttackNearestPlayer extends PathfinderGoal{

	private NavigationAbstract nav;
	private EntityInsentient entity;
	private double speed;
	
	public AttackNearestPlayer(EntityInsentient entity, double speed) {
		this.entity = entity;
		this.nav = this.entity.getNavigation();
		this.speed = speed;
	}
	
	@Override
	public boolean a() {
		return true;
	}
	
	@Override
	public void c() {
		Location l = new Location(this.entity.getWorld().getWorld(),this.entity.locX, this.entity.locY, this.entity.locZ);
		Collection<Entity> near = l.getNearbyEntities(30.0D, 30.0D, 30.0D);
		for (Entity e : near) {
			if (e instanceof Player) {
				Location loc = ((Player)e).getLocation();
				this.nav.a(this.nav.a(loc.getX(),loc.getY(),loc.getZ()),speed);
			}
		}
		
	}

}
