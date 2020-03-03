package net.iceyleagons.frostedengineering.storage.sql.types.altypes.craftingtables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import net.iceyleagons.frostedengineering.storage.sql.types.SQLite;
import net.iceyleagons.frostedengineering.storage.types.ICraftingTableDatabase;
import net.iceyleagons.frostedengineering.utils.PluginUtils;

public class CraftingTablesSQLite extends SQLite implements ICraftingTableDatabase {

	public CraftingTablesSQLite(String database, String username, String password) {
		super(database, username, password);
	}

	@Override
	public void init() {
		String sql = "CREATE TABLE IF NOT EXISTS craftingtables (id integer PRIMARY KEY, location text NOT NULL);";
		try (Connection conn = super.open(); Statement stmt = conn.createStatement()) {
			// create a new table
			stmt.execute(sql);
			super.closeConnection(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addCraftingTable(Location loc) {
		String sql = "INSERT INTO craftingtables (location) VALUES (?)";

		String ser = PluginUtils.serializeLocation(loc);
		try (Connection conn = super.open(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			// create a new table
			stmt.setString(1, ser);
			stmt.executeUpdate();
			super.closeConnection(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void removeCraftingTable(Location loc) {
		String sql = "DELETE FROM craftingtables WHERE location = ?";

		String ser = PluginUtils.serializeLocation(loc);
		try (Connection conn = super.open(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			// create a new table
			stmt.setString(1, ser);
			stmt.executeUpdate();
			super.closeConnection(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Location> getCraftingTables() {
		String sql = "SELECT * FROM craftingtables";
		List<Location> locs = new ArrayList<Location>();
		try (Connection conn = super.open(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
			// create a new table
			// stmt.setString(1, ser);
			while (rs.next()) {
				locs.add(PluginUtils.deserializeLocation(rs.getString("location")));
			}
			super.closeConnection(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return locs;
	}

}
