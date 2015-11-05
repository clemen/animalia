package servlets;

import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import clients.wit.WitResponse;

import com.google.gson.Gson;

import exceptions.WitException;

@Path("")
public class QueryServlet {
	private static final float MIN_CONFIDENCE = 0.3F;
	@Context AnimaliaServletContext context;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response query(@QueryParam(value = "q") final String q) {
		WitResponse witResponse;
		try {
			witResponse = context.witClient.sendMessage(q);
			if (witResponse.getOutcome().getConfidence() < MIN_CONFIDENCE) {
				return Response.status(400).entity(context.gson.toJson(new AnimaliaResponse.Builder().withMessage("Failed to parse your fact").build())).build();
			}
			String response = context.queryProcessor.query(witResponse);			
			return Response.status(200).entity(context.gson.toJson(new AnimaliaResponse.Builder().withFact(response).build())).build();
		} catch (WitException | SQLException e) {
			return Response.status(400).entity(context.gson.toJson(new AnimaliaResponse.Builder().withMessage("I can't answer your question.").build())).build();
		}
	}
}
