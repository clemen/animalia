package clients.wit;

import java.util.List;

public class Entities {
	private final List<Entity> relationship;
	private final List<Entity> body_part;
	private final List<Entity> food;
	private final List<Entity> place;
	private final List<Entity> animal;
	private final List<Entity> fur;
	private final List<Entity> scales;
	private final List<Entity> species;
	private final List<Entity> number;

	private Entities (Builder builder) {
		this.relationship =  builder.relationship;
		this.body_part = builder.body_part;
		this.food = builder.food;
		this.place = builder.place;
		this.animal = builder.animal;
		this.fur = builder.fur;
		this.scales = builder.scales;
		this.species = builder.species;
		this.number = builder.number;
	}

	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		if (relationship != null) {
			stringBuilder.append("relationship: " + relationship + ",");
		}
		if (body_part != null) {
			stringBuilder.append("body_part: " + body_part + ",");
		}
		if (food != null) {
			stringBuilder.append("food: " + food + ",");
		}
		if (place != null) {
			stringBuilder.append("place: " + place + ",");
		}
		if (animal != null) {
			stringBuilder.append("animal: " + animal);
		}
		if (fur != null) {
			stringBuilder.append("fur: " + fur + ",");
		}
		if (scales != null) {
			stringBuilder.append("scales: " + scales + ",");
		}
		if (species != null) {
			stringBuilder.append("species: " + species + ",");
		}
		if (number != null) {
			stringBuilder.append("number: " + number + ",");
		}
		return stringBuilder.toString();
	}

	public static class Builder {
		private List<Entity> relationship;
		private List<Entity> body_part;
		private List<Entity> food;
		private List<Entity> place;
		private List<Entity> animal;
		private List<Entity> fur;
		private List<Entity> scales;
		private List<Entity> species;
		private List<Entity> number;

		public Builder withRelationship(List<Entity> relationship) {
			this.relationship = relationship;
			return this;
		}

		public Builder withBodyPart(List<Entity> body_part) {
			this.body_part = body_part;
			return this;
		}

		public Builder withFood(List<Entity> food) {
			this.food = food;
			return this;
		}

		public Builder withPlace(List<Entity> place) {
			this.place = place;
			return this;
		}

		public Builder withAnimal(List<Entity> animal) {
			this.animal = animal;
			return this;
		}

		public Builder withFur(List<Entity> fur) {
			this.fur = fur;
			return this;
		}

		public Builder withSpecies(List<Entity> species ) {
			this.species = species;
			return this;
		}

		public Builder withScales(List<Entity> scales) {
			this.scales = scales;
			return this;
		}

		public Builder withNumber(List<Entity> number) {
			this.number = number;
			return this;
		}

		public Entities build() {
			return new Entities(this);
		}
	}

	public List<Entity> getRelationship() {
		return relationship;
	}

	public List<Entity> getBodyPart() {
		return body_part;
	}

	public List<Entity> getAnimal() {
		return animal;
	}

	public List<Entity> getFood() {
		return food;
	}

	public List<Entity> getPlace() {
		return place;
	}

	public List<Entity> getFur() {
		return fur;
	}

	public List<Entity> getScales() {
		return scales;
	}

	public List<Entity> getSpecies() {
		return species;
	}

	public List<Entity> getNumber() {
		return number;
	}


}
