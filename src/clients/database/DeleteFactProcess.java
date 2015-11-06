package clients.database;

import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import model.Coat;
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

public class DeleteFactProcess extends FactProcessorHelper{

	public DeleteFactProcess(DatabaseConfig dbConfig, WitResponse witResponse) {
		super(dbConfig, witResponse);
	}


	public String revertProcess(WitResponse witResponse) throws SQLException, NotFoundException, WitException, NotImplementedException {
		Fact fact = psqlClient.getFactFromFact(URLDecoder.decode(witResponse.get_text()));
		if (fact == null) {
			// TODO: should we also account for the case where the fact is formulated differently and remove the information from the other tables?
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
			else if (FactProcessor.ANIMAL_BODY_FACT.equals(outcome.getIntent())) {
				List<Entity> entities = outcome.getEntities().getBodyPart();
				String animalStr = validateAndGetAnimal(outcome, factId, entities);
				if (animalStr == null) {
					return factId.toString();
				}

				Animal animal = psqlClient.getAnimal(animalStr);
				if (animal != null) {
					for (Entity entity: entities) {
						String name = entity.getValue();
						psqlClient.deleteBodyPart(name, animal);
					} 
					psqlClient.tryDeleteAnimal(animal);
				}
			}

			else if (FactProcessor.ANIMAL_EAT_FACT.equals(outcome.getIntent())) {
				List<Entity> entities = outcome.getEntities().getFood();
				String animalStr = validateAndGetAnimal(outcome, factId, entities);
				if (animalStr == null) {
					return factId.toString();
				}

				Animal animal = psqlClient.getAnimal(animalStr);
				if (animal != null) {
					for (Entity entity: entities) {
						String name = entity.getValue();
						psqlClient.getOrSetFood(name, animal);
					} 
					psqlClient.tryDeleteAnimal(animal);
				}
			}
			else if (FactProcessor.ANIMAL_LEG_FACT.equals(outcome.getIntent())) {
				List<Entity> legCountEntities = outcome.getEntities().getNumber();
				String animalStr = validateAndGetAnimal(outcome, factId, legCountEntities);
				if (animalStr == null) {
					return factId.toString();
				}
				Animal animal = psqlClient.getOrPrepareAnimal(animalStr);
				if (animal != null) {
					if (legCountEntities.size() > 1) {
						psqlClient.deleteFact(factId);
						return factId.toString();
					}
					String legCountName = legCountEntities.get(0).getValue();
					if (Integer.parseInt(legCountName) != 0) {
						psqlClient.deleteBodyPart("leg", animal);
					}
					psqlClient.tryDeleteAnimal(animal);
				}
			}
			else if (FactProcessor.ANIMAL_FUR_FACT.equals(outcome.getIntent())) {
				List<Entity> furEntities = outcome.getEntities().getFur();
				String animalStr = validateAndGetAnimal(outcome, factId, furEntities);
				if (animalStr == null) {
					return factId.toString();
				}
				Animal animal = psqlClient.getOrPrepareAnimal(animalStr);
				if (animal != null) {
					if (furEntities.size() > 1) {
						psqlClient.deleteFact(factId);
					}
					// TODO: create a function tryDeleteAnimalSingleDependance(animal, columnName) to avoid two calls to the DB here 
					psqlClient.deleteCoat(animal);
					psqlClient.tryDeleteAnimal(animal);
				}
			}
			else if (FactProcessor.ANIMAL_SCALES_FACT.equals(outcome.getIntent())) {
				List<Entity> entities = outcome.getEntities().getScales();
				String animalStr = validateAndGetAnimal(outcome, factId, entities);
				if (animalStr == null) {
					return factId.toString();
				}
				Animal animal = psqlClient.getOrPrepareAnimal(animalStr);
				if (animal != null) {
					if (entities.size() > 1) {
						psqlClient.deleteFact(factId);
					}
					psqlClient.deleteCoat(animal);
					psqlClient.tryDeleteAnimal(animal);
				}
			}
			else if (FactProcessor.ANIMAL_SPECIES_FACT.equals(outcome.getIntent())) {
				List<Entity> entities = outcome.getEntities().getSpecies();
				String animalStr = validateAndGetAnimal(outcome, factId, entities);
				if (animalStr == null) {
					return factId.toString();
				}
				Animal animal = psqlClient.getOrPrepareAnimal(animalStr);
				if (animal != null) {
					if (entities.size() > 1) {
						psqlClient.deleteFact(factId);
					}
					String species = entities.get(0).getValue();
					psqlClient.deleteSpecies(animal);
					psqlClient.tryDeleteAnimal(animal);
				}
			}
			else {
				throw new NotImplementedException("intent " + witResponse.getOutcome().getIntent() + "has not been implemented yet");
			}
		}
		finally {
			Fact f = psqlClient.getFactFromFact(URLDecoder.decode(witResponse.get_text()));
			if (f != null) {
				psqlClient.deleteFact(factId);
			}
		}
		return factId.toString();

	}



}
