package database;

import com.j256.ormlite.field.DatabaseField;

public abstract class AnimalAttribute extends NamedObject {
	public static final String ID = "id";
	public static final String ANIMAL_ID = "animal_id";
	
	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = ANIMAL_ID)
	protected Animal animal;
	@DatabaseField(generatedId = true)
	protected Integer id;
	
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
	
	public int hashCode() {
		int hash = 31 + animal.hashCode();
		hash = (name == null ? hash : 31 * hash + name.hashCode());
		return hash;
	}

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
