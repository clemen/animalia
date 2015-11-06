package clients.database;

import java.sql.SQLException;
import java.util.UUID;

import servlets.DatabaseConfig;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;

import database.Animal;
import database.BodyPart;
import database.Fact;
import database.Food;
import database.Place;

public class PsqlClient {
	private final DatabaseConfig dbConfig;
	private final Dao<Animal, Integer> animalDao;
	private final Dao<Place, Integer> placeDao;
	private final Dao<BodyPart, Integer> bodyPartDao;
	private final Dao<Food, Integer> foodDao;
	public final Dao<Fact, UUID> factDao;
	
	public PsqlClient(DatabaseConfig dbConfig) {
		this.dbConfig = dbConfig;
		this.animalDao = dbConfig.getAnimalDao();
		this.placeDao = dbConfig.getPlaceDao();
		this.factDao = dbConfig.getFactDao();
		this.bodyPartDao = dbConfig.getBodyPartDao();
		this.foodDao = dbConfig.getFoodDao();
	}
	
	public UUID deleteFact(UUID factId) throws SQLException {
		factDao.deleteById(factId);
		return factId;
	}

	public class StoredFact {
		public Boolean stored;
		public UUID factId;
		
		public StoredFact(Boolean stored, UUID factId) {
			this.stored = stored;
			this.factId = factId;
		}
	}
	
	public StoredFact storeFact(String factStr) throws SQLException {
		Dao<Fact, UUID> factDao = dbConfig.getFactDao();
		Fact fact = getFactFromFact(factStr);
		if (fact == null) {
			fact = new Fact(factStr);
			factDao.create(fact);
			return new StoredFact(true, fact.getGuid());
		}
		return new StoredFact(false, fact.getGuid());
	}
	
	public Animal getAnimal(String animalStr) throws SQLException {
		PreparedQuery<Animal> query = animalDao.queryBuilder().where().eq(Animal.NAME, animalStr).prepare();
		Animal animal = animalDao.queryForFirst(query);
		return animal;
	}
	
	public Fact getFactFromId(String factId) throws SQLException {
		return factDao.queryForId(UUID.fromString(factId));
	}
	
	
	public Fact getFactFromFact(String fact) throws SQLException {
		PreparedQuery<Fact> query = factDao.queryBuilder().where().eq(Fact.FACT, fact).prepare();
		return factDao.queryForFirst(query);
	}
	
	public void deletePlace(String placeName, Animal animal) throws SQLException {
		Place place = getPlace(placeName, animal);
		placeDao.delete(place );
	}

	public Animal deleteAnimal(String animalStr) throws SQLException {
		Animal animal = getAnimal(animalStr);
		if (animal != null) {
			animalDao.delete(animal);
		}
		return animal;
	}

	public void deleteAnimal(Animal animal) throws SQLException {
		animalDao.delete(animal);		
	}

	public void tryDeleteAnimal(Animal animal) throws SQLException {
		if (animal.isEmpty()) {
			deleteAnimal(animal);
		}
		
	}
	public Animal getOrPrepareAnimal(String animalStr) throws SQLException {
		Animal animal = getAnimal(animalStr);
		if (animal == null) {
			animal = new Animal(animalStr);
		}
		return animal;
	}
	public Animal getOrSetAnimal(String animalStr) throws SQLException {
		Animal animal = getAnimal(animalStr);
		if (animal == null) {
			System.out.println("no " + animal + " found. Creating...");
			animal = new Animal(animalStr);
			animalDao.create(animal);
		}
		return animal;
	}
	
	public Place getPlace(String placeName, Animal animal) throws SQLException {
		PreparedQuery<Place> query = placeDao.queryBuilder().where().eq(Place.NAME, placeName).and().eq(Place.ANIMAL_ID, animal.getAnimalId()).prepare();
		return placeDao.queryForFirst(query);
	}
	
	public Place getOrSetPlace(String placeName, Animal animal) throws SQLException {
		Place place = getPlace(placeName, animal);
		if (place == null) {
			place = new Place(animal, placeName);
			placeDao.create(place);
		}
		return place;
	}

	public Animal getOrSetLegCount(String legCountName, Animal animal, boolean create) throws SQLException {
		Integer legCount = Integer.parseInt(legCountName);
		if (!legCount.equals(animal.getLegCount())) {
			animal.setLegCount(legCount);
			if (create) {
				animalDao.create(animal);
			}
			else {
				animalDao.update(animal);
			}
		}
		return animal;
	}

	public Animal getOrSetCoat(String coat, Animal animal, boolean create) throws SQLException {
		if (!coat.equals(animal.getCoat())) {
			animal.setCoat(coat);
			if (create) {
				animalDao.create(animal);
			}
			else {
				animalDao.update(animal);
			}
		}
		return animal;
	}

	public Animal getOrSetSpecies(String species, Animal animal, boolean create) throws SQLException {
		if (!species.equals(animal.getSpecies())) {
			animal.setSpecies(species);
			if (create) {
				animalDao.create(animal);
			}
			else {
				animalDao.update(animal);
			}
		}
		return animal;
	}

	public BodyPart getOrSetBodyPart(String name, Animal animal) throws SQLException {
		BodyPart bodyPart = getBodyPart(name, animal);
		if (bodyPart == null) {
			bodyPart = new BodyPart(animal, name);
			bodyPartDao.create(bodyPart);
		}
		return bodyPart;		
	}

	private BodyPart getBodyPart(String name, Animal animal) throws SQLException {
		PreparedQuery<BodyPart> query = bodyPartDao.queryBuilder().where().eq(BodyPart.NAME, name).and().eq(BodyPart.ANIMAL_ID, animal.getAnimalId()).prepare();
		return bodyPartDao.queryForFirst(query);
	}

	public Food getOrSetFood(String name, Animal animal) throws SQLException {
		Food food = getFood(name, animal);
		if (food == null) {
			food = new Food(animal, name);
			foodDao.create(food);
		}
		return food;		
	}

	private Food getFood(String name, Animal animal) throws SQLException {
		PreparedQuery<Food> query = foodDao.queryBuilder().where().eq(Food.NAME, name).and().eq(Food.ANIMAL_ID, animal.getAnimalId()).prepare();
		return foodDao.queryForFirst(query);
	}

	public void deleteBodyPart(String name, Animal animal) throws SQLException {
		BodyPart bp = getBodyPart(name, animal);
		bodyPartDao.delete(bp );		
	}

	public void deleteSpecies(Animal animal) throws SQLException {
		animal.setSpecies(null);
		animalDao.update(animal);
	}

	public void deleteCoat(Animal animal) throws SQLException {
		animal.setCoat(null);
		animalDao.update(animal);
	}

}
