package net.iceyleagons.frostedengineering.storage.sql.types;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.storage.sql.Database;

public class MySQL extends Database {

	String user = "";
	String database = "";
	String password = "";
	String port = "";
	String hostname = "";
	Connection c = null;

	public MySQL(String hostname, String portnmbr, String database, String username, String password) {
		this.hostname = hostname;
		this.port = portnmbr;
		this.database = database;
		this.user = username;
		this.password = password;
	}

	public Connection open() {
		Main.debug("Opening MySQL connection...");
		try {
			Class.forName("com.mysql.jdbc.Driver");
			this.c = DriverManager.getConnection("jdbc:mysql://" + this.hostname + ":" + this.port + "/" + this.database, this.user, this.password);
			return c;
		} catch (SQLException e) {
			Main.debug("Could not connect to MySQL server!");
			Main.debug(e);
		} catch (ClassNotFoundException e) {
			Main.debug("JDBC drive not found!");
			Main.debug(e.getMessage());
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
		Main.debug("Closing connection to MySQL...");
		try {
			c.close();
		} catch (SQLException e) {
			Main.debug(e);
		}
		c = null;
	}

}
