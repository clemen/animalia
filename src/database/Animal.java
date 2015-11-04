package database;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "animals")
public class Animal {
	// TODO: set animal_id as the primary key not just an index
	public static final String ANIMAL_ID = "animal_id";
	public static final String NAME = "name";
	public static final String COAT = "coat";
	public static final String SPECIES = "species";
	public static final String LEG_COUNT = "leg_count";
	
	@DatabaseField(columnName = ANIMAL_ID, generatedId = true)
	private Integer animalId;
	@DatabaseField(columnName = NAME, canBeNull = false, unique = true)
	private String name;
	@DatabaseField(columnName = COAT, canBeNull = true)
	private String coat;
	@DatabaseField(columnName = SPECIES, canBeNull = true)
	private String species;
	@DatabaseField(columnName = LEG_COUNT, canBeNull = true)
	private Integer legCount;
	
	Animal() {
	}
	
	public Animal(String name) {
		this.name = name;
	}
	
	public Animal(Integer animalId, String name) {
		this.animalId = animalId;
		this.name = name;
	}

	public Integer getAnimalId() {
		return animalId;
	}

	public String getName() {
		return name;
	}

	public String getCoat() {
		return coat;
	}

	public String getSpecies() {
		return species;
	}

	public Integer getLegCount() {
		return legCount;
	}
	
	@Override
	public int hashCode() {
		int hash = 31 + animalId.hashCode();
		hash = (name == null ? hash : 31 * hash + name.hashCode());
		hash = (coat == null ? hash : 31 * hash + coat.hashCode());
		hash = (species == null ? hash : 31 * hash + species.hashCode());
		hash = (legCount == null ? hash : 31 * hash + legCount.hashCode());
		return hash;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || other.getClass() != getClass()) {
			return false;
		}
		if (name != null && ((Animal)other).getName() != name) {
			return false;
		}
		if ((coat == null && ((Animal)other).getCoat() != null) || coat != null && ((Animal)other).getCoat() != coat) {
			return false;
		}
		if ((species == null && ((Animal)other).getSpecies() != null) || species != null && ((Animal)other).getSpecies() != species) {
			return false;
		}
		if ((legCount == null && ((Animal)other).getLegCount() != null) || legCount != null && ((Animal)other).getLegCount() != legCount) {
			return false;
		}
		return true;
	}

} 

