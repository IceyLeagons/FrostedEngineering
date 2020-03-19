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
package net.iceyleagons.frostedengineering.utils.festruct.elements;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TOTHT
 *
 * This class represents a layer of a structure, containing all the needed information
 *
 */
public class Layer {

	private int id;
	private List<String> layerrow;
	public boolean counted = false;

	public Layer() {
		this.layerrow = new ArrayList<String>();

	}

	/**
	 * This will set the id of this layer to the parameter
	 * 
	 * @param id is the id to set the id to.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the layer's id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the rows of this layer.
	 */
	public List<String> getLayerRows() {
		return layerrow;
	}

}
