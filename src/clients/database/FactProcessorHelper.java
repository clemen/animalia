package clients.database;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import servlets.DatabaseConfig;
import clients.wit.Entity;
import clients.wit.Outcome;
import clients.wit.WitResponse;
import exceptions.NotImplementedException;
import exceptions.WitException;

public abstract class FactProcessorHelper {
	protected final WitResponse witResponse;
	protected final PsqlClient psqlClient;

	public FactProcessorHelper(DatabaseConfig dbConfig, WitResponse witResponse) {
		this.psqlClient = new PsqlClient(dbConfig);
		this.witResponse = witResponse;
	}
	
	protected String validateAndGetAnimal (Outcome outcome, UUID factId, List<Entity> entities) throws SQLException, WitException, NotImplementedException {
		if (outcome.getEntities() == null) {
			psqlClient.deleteFact(factId);
			return null;
		}
		if (outcome.getEntities().getAnimal() == null || entities == null) {
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
		if (outcome.getEntities().getNegation() != null) {
			return null;
		}
		return outcome.getEntities().getAnimal().get(0).getValue();
	}
}
