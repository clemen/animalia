package servlets;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

@Path("")
public class Query {
		@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response query(@QueryParam(value = "q") final String q) {
			Gson gson = new Gson();
			return Response.status(200).entity(gson.toJson(new AnimaliaResponse.Builder().withFact("not implemented. QUERY: " + q).build())).build();
	}
}
