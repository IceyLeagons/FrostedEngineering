/*******************************************************************************
 * Copyright (C) IceyLeagons(https://iceyleagons.net/) 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iceyleagons.frostedengineering.energy.network.components;

import org.bukkit.Location;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.energy.events.FrostedPowerGeneratedEvent;
import net.iceyleagons.frostedengineering.energy.network.EnergyNetwork;
import net.iceyleagons.frostedengineering.energy.network.ISecond;
import net.iceyleagons.frostedengineering.energy.network.ITick;
import net.iceyleagons.frostedengineering.energy.network.Unit;

public class Generator extends Unit implements ITick, ISecond {

	private float generates = 1.0f; //how many FP it generates/tick
	private boolean enabled = true;
	
	private FrostedPowerGeneratedEvent e;
	
	public Generator(Location loc, EnergyNetwork network, float generates) {
		super(loc, network);
		this.generates = generates;
		Unit.tickListeners.add(this);
		Unit.secondListeners.add(this);
		Main.debug("Creating generator...");
	}
	
	@Override
	public void onTick() {
		if (enabled && !destroy) {
			e = new FrostedPowerGeneratedEvent(getNetwork(), generates, this);
			e.callEvent();
			if (!e.isCancelled())
				getNetwork().generateFP(generates);
			//Main.debug("Generating " + generates + " for network " + getNetwork().toString());
		}
	}

	public float getGenerates() {
		return generates;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public void onSecond() {
		if (enabled && !destroy) {
			if (!e.isCancelled())
				Main.debug("Generating " + generates + " for network " + getNetwork().toString());
		}
	}
	

}
