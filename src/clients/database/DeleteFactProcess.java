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

public class DeleteFactProcess{
	private final WitResponse witResponse;
	private final PsqlClient psqlClient;

	public DeleteFactProcess(DatabaseConfig dbConfig, WitResponse witResponse) {
		this.psqlClient = new PsqlClient(dbConfig);
		this.witResponse = witResponse;
	}

	public String revertProcess(WitResponse witResponse) throws SQLException, NotFoundException, WitException, NotImplementedException {
		Fact fact = psqlClient.getFactFromFact(URLDecoder.decode(witResponse.get_text()));
		if (fact == null) {
			throw new NotFoundException("Fact not found");
		}
		Outcome outcome = witResponse.getOutcome();
		UUID factId = fact.getGuid();
		try {
			if (FactProcessor.ANIMAL_PLACE_FACT.equals(outcome.getIntent())) {
				if (outcome.getEntities() == null) {
					psqlClient.deleteFact(factId);
					return factId.toString();
				}
				if (outcome.getEntities().getAnimal() == null || outcome.getEntities().getPlace() == null) {
					throw new WitException("Wit failed to parse the fact properly: " + witResponse);
				}
				// the walrus lives in the sea but the wolf lives in the forest
				// the walrus and the salmon live in the sea
				// the walrus and the salmon live in the sea but the salmon also lives in rivers
				// the salmon lives in sea and rivers
				if (outcome.getEntities().getAnimal().size() > 1) {
					throw new NotImplementedException("facts with several animals are not supported");
				}
				String animalStr = outcome.getEntities().getAnimal().get(0).getValue();
				Animal animal = psqlClient.getAnimal(animalStr);
				for (Entity entity: outcome.getEntities().getPlace()) {
					String placeName = entity.getValue();
					psqlClient.deletePlace(placeName, animal);
				} 
				psqlClient.tryDeleteAnimal(animal);
			}
			else {
				throw new NotImplementedException("intent " + witResponse.getOutcome().getIntent() + "has not been implemented yet");
			}
		}
		finally {
			psqlClient.deleteFact(factId);
		}
		return factId.toString();

	}



}
