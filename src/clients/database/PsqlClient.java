package clients.database;

import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.UUID;

import servlets.DatabaseConfig;
import clients.wit.WitResponse;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;

import database.Animal;
import database.Fact;
import database.Place;

public class PsqlClient {
	public static final String ANIMAL_PLACE_FACT = "animal_place_fact";
	private final DatabaseConfig dbConfig;
	private final Dao<Animal, Integer> animalDao;
	private final Dao<Place, Integer> placeDao;
	public final Dao<Fact, UUID> factDao;
	
	public PsqlClient(DatabaseConfig dbConfig) {
		this.dbConfig = dbConfig;
		this.animalDao = dbConfig.getAnimalDao();
		this.placeDao = dbConfig.getPlaceDao();
		this.factDao = dbConfig.getFactDao();
	}
	
	public UUID deleteFact(UUID factId) throws SQLException {
		factDao.deleteById(factId);
		return factId;
	}

	public UUID storeFact(String factStr) throws SQLException {
		Dao<Fact, UUID> factDao = dbConfig.getFactDao();
		Fact fact = new Fact(factStr);
		factDao.create(fact);
		return fact.getGuid();
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

}
