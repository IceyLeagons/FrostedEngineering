package net.iceyleagons.frostedengineering.utils.festruct;

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
