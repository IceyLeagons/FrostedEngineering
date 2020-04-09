package net.iceyleagons.frostedengineering.network.energy.components.sub;

import java.util.HashMap;
import java.util.Map;

/**
 * @author TOTHT
 *
 * This class is used for saving/loading.
 *
 */
public class ComponentManager {
	
	public static Map<String,Object> components;
	
	static {
		components = new HashMap<String,Object>();
	}
	
	/**
	 * @param name is the name for the component
	 * @param texturedObject is the object of the textured component
	 */
	public static void registerComponent(String name, Object texturedObject) {
		if (!components.containsKey(name)) components.put(name, texturedObject);
	}
	
	/**
	 * @param name is the name of the component to unregister
	 */
	public static void unregisterComponent(String name) {
		if (components.containsKey(name)) components.remove(name);
	}
	
	/**
	 * @param name is the name of the component to search for
	 * @return the textured component object of it.
	 */
	public static Object getComponent(String name) {
		return components.get(name) != null ? components.get(name) : null;
	}

}
