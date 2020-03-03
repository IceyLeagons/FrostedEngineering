package net.iceyleagons.frostedengineering.storage;

import javax.annotation.Nullable;

import org.apache.commons.lang.NotImplementedException;

import net.iceyleagons.frostedengineering.storage.mongo.types.CraftingTablesMongo;
import net.iceyleagons.frostedengineering.storage.sql.types.altypes.craftingtables.CraftingTablesMySQL;
import net.iceyleagons.frostedengineering.storage.sql.types.altypes.craftingtables.CraftingTablesSQLite;
import net.iceyleagons.frostedengineering.storage.types.ICraftingTableDatabase;

public class StorageManager {

	public ICraftingTableDatabase CRAFTING_TABLE;

	public StorageManager(String username, String password, String database, @Nullable String host,
			@Nullable String port, StorageType type) {
		if (username == null)
			throw new IllegalArgumentException("Username cannot be null.");
		if (password == null)
			throw new IllegalArgumentException("Password cannot be null.");
		if (database == null)
			throw new IllegalArgumentException("Database cannot be null.");

		switch (type) {
		case MONGO:
			setupMongo(username, password, database, host, port);
			break;
		case MYSQL:
			setupMySQL(username, password, database, host, port);
			break;
		case SQLITE:
			setupSQLite(username, password, database);
			break;
		case YAML:
			throw new NotImplementedException("Cannot use YAML in Storage manager! This is only for databases");
		default:

			throw new IllegalArgumentException("StorageType is not recognized in class StorageManager line 24");

		}
	}

	private void setupMongo(String username, String password, String database, String host, String port) {
		assert host == null : "Host cannot be null.";
		assert host == null : "Port cannot be null.";

		CRAFTING_TABLE = new CraftingTablesMongo(host, port, username, password, database);
		CRAFTING_TABLE.init();
	}

	private void setupMySQL(String username, String password, String database, String host, String port) {
		assert host == null : "Host cannot be null.";
		assert host == null : "Port cannot be null.";

		CRAFTING_TABLE = new CraftingTablesMySQL(host, port, database, username, password);
		CRAFTING_TABLE.init();
	}

	private void setupSQLite(String username, String password, String database) {
		CRAFTING_TABLE = new CraftingTablesSQLite(database, username, password);
		CRAFTING_TABLE.init();
	}

}
