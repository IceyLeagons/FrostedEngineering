package net.iceyleagons.frostedengineering.storage.mongo.types;

import java.util.List;

import org.bukkit.Location;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

import net.iceyleagons.frostedengineering.storage.mongo.MongoClientAbstract;
import net.iceyleagons.frostedengineering.storage.types.ICraftingTableDatabase;
import net.iceyleagons.frostedengineering.utils.PluginUtils;

public class CraftingTablesMongo extends MongoClientAbstract implements ICraftingTableDatabase{

	public CraftingTablesMongo(String host, String port, String user, String password,String database) {
		super(host, port, user, password,database);
	}
	
	@Override
	public void addCraftingTable(Location loc) {
		DB db = super.client.getDB(super.database);
		DBCollection craftingtables = db.getCollection("craftingtables");
		craftingtables.save(new BasicDBObject("location", PluginUtils.serializeLocation(loc)));
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeCraftingTable(Location loc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Location> getCraftingTables() {
		// TODO Auto-generated method stub
		return null;
	}
}
