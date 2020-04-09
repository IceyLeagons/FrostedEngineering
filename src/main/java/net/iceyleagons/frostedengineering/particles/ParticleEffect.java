package net.iceyleagons.frostedengineering.particles;

import org.bukkit.Location;

public abstract class ParticleEffect {
	
	private int id;
	private String name;
	
	public ParticleEffect(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getName() {
		return name;
	}
	
	public abstract void start(Location start);
	public abstract void stop(Location loc);
	public abstract void stop();
	

}
