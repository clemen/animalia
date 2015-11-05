package database;

import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "body_parts")
public class BodyPart extends AnimalAttribute{
	
	public BodyPart() {
	}
	
	public BodyPart(Animal animal, String name) {
		super(animal, name);
	}
	
	
}
