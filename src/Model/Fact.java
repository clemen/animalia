package Model;

public class Fact {
	private String fact;

	
	
	public Fact(String fact) {
		super();
		this.fact = fact;
	}

	public String getFact() {
		return fact;
	}

	public void setFact(String fact) {
		this.fact = fact;
	}
	
	public String toString() {
		return "fact: " + fact;
	}
	
	
}
