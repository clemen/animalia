package clients.database;

import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import servlets.DatabaseConfig;
import clients.wit.Entity;
import clients.wit.Outcome;
import clients.wit.WitResponse;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import database.Animal;
import database.Food;
import exceptions.WitException;

public class QueryProcessor {
	public static final String WHICH_ANIMAL_QUESTION = "which_animal_question";
	public static final String ANIMAL_HOW_MANY_QUESTION = "animal_how_many_question";
	public static final String ANIMAL_SCALES_QUERY = "animal_scales_query";
	public static final String ANIMAL_PLACE_QUESTION = "animal_place_question";
	public static final String ANIMAL_ATTRIBUTE_QUESTION = "animal_attribute_question";
	private final DatabaseConfig dbConfig;
	private final Function<Food, String> FOOD_TO_ANIMAL_NAME = 
			new Function<Food,String>() { 
		public String apply(Food f) { return f.getAnimal().getName(); }
	};

	public QueryProcessor(DatabaseConfig dbConfig) {
		this.dbConfig = dbConfig;
	}

	public String query(WitResponse witResponse) throws WitException, SQLException {
		Outcome outcome = witResponse.getOutcome();
		if (WHICH_ANIMAL_QUESTION.equals(outcome.getIntent())) {
			List<Entity> relationships = outcome.getEntities().getRelationship(); 
			if (relationships.size() != 1) {
				throw new WitException("wit parsed " + relationships.size() + " relationships. " + witResponse);
			}
			if ("eat".equals(relationships.get(0).getValue())) {
				Dao<Food, Integer> foodDao = dbConfig.getFoodDao();
				Dao<Animal, Integer> animalDao = dbConfig.getAnimalDao();
				List<Entity> foodEntities = outcome.getEntities().getFood();
				int foodNb = foodEntities.size();
				if (foodNb == 0) {
					throw new  WitException("wit parsed no food. " + witResponse);
				}
				QueryBuilder<Food, Integer> foodQb =
						foodDao.queryBuilder();
				Where<Food, Integer> where = foodQb.where();
				if (outcome.getEntities().getNegation() != null) {
					where.not();
				}
				where.eq(Food.NAME, foodEntities.get(0).getValue());

				for (int i = 1; i < foodNb; i+=1) {
					if (outcome.getEntities().getNegation() != null) {
						where.and().not();
					}
					else {
						where.and();
					}
					where.eq(Food.NAME, foodEntities.get(i).getValue());
				}
				List<String> animalNames = Lists.transform(foodQb.query(), FOOD_TO_ANIMAL_NAME);
				Set<String> dedupedAnimalNames = new LinkedHashSet<>(animalNames);
				String str = dedupedAnimalNames.toString();
				return str.substring(1, str.length() - 1);
				// TODO: test this and then remove previous line and do the join query:
				//				QueryBuilder<Animal, Integer> animalQb = animalDao.queryBuilder();
				//				List<Animal> results = animalQb.join(foodQb).query();
			}
		}
		return "bla";
	}

}
