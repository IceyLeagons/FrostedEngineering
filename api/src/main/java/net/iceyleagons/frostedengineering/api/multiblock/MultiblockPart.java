package net.iceyleagons.frostedengineering.api.multiblock;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Material;

/**
 * @author TOTHTOMI
 */
@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class MultiblockPart {

    private final Material material;
    @Setter
    private boolean master;

    private final int relativeX;
    private final int relativeZ;
    private final int relativeY;

}
