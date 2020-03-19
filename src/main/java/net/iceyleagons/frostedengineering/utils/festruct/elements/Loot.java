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
 * This class contains all the information about loots(chest) in the structure.
 *
 */
public class Loot {
	
	private int id;
	private String letter;
	private List<String> items;
	
	public Loot() {
		items = new ArrayList<String>();
	}

	/**
	 * @return the id of the loot
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the letter-code of the loot
	 */
	public String getLetter() {
		return letter;
	}

	/**
	 * @return the items of the loost
	 */
	public List<String> getItems() {
		return items;
	}

	/**
	 * @param id is the id to set the id to.
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * @param items is a list of strings to set the items to
	 */
	public void setItems(List<String> items) {
		this.items = items;
	}

	/**
	 * @param letter is the letter to set the letter to
	 */
	public void setLetter(String letter) {
		this.letter = letter;
	}
	
	

}
