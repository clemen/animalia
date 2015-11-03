package Intents;

import java.util.List;

public class Entities {
	private final List<Entity> relationship;
	private final List<Entity> body_part;
	private final List<Entity> animal;

	private Entities (Builder builder) {
		this.relationship =  builder.relationship;
		this.body_part = builder.body_part;
		this.animal = builder.animal;
	}

	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("relationship: " + relationship + ",");
		stringBuilder.append("body_part: " + body_part + ",");
		stringBuilder.append("animal: " + animal);
		return stringBuilder.toString();
	}

	public static class Builder {
		private List<Entity> relationship;
		private List<Entity> body_part;
		private List<Entity> animal;

		public Builder withRelationship(List<Entity> relationship) {
			this.relationship = relationship;
			return this;
		}

		public Builder withBodyPart(List<Entity> body_part) {
			this.body_part = body_part;
			return this;
		}

		public Builder withAnimal(List<Entity> animal) {
			this.animal = animal;
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
	
	
}
