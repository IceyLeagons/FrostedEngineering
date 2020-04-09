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
package net.iceyleagons.frostedengineering.network.energy.components.sub.generators.nonthermal;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.network.energy.components.sub.ComponentManager;
import net.iceyleagons.frostedengineering.textures.base.TexturedBlock;

public class TexturedNonthermalGenerator extends TexturedBlock {

	public TexturedNonthermalGenerator() {
		super(Main.MAIN, "nonthermal_generator", "block/nonthermal_generator", "§rNonthermal Generator");
		ComponentManager.registerComponent("fe:nonthermalgenerator", this);
	}

}
