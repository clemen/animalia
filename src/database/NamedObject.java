package database;

import com.j256.ormlite.field.DatabaseField;

public abstract class NamedObject {

	public static final String NAME = "name";

	@DatabaseField(columnName = NAME, canBeNull = false, unique = true)
	protected String name;
	
	public NamedObject() {
		
	}
	
	public NamedObject(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
