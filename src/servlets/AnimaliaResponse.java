package servlets;

public class AnimaliaResponse {
	public final String message;
	public final String fact;
	public final String id;

	public AnimaliaResponse(Builder builder) {
		this.message = builder.message;
		this.fact = builder.fact;
		this.id = builder.id;
	}
	
	static class Builder {
		public String message;
		public String fact;
		public String id;
		
		public Builder withMessage(String message) {
			this.message = message;
			return this;
		}
		public Builder withFact(String fact) {
			this.fact = fact;
			return this;
		}
		public Builder withId(String id) {
			this.id = id;
			return this;
		}
		
		public AnimaliaResponse build() {
			return new AnimaliaResponse(this);
		}

	}
}
