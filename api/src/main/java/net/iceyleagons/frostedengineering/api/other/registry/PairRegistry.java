package net.iceyleagons.frostedengineering.api.other.registry;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.iceyleagons.frostedengineering.api.exceptions.AlreadyRegisteredException;
import net.iceyleagons.frostedengineering.api.exceptions.NotRegisteredException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author TOTHTOMI
 */
@EqualsAndHashCode
public class PairRegistry<K, V> {

    @Getter
    private final Map<K,V> registered = new HashMap<>();

    public void register(K key, V value) throws AlreadyRegisteredException {
        if (registered.containsKey(key)) throw new AlreadyRegisteredException(key);
        registered.put(key, value);
    }

    public Optional<V> getOptionally(K key) {
        try {
            V value = get(key);
            return Optional.of(value);
        } catch (NotRegisteredException ignored) {}
        return Optional.empty();
    }

    public V get(K key) throws NotRegisteredException {
        if (!registered.containsKey(key)) throw new NotRegisteredException(key);
        return registered.get(key);
    }

    public void unregister(K key) throws NotRegisteredException {
        if (!registered.containsKey(key)) throw new NotRegisteredException(key);
        registered.remove(key);
    }

}
