package net.iceyleagons.frostedengineering.network.interfaces;

import net.iceyleagons.frostedengineering.network.energy.Upgrade;

import java.util.List;

public interface UpgradeableComponent {

    List<Upgrade> getUpgrades();
    boolean offerUpgrade(Upgrade upgrade);
    void removeUpgrade(Upgrade upgrade);

}
