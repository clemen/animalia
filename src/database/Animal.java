package database;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "animals")
public class Animal extends NamedObject{
	// TODO: set animal_id as the primary key not just an index
	public static final String ANIMAL_ID = "animal_id";
	public static final String COAT = "coat";
	public static final String SPECIES = "species";
	public static final String LEG_COUNT = "leg_count";
	
	@DatabaseField(columnName = ANIMAL_ID, generatedId = true)
	private Integer animalId;
	@DatabaseField(columnName = COAT, canBeNull = true)
	private String coat;
	@DatabaseField(columnName = SPECIES, canBeNull = true)
	private String species;
	@DatabaseField(columnName = LEG_COUNT, canBeNull = true)
	private Integer legCount;
	@ForeignCollectionField(eager = false)
    private ForeignCollection<Place> places;
	@ForeignCollectionField(eager = false)
    private ForeignCollection<Food> foods;
	@ForeignCollectionField(eager = false)
    private ForeignCollection<BodyPart> bodyParts;
	
	public Animal() {
	}
	
	public Animal(String name) {
		super(name);
	}
	
	public Animal(Integer animalId, String name) {
		super(name);
		this.animalId = animalId;
	}

	public Integer getAnimalId() {
		return animalId;
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
	
	public ForeignCollection<Place> getPlaces() {
		return places;
	}

	public ForeignCollection<Food> getFoods() {
		return foods;
	}

	public ForeignCollection<BodyPart> getBodyParts() {
		return bodyParts;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCoat(String coat) {
		this.coat = coat;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public void setLegCount(Integer legCount) {
		this.legCount = legCount;
	}

	@Override
	public int hashCode() {
		int hash = 31 + animalId.hashCode();
		hash = (name == null ? hash : 31 * hash + name.hashCode());
		hash = (coat == null ? hash : 31 * hash + coat.hashCode());
		hash = (species == null ? hash : 31 * hash + species.hashCode());
		hash = (legCount == null ? hash : 31 * hash + legCount.hashCode());
		hash = (places == null ? hash : 31 * hash + places.hashCode()); 
		hash = (foods == null ? hash : 31 * hash + foods.hashCode()); 
		hash = (bodyParts == null ? hash : 31 * hash + bodyParts.hashCode()); 
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
		if ((places == null && ((Animal)other).getPlaces() != null) || places != null && ((Animal)other).getPlaces() != places) {
			return false;
		}
		if ((foods == null && ((Animal)other).getFoods() != null) || foods != null && ((Animal)other).getFoods() != foods) {
			return false;
		}
		if ((bodyParts == null && ((Animal)other).getBodyParts() != null) || bodyParts != null && ((Animal)other).getBodyParts() != bodyParts) {
			return false;
		}
		return true;
	}
	
	public boolean isEmpty() {
		if (coat == null && legCount == null && species == null && (places == null || places.isEmpty()) 
				&& (foods == null || foods.isEmpty()) && (bodyParts == null || bodyParts.isEmpty())) {
			return true;
		}
		return false;
	}

} 

