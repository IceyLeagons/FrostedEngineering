package net.iceyleagons.frostedengineering.energy.interfaces;

import net.iceyleagons.frostedengineering.energy.network.Unit;

public interface ExplodableComponent {
	
	/**
	 * This function explode the desired {@link Unit}
	 * In case of cable it just melts
	 */
	public void explode();

}
