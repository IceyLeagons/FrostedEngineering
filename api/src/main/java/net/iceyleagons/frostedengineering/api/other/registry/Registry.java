package net.iceyleagons.frostedengineering.api.other.registry;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.iceyleagons.frostedengineering.api.exceptions.AlreadyRegisteredException;
import net.iceyleagons.frostedengineering.api.exceptions.NotRegisteredException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TOTHTOMI
 */
@EqualsAndHashCode
public class Registry<T> {

    @Getter
    private final List<T> registered = new ArrayList<>();

    public void register(T toRegister) throws AlreadyRegisteredException {
        if (registered.contains(toRegister)) throw new AlreadyRegisteredException(toRegister);
        registered.add(toRegister);
    }

    public void unregister(T toUnregister) throws NotRegisteredException {
        if (!registered.contains(toUnregister)) throw new NotRegisteredException(toUnregister);
        registered.remove(toUnregister);
    }
}
