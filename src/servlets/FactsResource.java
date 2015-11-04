package servlets;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import clients.wit.WitClient;
import clients.wit.WitResponse;

import com.google.gson.Gson;

import database.Animal;
import database.Fact;
import exceptions.NotFoundException;
import exceptions.NotImplementedException;
import exceptions.WitException;

@Path("/facts")
public class FactsResource {
	private static final float MIN_CONFIDENCE = 0.3F;
	@Context AnimaliaServletContext context;
	//	@GET
	//	@Path("{id}")
	//	@Produces(MediaType.TEXT_HTML)
	//	public String getFact(@PathParam("id") String id) {
	//
	//		return "<html> " + "<title>" + "fact number " + id  + "</title>"
	//				+ "<body><h1>" + "fact number " + id + "</body></h1>" + "</html> ";
	//	}

	// TODO: look at this https://github.com/DominikAngerer/Boostraped-Jersey-RestAPI to 
	// use gson request/response deserializer automatically (native is jackson)
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFactJson(@PathParam("id") String id) {
		try {
			Fact fact = context.factProcessor.getFact(id);
			if (fact == null) {
				return Response.status(404).build();
			}
			return Response.status(200).entity(context.gson.toJson(
					new AnimaliaResponse.Builder().withId(fact.getGuid().toString()).withFact(fact.getFact()).build())).build();
		} catch (SQLException e) {
			return Response.status(404).build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postFact(String postFactRequestString) {
		PostFactRequest postFactRequest = context.gson.fromJson(postFactRequestString, PostFactRequest.class);
		WitClient wit = context.witClient;
		System.out.println(context.gson.toJson(postFactRequest));
		try {
			WitResponse witResponse = wit.sendMessage(postFactRequest.getFact());
			if (witResponse.getOutcome().getConfidence() < MIN_CONFIDENCE) {
				return Response.status(400).entity(context.gson.toJson(new AnimaliaResponse.Builder().withMessage("Failed to parse your fact").build())).build();
			}
			String factId = context.factProcessor.addFact(witResponse);
			return Response.status(200).entity(context.gson.toJson(new AnimaliaResponse.Builder().withId(factId).build())).build();
		} catch (WitException | SQLException  e) {
			return Response.status(400).entity(context.gson.toJson(new AnimaliaResponse.Builder().withMessage("Failed to parse your fact").build())).build();
		} catch (Exception e) {
			// TODO: add logger
			return Response.status(400).entity(context.gson.toJson(new AnimaliaResponse.Builder().withMessage("Failed to parse your fact").build())).build();
		}

	}

	@DELETE
	@Path("{id}")
	public Response deleteFact(@PathParam("id") String id) {
		Fact fact;
		try {
			fact = context.factProcessor.getFact(id);
			WitResponse witResponse = context.witClient.sendMessage(fact.getFact());
			context.factProcessor.deleteFact(witResponse);
			return Response.status(200).entity(context.gson.toJson(new AnimaliaResponse.Builder().withId(id).build())).build();
		} catch (SQLException | WitException | NotImplementedException | NotFoundException e) {
			return Response.status(404).build();
		} 
	}
}
