package net.iceyleagons.frostedengineering.storage.sql.types;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.storage.sql.Database;

public class SQLite extends Database {
	String user = "";
	String database = "";
	String password = "";
	Connection c = null;

	public SQLite(String database, String username, String password) {
		this.database = database;
		this.user = username;
		this.password = password;
	}
	
	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Connection open() {
		try {
			new File("plugins/FrostedDatabases").mkdir();
		
			this.c = DriverManager.getConnection("jdbc:sqlite:plugins/FrostedDatabases/"+database+".db");
			return c;
		} catch (SQLException e) {
			Main.debug("Could not connect to SQLite database!");
			Main.debug(e);
		}
		return this.c;
	}

	public boolean checkConnection() {
		if (this.c != null) {
			return true;
		}
		return false;
	}

	public Connection getConn() {
		return this.c;
	}

	public void closeConnection(Connection c) {
		c = null;
	}
}