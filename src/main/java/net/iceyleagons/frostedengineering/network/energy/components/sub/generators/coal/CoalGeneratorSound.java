package net.iceyleagons.frostedengineering.network.energy.components.sub.generators.coal;

import org.bukkit.plugin.java.JavaPlugin;

import net.iceyleagons.frostedengineering.textures.base.TexturedSound;

public class CoalGeneratorSound extends TexturedSound {

	public CoalGeneratorSound(JavaPlugin plugin) {
		super(plugin, "generator", "frosted/generator", (short) 100, 0.75f, 1.f);
	}

}
