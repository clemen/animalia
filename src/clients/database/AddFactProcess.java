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

public class AddFactProcess{
	private final WitResponse witResponse;
	private final PsqlClient psqlClient;
	
	public AddFactProcess(DatabaseConfig dbConfig, WitResponse witResponse) {
		this.psqlClient = new PsqlClient(dbConfig);
		this.witResponse = witResponse;
	}



	public UUID process(WitResponse witResponse) throws WitException, NotImplementedException, SQLException {
		UUID factId = psqlClient.storeFact(URLDecoder.decode(witResponse.get_text()));
		Outcome outcome = witResponse.getOutcome();
		if (outcome.getIntent().equals(PsqlClient.ANIMAL_PLACE_FACT)) {
			if (outcome.getEntities() == null) {
				psqlClient.deleteFact(factId);
				return factId;
			}
			if (outcome.getEntities().getAnimal() == null || outcome.getEntities().getPlace() == null) {
				psqlClient.deleteFact(factId);
				throw new WitException("Wit failed to parse the fact properly: " + witResponse);
			}
			// the walrus lives in the sea but the wolf lives in the forest
			// the walrus and the salmon live in the sea
			// the walrus and the salmon live in the sea but the salmon also lives in rivers
			// the salmon lives in sea and rivers
			if (outcome.getEntities().getAnimal().size() > 1) {
				psqlClient.deleteFact(factId);
				throw new NotImplementedException("facts with several animals are not supported");
			}
			String animalStr = outcome.getEntities().getAnimal().get(0).getValue();
			Animal animal = psqlClient.getOrSetAnimal(animalStr);
			for (Entity entity: outcome.getEntities().getPlace()) {
				String placeName = entity.getValue();
				psqlClient.getOrSetPlace(placeName, animal);
			} 
		}
		else {
			psqlClient.deleteFact(factId);
			throw new NotImplementedException("intent " + witResponse.getOutcome().getIntent() + "has not been implemented yet");
		}
		return factId;
	}



}
