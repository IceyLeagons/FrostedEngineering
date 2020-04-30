package net.iceyleagons.frostedengineering.particles.effects;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.particles.ParticleEffect;

public class Effect4 extends ParticleEffect {

    private HashMap<Location, BukkitRunnable> running;

    public Effect4(int id, String name) {
        super(id, name);
        running = new HashMap<Location, BukkitRunnable>();
    }

    @Override
    public void start(final Location start) {

        BukkitRunnable r = new BukkitRunnable() {
            int i = 0;
            Location loc = start;

            @Override
            public void run() {
                loc = loc.add(0, i * 0.1f, 0);
                loc.getWorld().spawnParticle(Particle.FLAME, loc, 0, 0, 0, 0);
                if (i == 10) {
                    float num = 400;
                    float fraction = 0.618033f;//0.501616f;

                    for (int i = 0; i < num; i++) {
                        double distance = Math.pow((i / (num - 1)), 3);
                        double angle = 2 * Math.PI * fraction * i;

                        double x = (distance * 20) * Math.cos(angle);
                        double z = (distance * 20) * Math.sin(angle);

                        loc = loc.add(x, 0, z);
                        loc.getWorld().spawnParticle(Particle.SPELL_WITCH, loc, 0, 0, 0, 0);
                    }
                }
                if (i == 12) {
                    this.cancel();
                    i = 0;
                }

                i++;
            }
        };
        r.runTaskTimer(Main.MAIN, 0L, 5L);
    }

    @Override
    public void stop(Location loc) {
        running.get(loc).cancel();
        running.remove(loc);
    }

    @Override
    public void stop() {
        running.forEach((loc, br) -> {
            br.cancel();
        });
        running.clear();
    }

}
