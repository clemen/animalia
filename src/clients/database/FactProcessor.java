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
	public static final String ANIMAL_PLACE_FACT = "animal_place_fact";
	public static final String ANIMAL_LEG_FACT = "animal_leg_fact";
	public static final String ANIMAL_FUR_FACT = "animal_fur_fact";
	public static final String ANIMAL_SCALES_FACT = "animal_scales_fact";
	public static final String ANIMAL_EAT_FACT = "animal_eat_fact";
	public static final String ANIMAL_BODY_FACT = "animal_body_fact";
	public static final String ANIMAL_SPECIES_FACT = "animal_species_fact";
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
