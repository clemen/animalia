package Intents;

import java.util.List;

public class Outcome {
	private String _text;
	private float confidence;
	private String intent;
	private Entities entities;
//{
//		    "_text" : "How many animals have fins?",
//		    "confidence" : 0.995,
//		    "intent" : "animal_how_many_question",
//		    "entities" : {
//		      "relationship" : [ {
//		        "type" : "value",
//		        "value" : "have"
//		      } ],
//		      "body_part" : [ {
//		        "type" : "value",
//		        "value" : "fins"
//		      } ],
//		      "animal" : [ {
//		        "type" : "value",
//		        "value" : "animals"
//		      } ]
//		    }
//		  }
	public Outcome(String _text, float confidence, String intent, Entities entities) {
		this._text = _text;
		this.confidence = confidence;
		this.intent = intent;
		this.entities = entities;
	}

	public String toString() {
		return "_text: " + _text + ", " +
	"confidence: " + confidence + ", " + 
				"intent: " + intent + ", " +
	"entities: " + entities;
	}
	
}
