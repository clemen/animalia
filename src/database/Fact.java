package database;

import java.util.UUID;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="facts")
public class Fact {
	public static final String GUID = "guid";
	public static final String FACT = "fact";
	
	@DatabaseField(columnName = GUID, canBeNull = false, unique = true, generatedId = true)
	private UUID guid;

	@DatabaseField(columnName = FACT, canBeNull = false, unique = true)
	private String fact;
	
	public Fact() {
		
	}
	
	public Fact(String fact) {
		this.fact = fact;
	}
	
	@Override
	public int hashCode() {
		return guid.hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		return guid == ((Fact)other).getGuid();
	}

	public UUID getGuid() {
		return guid;
	}

	public String getFact() {
		return fact;
	}
	
	
}
