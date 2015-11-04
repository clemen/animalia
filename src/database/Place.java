package database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "places")
public class Place {
	public static final String ID = "id";
	public static final String ANIMAL_ID = "animal_id";
	public static final String NAME = "name";
	
	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = ANIMAL_ID)
	private Animal animal;
	@DatabaseField(columnName = NAME, canBeNull = false)
	private String name;
	
	Place() {
	}
	
	public Place(Animal animal, String name) {
		this.animal = animal;
		this.name = name;
	}
	
	public Integer getId() {
		return id;
	}
	public Animal getAnimal() {
		return animal;
	}
	public String getName() {
		return name;
	}
}
