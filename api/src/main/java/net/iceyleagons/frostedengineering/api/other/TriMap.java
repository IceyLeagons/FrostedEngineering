package net.iceyleagons.frostedengineering.api.other;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class TriMap<A, B, C> {

    private Map<A, Map.Entry<B, C>> map;

    public TriMap(int initialSize) {
        this.map = new HashMap<>(initialSize);
    }

    public TriMap() {
        this(10);
    }

    public Map.Entry<B, C> getEntry(A key) {
        return map.get(key);
    }

    public C getThirdEntry(A key) {
        return getEntry(key).getValue();
    }

    public B getSecondEntry(A key) {
        return getEntry(key).getKey();
    }

    public Map.Entry<B, C> put(A key, B value1, C value2) {
        Map.Entry<B, C> entry = new AbstractMap.SimpleEntry<>(value1, value2);
        map.put(key, entry);
        return entry;
    }

    public boolean containsKey(A key) {
        return map.containsKey(key);
    }

}
