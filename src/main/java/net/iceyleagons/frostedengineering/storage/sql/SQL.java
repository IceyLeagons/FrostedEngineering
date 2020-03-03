package net.iceyleagons.frostedengineering.storage.sql;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.storage.StorageType;
import net.iceyleagons.frostedengineering.storage.sql.types.MySQL;
import net.iceyleagons.frostedengineering.storage.sql.types.SQLite;

public class SQL {
	
	Main main = null;
	private StorageType type;
	public MySQL mySQL;
	public SQLite sqlite;
	
	public SQL(Main main, StorageType type) {
		this.main = main;
		this.type = type;
		
		init();
	}
	
	private void init() {
		switch(this.type) {
		case MYSQL:
			mySQL = new MySQL("","","","","");
			break;
		case SQLITE:
			sqlite = new SQLite("main", "FrostedEngineering", "test1234"); //Main is used for 
			break;
		default:
			break;
		
		}
	}

}
