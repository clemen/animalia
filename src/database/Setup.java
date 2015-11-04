package database;


import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class Setup {
	// TODO: move that to a configuration file 
	private final static String DATABASE_URL = "jdbc:postgresql://localhost/dbtest";
	private final static String user = "animalia"; 
	private final static String password = "password";
	
	private Dao<Animal, Integer> animalDao;
	private Dao<BodyPart, Integer> bodyPartDao;
	private Dao<Food, Integer> foodDao;
	private Dao<Place, Integer> placeDao;
	private Dao<Fact, Integer> factDao;

	public static void main(String[] args) throws Exception {
		new Setup().setup();
	}
	
	public void setup() throws Exception {
		ConnectionSource connectionSource = null;
		try {
			// create our data-source for the database
			connectionSource = new JdbcConnectionSource(DATABASE_URL, user, password);
			// setup our database and DAOs
			setupDatabase(connectionSource);
		} finally {
			// destroy the data source which should close underlying connections
			if (connectionSource != null) {
				connectionSource.close();
			}
		}
	}

	/**
	 * Setup our database and DAOs
	 */
	private void setupDatabase(ConnectionSource connectionSource) throws Exception {

		animalDao = DaoManager.createDao(connectionSource, Animal.class);
		bodyPartDao = DaoManager.createDao(connectionSource, BodyPart.class);
		foodDao = DaoManager.createDao(connectionSource, Food.class);
		placeDao = DaoManager.createDao(connectionSource, Place.class);
		factDao = DaoManager.createDao(connectionSource, Fact.class);

		// TODO: check whether those tables exist
//		TableUtils.createTable(connectionSource, Animal.class);
//		TableUtils.createTable(connectionSource, BodyPart.class);
//		TableUtils.createTable(connectionSource, Food.class);
//		TableUtils.createTable(connectionSource, Place.class);
		TableUtils.createTable(connectionSource, Fact.class);
		System.out.println("success");
	}

	public Dao<Animal, Integer> getAnimalDao() {
		return animalDao;
	}

	public Dao<BodyPart, Integer> getBodyPartDao() {
		return bodyPartDao;
	}

	public Dao<Food, Integer> getFoodDao() {
		return foodDao;
	}

	public Dao<Place, Integer> getPlaceDao() {
		return placeDao;
	}

	
	
}
