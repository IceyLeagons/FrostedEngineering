package old.particles.effects;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

import net.iceyleagons.frostedengineering.Main;
import old.particles.ParticleEffect;

public class Effect3 extends ParticleEffect {

    private HashMap<Location, BukkitRunnable> running;

    public Effect3(int id, String name) {
        super(id, name);
        running = new HashMap<Location, BukkitRunnable>();
    }

    @Override
    public void start(final Location start) {

        BukkitRunnable r = new BukkitRunnable() {
            int i = 0;
            Location loc = start;
            Location line = start;

            @Override
            public void run() {
                line = line.add(0, i * 0.1f, 0);
                line.getWorld().spawnParticle(Particle.FLAME, loc, 0, 0, 0, 0);
                if (i == 10) {
                    Location effect = loc;
                    float num = 400;
                    float fraction = 0.618033f;//0.501616f;

                    for (int i = 0; i < num; i++) {
                        double distance = Math.pow((i / (num - 1)), 3);
                        double angle = 2 * Math.PI * fraction * i;

                        double x = (distance * 20) * Math.cos(angle);
                        double z = (distance * 20) * Math.sin(angle);

                        effect = effect.add(x, 0, z);
                        effect.getWorld().spawnParticle(Particle.SPELL_WITCH, loc, 0, 0, 0, 0);
                    }
                }
                if (i == 11) {
                    float num = 400;
                    float fraction = 0.501616f;//0.501616f;

                    for (int i = 0; i < num; i++) {
                        double distance = Math.pow((i / (num - 1)), 3);
                        double angle = 2 * Math.PI * fraction * i;

                        double x = (distance * 20) * Math.cos(angle);
                        double z = (distance * 20) * Math.sin(angle);
                        double y = Math.sin(angle) * Math.PI;

                        loc = loc.add(x, y, z);
                        loc.getWorld().spawnParticle(Particle.CLOUD, loc, 0, 0, 0, 0);
                        if (num == 398) loc.getWorld().spawnParticle(Particle.SPELL_WITCH, loc, 10, 0, 0, 0);
                        if (num == 399) loc.getWorld().spawnParticle(Particle.SPELL_WITCH, loc, 10, 0, 0, 0);
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
