package database;

import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "places")
public class Place extends AnimalAttribute{
	
	Place() {
	}
	
	public Place(Animal animal, String name) {
		super(animal, name);
	}
	
	public int hashCode() {
		return super.hashCode();
	}

	public boolean equals(Object other) {
		return super.equals(other);
	}
}
