package clients.database;

import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.UUID;

import servlets.DatabaseConfig;
import clients.wit.Entity;
import clients.wit.Outcome;
import clients.wit.WitResponse;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;

import database.Animal;
import database.Fact;
import database.Place;
import exceptions.NotFoundException;
import exceptions.NotImplementedException;
import exceptions.WitException;

public class FactProcessor {
	private final DatabaseConfig dbConfig;

	public FactProcessor(DatabaseConfig dbConfig) {
		this.dbConfig = dbConfig;
	}
	
	public String addFact(WitResponse witResponse) throws SQLException, WitException, NotImplementedException {
		AddFactProcess processor = new AddFactProcess(dbConfig, witResponse);
		UUID factId = processor.process(witResponse);
		return factId.toString();
	}
	
	public String deleteFact(WitResponse witResponse) throws SQLException, WitException, NotImplementedException, NotFoundException {
		DeleteFactProcess processor = new DeleteFactProcess(dbConfig, witResponse);
		return processor.revertProcess(witResponse);
	}
	
	public Fact getFact(String id) throws SQLException {
		PsqlClient psqlClient = new PsqlClient(dbConfig);
		return psqlClient.getFactFromId(id);
	}
}
