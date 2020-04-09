package net.iceyleagons.frostedengineering.particles.effects;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

import net.iceyleagons.frostedengineering.particles.ParticleEffect;

public class Effect2 extends ParticleEffect{

	private HashMap<Location,BukkitRunnable> running;
	
	public Effect2(int id, String name) {
		super(id, name);
		running = new HashMap<Location,BukkitRunnable>();
	}

	@Override
	public void start(final Location start) {
		
		float num = 400;
		float fraction = 0.501616f;//0.501616f;
		Location loc = start;
		
		for (int i = 0; i < num; i++) {
			double distance = Math.pow((i / (num -1)), 3);
			double angle = 2 * Math.PI * fraction * i;
			
			double x = (distance*20) * Math.cos(angle);
			double z = (distance*20) * Math.sin(angle);
			double y = Math.sin(angle) * Math.PI;
			
			loc = loc.add(x, y, z);
			loc.getWorld().spawnParticle(Particle.FLAME, loc, 0, 0, 0, 0);
		}
		
		/*
		
		BukkitRunnable r = new BukkitRunnable() {
			int i = 0;
			Location loc = start;
			
			@Override
			public void run() {
				double distance = i / (num - 15);
				double angle = 2 * Math.PI * fraction * i;
				
				double x = distance * Math.cos(angle);
				double z = distance * Math.sin(angle);
				
				loc = loc.add(x, 0, z);
				loc.getWorld().spawnParticle(Particle.FLAME, loc, 0, 0, 0, 0);
				
				if (i > num) this.cancel();
				i++;
			}
		};
		running.put(start, r);
		r.runTaskTimer(Main.MAIN, 0L, 1L);
		*/
	}

	@Override
	public void stop(Location loc) {
		running.get(loc).cancel();
		running.remove(loc);
	}

	@Override
	public void stop() {
		running.forEach((loc,br) -> {
			br.cancel();
		});
		running.clear();
	}

}
