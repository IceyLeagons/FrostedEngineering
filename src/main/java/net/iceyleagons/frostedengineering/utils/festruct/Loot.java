package net.iceyleagons.frostedengineering.utils.festruct;

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
