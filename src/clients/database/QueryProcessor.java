package clients.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import database.Animal;
import database.AnimalAttribute;
import database.BodyPart;
import database.Food;
import database.NamedObject;
import database.Place;
import exceptions.NotImplementedException;
import exceptions.WitException;

public class QueryProcessor {
	public static final String WHICH_ANIMAL_QUESTION = "which_animal_question";
	public static final String ANIMAL_HOW_MANY_QUESTION = "animal_how_many_question";
	public static final String ANIMAL_SCALES_QUERY = "animal_scales_query";
	public static final String ANIMAL_PLACE_QUESTION = "animal_place_question";
	public static final String ANIMAL_ATTRIBUTE_QUESTION = "animal_attribute_question";
	private final DatabaseConfig dbConfig;
	private final Function<Place, String> PLACE_TO_NAME = 
			new Function<Place,String>() { 
		public String apply(Place f) { return f.getName(); }
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
		Set<String> animalNames = getAnimalNames(list);
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

	private <O extends AnimalAttribute> Set<String> getAnimalNames(List<O> attributes) {
		Set<String> animalNames = new LinkedHashSet<String>();
		for (int i = 0; i < attributes.size(); i += 1) {
			animalNames.add(attributes.get(i).getAnimal().getName());
		}
		return animalNames;
	}

	private String listToString(ForeignCollection<? extends NamedObject> animals ) {
		if (animals.isEmpty()) {
			return "none";
		}
		Iterator<? extends NamedObject> it = animals.iterator();
		StringBuilder stb = new StringBuilder();
		stb.append(it.next().getName());
		while (it.hasNext()) {
			String st = it.next().getName();
			if (it.hasNext()) {
				stb.append(", " + st);
			}
			else {
				stb.append(" and " + st);
			}
		}
		return stb.toString();
	}

	private String listToString(Collection<? extends NamedObject> animals ) {
		if (animals.isEmpty()) {
			return "none";
		}
		Iterator<? extends NamedObject> it = animals.iterator();
		StringBuilder stb = new StringBuilder();
		stb.append(it.next().getName());
		while (it.hasNext()) {
			String st = it.next().getName();
			if (it.hasNext()) {
				stb.append(", " + st);
			}
			else {
				stb.append(" and " + st);
			}
		}
		return stb.toString();
	}

	public String getWhichAnswerAnimal(String columnName, List<Entity> entities, 
			List<Entity> negation, String value ) throws WitException, SQLException {
		Dao<Animal, Integer> dao = dbConfig.getAnimalDao();
		if (negation != null) {
			return format(dao.queryBuilder().where().not().eq(columnName, value).query());
		}
		return format(dao.queryBuilder().where().eq(columnName, value).query());
	}

	public <O, I, C> QueryBuilder<O, I> buildQueryBuilder(Dao<O, I> dao, Outcome outcome, 
			List<Entity> attributes, String colName, C attr) throws SQLException, NotImplementedException {
		QueryBuilder<O, I> qb = dao.queryBuilder();
		Where<O, I> wh = qb.where();
		boolean first = true;
		for (Entity entity : attributes) {
			if (!first) {
				wh.and();
			}
			if (attr instanceof String) {
				wh.eq(colName, entity.getValue());
			}
			else if (attr instanceof Integer) {
				wh.eq(colName, Integer.parseInt(entity.getValue()));
			}
			else {
				throw new NotImplementedException("unknown column type " + attr);
			}
			first = false;
		}
		return qb;
	}
	
	public QueryBuilder<Animal, Integer> buildAnimalQueryBuilder(Dao<Animal, Integer> dao, String col, String val) throws SQLException {
		QueryBuilder<Animal, Integer> qb = dao.queryBuilder();
		qb.where().eq(col, val);
		return qb;
	}
	
	public String query(WitResponse witResponse) throws WitException, SQLException, NotImplementedException {
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
		// TODO: WHICH & HOW_MANY are actually almost the same. Reuse
		else if (ANIMAL_HOW_MANY_QUESTION.equals(outcome.getIntent())) {
			// how many animals live in the mountains
			//eat berries
			//have legs
			//have scales
			QueryBuilder<BodyPart, Integer> bodyQb = null;
			QueryBuilder<Food, Integer> foodQb = null;
			QueryBuilder<Place, Integer> placeQb = null;
			QueryBuilder<Animal, Integer> animalQb = null;
			Integer count = 0;
			Integer requestComponents = 0;
			if (outcome.getEntities().getBodyPart() != null) {
				bodyQb = buildQueryBuilder(dbConfig.getBodyPartDao(), outcome, 
						outcome.getEntities().getBodyPart(), BodyPart.NAME, new String());
				count = bodyQb.query().size();
				requestComponents += 1;
			}
			if (outcome.getEntities().getFood() != null) {
				foodQb = buildQueryBuilder(dbConfig.getFoodDao(), outcome, 
						outcome.getEntities().getFood(), Food.NAME, new String());
				count = foodQb.query().size();
				requestComponents += 1;
			}
			if (outcome.getEntities().getPlace() != null) {
				placeQb = buildQueryBuilder(dbConfig.getPlaceDao(), outcome, 
						outcome.getEntities().getPlace(), Place.NAME, new String());
				count = placeQb.query().size();
				requestComponents += 1;
			}
			if (outcome.getEntities().getFur() != null) {
				animalQb = buildAnimalQueryBuilder(dbConfig.getAnimalDao(), Animal.COAT, Coat.FUR.name());
				count = animalQb.query().size();
				requestComponents += 1;
			}
			if (outcome.getEntities().getScales() != null) {
				animalQb = buildAnimalQueryBuilder(dbConfig.getAnimalDao(), Animal.COAT, Coat.SCALES.name());
				count = animalQb.query().size();
				requestComponents += 1;
			}
			if (outcome.getEntities().getSpecies() != null && outcome.getEntities().getSpecies().size() == 1) {
				animalQb = buildAnimalQueryBuilder(dbConfig.getAnimalDao(), Animal.SPECIES, outcome.getEntities().getSpecies().get(0).getValue());
				count = animalQb.query().size();
				requestComponents += 1;
			}
			if (requestComponents > 1) {
				// have fur and eat berries : can't do now because wit does not parse 'and' and 'or' correctly. Use joins once this is done
				throw new NotImplementedException("We don't support composite entities for now");
			}
			return count.toString();
			
		}
		else if (ANIMAL_PLACE_QUESTION.equals(outcome.getIntent())) {
			// where does the otter live?
			// does the otter live in the sea?
			if (outcome.getEntities().getAnimal() == null || outcome.getEntities().getAnimal().size() != 1) {
				throw new WitException("invalid animal: " + witResponse);
			}
			Animal animal = getAnimal(outcome);
			if (animal != null) {
				ForeignCollection<Place> places = animal.getPlaces();
				if (outcome.getEntities().getPlace() == null) {
					return "the " + animal.getName() + " lives in " + listToString(places);
				}
				else {
					//				List<String> placesStrList = Lists.transform(new ArrayList<Place>(places), PLACE_TO_NAME);
					List<String> placesStrList = new ArrayList<String>();
					for (Place place: places) {
						placesStrList.add(place.getName());
					}
					for (Entity entity: outcome.getEntities().getPlace()) {
						if (!places.contains(entity.getValue())) {
							return "No.";
						}
					}
					return "Yes.";
				}
			}
			throw new NotImplementedException("We know nothing about this animal ");
		}
		else if (ANIMAL_SCALES_QUERY.equals(outcome.getIntent())) {
			// does the snake have scales?
			if (outcome.getEntities().getAnimal() == null || outcome.getEntities().getAnimal().size() != 1) {
				throw new WitException("No animal found,  " + witResponse);
			}
			Animal animal = getAnimal(outcome);
			if (Coat.SCALES.equals(animal.getCoat())) {
				return "Yes.";
			}
			return "No.";
		}
		throw new  NotImplementedException("Intent not implemented.");
	}

	private Animal getAnimal(Outcome outcome) throws SQLException {
		String animalName = outcome.getEntities().getAnimal().get(0).getValue();
		Dao<Animal, Integer> dao = dbConfig.getAnimalDao();
		PreparedQuery<Animal> query = dao.queryBuilder().where().eq(Animal.NAME, animalName).prepare();
		return dao.queryForFirst(query);
	}

}
