package clients.database;

import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import model.Coat;
import servlets.DatabaseConfig;
import clients.wit.Entities;
import clients.wit.Entity;
import clients.wit.Outcome;
import clients.wit.WitResponse;

import com.google.common.base.Function;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import database.Animal;
import database.AnimalAttribute;
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

	private String format(List<Animal> animalList) {
		Set<String> animalNames = new LinkedHashSet<String>();
		for (int i = 0; i < animalList.size(); i += 1) {
			animalNames.add(animalList.get(i).getName());
		}
		String str = animalNames.toString();
		return str.substring(1, str.length() - 1);
	}
	
	private <O extends AnimalAttribute, I> String getWhichAnswerAnimalAttribute(Dao<O, I> dao, List<Entity> entities, 
			List<Entity> negation ) throws WitException, SQLException {
		int entitySz = entities.size();
		if (entitySz == 0) {
			throw new  WitException("wit parsed no object. ");
		}
		QueryBuilder<O, I> qb =
				dao.queryBuilder();
		Where<O, I> where = qb.where();
		if (negation != null) {
			where.not();
		}
		where.eq(AnimalAttribute.NAME, entities.get(0).getValue());
		for (int i = 1; i < entitySz; i+=1) {
			if (negation != null) {
				where.and().not();
			}
			else {
				where.or();
			}
			where.eq(AnimalAttribute.NAME, entities.get(i).getValue());
		}
		//TODO: remove (for debug)
		PreparedQuery<O> prepared = qb.prepare();
		List<O> list = qb.query();
		Set<String> animalNames = new LinkedHashSet<String>();
		for (int i = 0; i < list.size(); i += 1) {
			animalNames.add(list.get(i).getAnimal().getName());
		}
		String str = animalNames.toString();
		return str.substring(1, str.length() - 1);
		//		Function<A extends AnimalAttribute, String> COL_TO_ANIMAL_NAME = 
		//				new Function<A extends AnimalAttribute,String>() { 
		//			public String apply(A f) { return f.getAnimal().getName(); }
		//		};
		//		List<String> animalNames = Lists.transform(list, COL_TO_ANIMAL_NAME);
		//		Set<String> dedupedAnimalNames = new LinkedHashSet<>(animalNames);
		//		String str = dedupedAnimalNames.toString();
		//		return str.substring(1, str.length() - 1);
	}

	public String getWhichAnswerAnimal(String columnName, List<Entity> entities, 
			List<Entity> negation, String value ) throws WitException, SQLException {
		Dao<Animal, Integer> dao = dbConfig.getAnimalDao();
		if (negation != null) {
			return format(dao.queryBuilder().where().not().eq(columnName, value).query());
		}
		return format(dao.queryBuilder().where().eq(columnName, value).query());
	}
	
	public String query(WitResponse witResponse) throws WitException, SQLException {
		Outcome outcome = witResponse.getOutcome();
		if (WHICH_ANIMAL_QUESTION.equals(outcome.getIntent())) {
			Entities entities = outcome.getEntities();
			if (entities.getFood() != null) {
				return getWhichAnswerAnimalAttribute(dbConfig.getFoodDao(), entities.getFood(), 
						entities.getNegation() );
			}
			else if (entities.getFur() != null) {
				return getWhichAnswerAnimal(Animal.COAT, entities.getFur(), entities.getNegation(), Coat.FUR.name());
			}
			else if (entities.getPlace() != null) {
				return getWhichAnswerAnimalAttribute(dbConfig.getPlaceDao(), entities.getPlace(), 
						entities.getNegation() );
			}
			else if (entities.getScales() != null) {
				return getWhichAnswerAnimal(Animal.COAT, entities.getFur(), entities.getNegation(), Coat.SCALES.name());
			}
			else if (entities.getSpecies() != null) {
				if (entities.getSpecies() == null || entities.getSpecies().size() != 1) {
					throw new WitException("invalid or no species" + witResponse);
				}
				return getWhichAnswerAnimal(Animal.SPECIES, entities.getSpecies(), entities.getNegation(), entities.getSpecies().get(0).getValue());
			}
			else if (entities.getBodyPart() != null) {
				return getWhichAnswerAnimalAttribute(dbConfig.getBodyPartDao(), entities.getBodyPart(), 
						entities.getNegation() );
			}
			else {
				throw new WitException("Could not parse question properly " + witResponse);
			}
		}
		return "only which_animal_questions are implemented right now";
	}

}
