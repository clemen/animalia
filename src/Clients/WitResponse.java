package Clients;

import java.util.List;

import Intents.Outcome;

public class WitResponse {
//	{
//		  "msg_id" : "657ea489-e7d2-4d2f-8bb6-ea1c4c3acb9c",
//		  "_text" : "How many animals have fins?",
//		  "outcomes" : [ {
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
//		  } ]
//		}
	private String msg_id;
	private String _text;
	private List<Outcome> outcomes;
	public WitResponse(String msg_id, String _text, List<Outcome> outcomes) {
		this.msg_id = msg_id;
		this._text = _text;
		this.outcomes = outcomes;
	}
	
	public String toString() {
		return "msg_id: " + msg_id + ", " + 
				"_text: " + _text + ", " + 
						"outcomes: " + outcomes ;
	}
	

}
