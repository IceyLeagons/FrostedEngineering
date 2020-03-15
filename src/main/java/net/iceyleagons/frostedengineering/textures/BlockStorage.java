package net.iceyleagons.frostedengineering.textures;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockStorage {
	private final World world;
	protected final HashMap<Location, Map<String, Object>> metadatas;

	public BlockStorage(World world) {
		this.world = world;
		this.metadatas = new HashMap<>();
	}

	public BlockStorage(World world, Map<Map<String, Object>, Map<String, Object>> values) {
		this.world = world;
		HashMap<Location, Map<String, Object>> map = new HashMap<>();

		for (Entry<Map<String, Object>, Map<String, Object>> entr : values.entrySet()) {
			map.put(Location.deserialize(entr.getKey()), entr.getValue());
		}

		this.metadatas = map;
	}

	public void handleEvent(BlockBreakEvent event) {
		metadatas.remove(event.getBlock().getLocation());
	}

	public Object getData(Location location, String key) {
		createIfNotExisting(location);
		return metadatas.get(location).get(key);
	}

	public Map<String, Object> getMetadatas(Location location) {
		return metadatas.get(location);
	}

	public void removeData(Location location, String key) {
		createIfNotExisting(location);
		metadatas.get(location).remove(key);
	}

	public void setData(Location location, String key, Object data) {
		createIfNotExisting(location);
		metadatas.get(location).put(key, data);
	}

	public boolean hasData(Location location, String key) {
		createIfNotExisting(location);
		return metadatas.get(location).containsKey(key);
	}

	private void createIfNotExisting(Location location) {
		if (!metadatas.containsKey(location))
			metadatas.put(location, new HashMap<>());
	}

	private Map<Map<String, Object>, Map<String, Object>> serialize() {
		Map<Map<String, Object>, Map<String, Object>> result = new HashMap<>();

		for (Entry<Location, Map<String, Object>> entr : metadatas.entrySet()) {
			result.put(entr.getKey().serialize(), entr.getValue());
		}
		return result;
	}

	public List<Location> getBlockLocationsForKey(String key) {
		List<Location> list = new ArrayList<>();
		for (Entry<Location, Map<String, Object>> entr : metadatas.entrySet()) {
			if (entr.getValue().containsKey(key))
				list.add(entr.getKey());
		}
		return list;
	}

	public void save() {
		File file = new File(world.getWorldFolder(), "block.map");

		try {
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(serialize());
			oos.close();
			fos.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
}