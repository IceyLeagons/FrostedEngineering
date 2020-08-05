package old.particles;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

public class ParticleManager {

    public static Map<Integer, ParticleEffect> effects = new HashMap<Integer, ParticleEffect>();

    public static void register(ParticleEffect e) {
        effects.put(e.getId(), e);
    }

    public static void unregister(ParticleEffect e) {
        effects.remove(e.getId());
    }

    public static void run(int id, Location loc) {
        effects.get(id).start(loc);
    }

    public static void stop(int id) {
        stop(id, null);
    }

    public static void stop(int id, Location loc) {
        if (loc != null) effects.get(id).stop(loc);
        else effects.get(id).stop();
    }

}
