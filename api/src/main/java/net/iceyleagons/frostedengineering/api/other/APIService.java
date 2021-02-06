package net.iceyleagons.frostedengineering.api.other;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.iceyleagons.frostedengineering.api.IFrostedEngineering;
import net.iceyleagons.frostedengineering.api.addon.Addon;

/**
 * @author TOTHTOMI
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class APIService<T> {

    private final Addon registrationHolder;
    private final IFrostedEngineering frostedEngineering;
    private final Class<T> serviceClass;
    private final T serviceProvider;

}
