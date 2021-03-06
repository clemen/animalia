package clients.wit;

public class Entity {
	private String type;
	private String value;
	
	public Entity(String type, String value) {
		this.type = type;
		this.value = value;
	}
	
	public String getType() {
		return type;
	}

	public String getValue() {
		return value.toLowerCase();
	}

	public String toString() {
		return "type: " + type + ", " + 
	"value: " + value;
	}
}
