package database;

import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "foods")
public class Food extends AnimalAttribute{
	
	Food() {
	}
	
	public Food(Animal animal, String name) {
		super(animal, name);
	}
}
