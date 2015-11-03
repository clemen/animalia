package servlets;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import exceptions.WitException;
import Clients.WitClient;
import Model.Fact;

@Path("/facts")
public class FactsResource {
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
		Gson gson = new Gson();
		return Response.status(200).entity(gson.toJson(new AnimaliaResponse.Builder().withId("fake_id").withFact("fake_fact").build())).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postFact(String postFactRequestString) {
		Gson gson = new Gson();
		PostFactRequest postFactRequest = gson.fromJson(postFactRequestString, PostFactRequest.class);
		WitClient wit = new WitClient();
		System.out.println(gson.toJson(postFactRequest));
			try {
				wit.sendMessage(postFactRequest.getFact());
			} catch (WitException e) {
				return Response.status(400).entity(gson.toJson(new AnimaliaResponse.Builder().withMessage(e.getMessage()).build())).build();
			}
			return Response.status(200).entity(gson.toJson(new AnimaliaResponse.Builder().withId("fake_id").build())).build();

	}
	
	@DELETE
	@Path("{id}")
	public Response deleteFact(@PathParam("id") String id) {
		Gson gson = new Gson();
		return Response.status(200).entity(gson.toJson(new AnimaliaResponse.Builder().withId("fake_id").build())).build();
	}
}
