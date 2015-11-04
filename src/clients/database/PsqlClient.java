package clients.database;

import java.sql.SQLException;

import servlets.DatabaseConfig;

import com.j256.ormlite.dao.Dao;

import database.Fact;

public class PsqlClient {
	private final DatabaseConfig dbConfig;

	public PsqlClient(DatabaseConfig dbConfig) {
		this.dbConfig = dbConfig;
	}
	
	public String store(String factStr) throws SQLException {
		Dao<Fact, Integer> factDao = dbConfig.getFactDao();
		Fact fact = new Fact(factStr);
		factDao.create(fact);
		return fact.getGuid();
	}
	
	public void process() {
		// TODO: make sure to delete the fact if this fails
	}
	
	public String deleteFact(String factId) {
		return null;
	}
	
	public void revertProcess(String factId) {
		
	}
}
