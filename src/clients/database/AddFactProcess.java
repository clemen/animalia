package clients.database;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import model.Coat;
import servlets.DatabaseConfig;
import clients.wit.Entity;
import clients.wit.Outcome;
import clients.wit.WitResponse;
import database.Animal;
import exceptions.NotImplementedException;
import exceptions.WitException;

public class AddFactProcess extends FactProcessorHelper{

	public AddFactProcess(DatabaseConfig dbConfig, WitResponse witResponse) {
		super(dbConfig, witResponse);
	}



	public UUID process(WitResponse witResponse) throws WitException, NotImplementedException, SQLException {
		//TODO: stem the animals to make sure at least case and plurals are removed
		PsqlClient.StoredFact storedFact = psqlClient.storeFact(witResponse.get_text());
		UUID factId = storedFact.factId;
		if (storedFact.stored) {
			Outcome outcome = witResponse.getOutcome();
			if (FactProcessor.ANIMAL_PLACE_FACT.equals(outcome.getIntent())) {
				// TODO: add a getFieldEntities in Entities with a swtch on the enum
				// Facts.ANIMAL_PLACE_FACT to outcome.getEntities().getPlace()
				List<Entity> placeEntities = outcome.getEntities().getPlace();
				String animalStr = validateAndGetAnimal(outcome, factId, placeEntities);
				if (animalStr == null) {
					return factId;
				}

				Animal animal = psqlClient.getOrSetAnimal(animalStr);
				for (Entity entity: placeEntities) {
					String placeName = entity.getValue();
					// TODO: move this to AnimalDao that extends AbstractDao<Animal, UUID> that extends Dao<Animal, UUID>: 
					//				animalDao.getOrSet(Animal.LEG_COUNT, legCountName, animal);
					//				replace psqlClient with the list of daos
					//				move what is in those if statements to 
					//				processHelper(outcome.getEntities().getNumber(), dbConfig.animalDao)
					//				signature: processHelper(List<Entity> legCountEntities, AbstractDao<Animal, UUID> dao)
					psqlClient.getOrSetPlace(placeName, animal);
				} 
			}
			else if (FactProcessor.ANIMAL_BODY_FACT.equals(outcome.getIntent())) {
				List<Entity> entities = outcome.getEntities().getBodyPart();
				String animalStr = validateAndGetAnimal(outcome, factId, entities);
				if (animalStr == null) {
					return factId;
				}

				Animal animal = psqlClient.getOrSetAnimal(animalStr);
				for (Entity entity: entities) {
					String name = entity.getValue();
					psqlClient.getOrSetBodyPart(name, animal);
				} 
			}

			else if (FactProcessor.ANIMAL_EAT_FACT.equals(outcome.getIntent())) {
				List<Entity> entities = outcome.getEntities().getFood();
				String animalStr = validateAndGetAnimal(outcome, factId, entities);
				if (animalStr == null) {
					return factId;
				}

				Animal animal = psqlClient.getOrSetAnimal(animalStr);
				for (Entity entity: entities) {
					String name = entity.getValue();
					psqlClient.getOrSetFood(name, animal);
				} 
			}
			else if (FactProcessor.ANIMAL_LEG_FACT.equals(outcome.getIntent())) {
				List<Entity> legCountEntities = outcome.getEntities().getNumber();
				String animalStr = validateAndGetAnimal(outcome, factId, legCountEntities);
				if (animalStr == null) {
					return factId;
				}
				Animal animal = psqlClient.getOrPrepareAnimal(animalStr);
				if (legCountEntities.size() > 1) {
					psqlClient.deleteFact(factId);
					throw new WitException("Wit failed to parse the fact properly. Found several numbers for legCount: " + witResponse);
				}
				String legCountName = legCountEntities.get(0).getValue();
				psqlClient.getOrSetLegCount(legCountName, animal, animal.getAnimalId() == null);
				if (Integer.parseInt(legCountName) != 0) {
					psqlClient.getOrSetBodyPart("leg", animal);
				}
			}
			else if (FactProcessor.ANIMAL_FUR_FACT.equals(outcome.getIntent())) {
				List<Entity> furEntities = outcome.getEntities().getFur();
				String animalStr = validateAndGetAnimal(outcome, factId, furEntities);
				if (animalStr == null) {
					return factId;
				}
				Animal animal = psqlClient.getOrPrepareAnimal(animalStr);
				if (furEntities.size() > 1) {
					psqlClient.deleteFact(factId);
					throw new WitException("Wit failed to parse the fact properly. Found several fur entities: " + witResponse);
				}
				psqlClient.getOrSetCoat(Coat.FUR.toString(), animal, animal.getAnimalId() == null);
			}
			else if (FactProcessor.ANIMAL_SCALES_FACT.equals(outcome.getIntent())) {
				List<Entity> entities = outcome.getEntities().getScales();
				String animalStr = validateAndGetAnimal(outcome, factId, entities);
				if (animalStr == null) {
					return factId;
				}
				Animal animal = psqlClient.getOrPrepareAnimal(animalStr);
				if (entities.size() > 1) {
					psqlClient.deleteFact(factId);
					throw new WitException("Wit failed to parse the fact properly. Found several fur entities: " + witResponse);
				}
				psqlClient.getOrSetCoat(Coat.SCALES.toString(), animal, animal.getAnimalId() == null);
			}
			else if (FactProcessor.ANIMAL_SPECIES_FACT.equals(outcome.getIntent())) {
				List<Entity> entities = outcome.getEntities().getSpecies();
				String animalStr = validateAndGetAnimal(outcome, factId, entities);
				if (animalStr == null) {
					return factId;
				}
				Animal animal = psqlClient.getOrPrepareAnimal(animalStr);
				if (entities.size() > 1) {
					psqlClient.deleteFact(factId);
					throw new WitException("Wit failed to parse the fact properly. Found several numbers for legCount: " + witResponse);
				}
				String species = entities.get(0).getValue();
				psqlClient.getOrSetSpecies(species, animal, animal.getAnimalId() == null);
			}
			else {
				psqlClient.deleteFact(factId);
				throw new NotImplementedException("intent " + witResponse.getOutcome().getIntent() + "has not been implemented yet");
			}
		}
		return factId;
	}



}
