package database;

import com.j256.ormlite.field.DatabaseField;

public abstract class AnimalAttribute {
	public static final String ID = "id";
	public static final String ANIMAL_ID = "animal_id";
	public static final String NAME = "name";
	
	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = ANIMAL_ID)
	protected Animal animal;
	@DatabaseField(generatedId = true)
	protected Integer id;
	@DatabaseField(columnName = NAME, canBeNull = false)
	protected String name;
	
	public AnimalAttribute() {
		
	}
	
	public AnimalAttribute(Animal animal, String name) {
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
	
	@Override
	public int hashCode() {
		int hash = 31 + animal.hashCode();
		hash = (name == null ? hash : 31 * hash + name.hashCode());
		return hash;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || other.getClass() != getClass()) {
			return false;
		}
		if (name != null && ((AnimalAttribute)other).getName() != name) {
			return false;
		}
		if (animal != null && ((AnimalAttribute)other).getAnimal() != animal) {
			return false;
		}
		return true;
	}
}
