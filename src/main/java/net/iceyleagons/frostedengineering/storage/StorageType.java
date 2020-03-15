package net.iceyleagons.frostedengineering.storage;

public enum StorageType {
	
	YAML("YAML"), SQLITE("SQLite"), MYSQL("MySQL"), MONGO("Mongo");

	public String name;
	StorageType(String name) {
		this.name = name;
	}
	
}
