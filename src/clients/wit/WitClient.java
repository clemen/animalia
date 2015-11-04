package clients.wit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.joda.time.DateTime;

import servlets.DatabaseConfig;
import Intents.AnimalPlaceFact;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import exceptions.NotImplementedException;
import exceptions.WitException;

public class WitClient {
	private final String endpoint = "https://api.wit.ai/";
	private final String token = "JZKCMFUAZKZ5FQZT3JXEZVJM2XVNNPXI";
	private final String dateFormat = "yyyyMMdd";
	private final HttpClient httpClient;

	public WitClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public WitResponse sendMessage(String message) throws WitException, SQLException {


		URI uri;
		try {
			uri = new URIBuilder(endpoint + "message")
			.addParameter("v", new DateTime().toString(dateFormat))
			.addParameter("q", URLEncoder.encode(message, "UTF-8"))
			.build();
		} catch (UnsupportedEncodingException | URISyntaxException e) {
			throw new WitException("error in wit uri", e);
		}
		HttpGet getRequest = new HttpGet(uri);
		getRequest.addHeader("accept", "application/json");
		getRequest.addHeader("Authorization", "Bearer "+ token);

		HttpResponse response;
		try {
			response = httpClient.execute(getRequest);
		} catch (IOException e) {
			throw new WitException("error while calling wit", e);
		}

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException(" Wit call Failed : HTTP error code : "
					+ response.getStatusLine().getStatusCode());
		}

		//	BufferedReader br = new BufferedReader(
		//                     new InputStreamReader((response.getEntity().getContent())));
		//
		//	String output;
		//	System.out.println("Output from WIT .... \n");
		//	while ((output = br.readLine()) != null) {
		//		System.out.println(output);
		//	}

		Gson gson = new GsonBuilder().create();
		WitResponse resp = null;
		try (BufferedReader reader = new BufferedReader( new InputStreamReader((response.getEntity().getContent()))); ) {
			resp = gson.fromJson(reader, WitResponse.class);
			System.out.println("response from server: " + resp);
		}
		catch (UnsupportedOperationException | IOException e) {
			throw new WitException("error while reading response from wit", e);
		}

		return resp;

	} 
}
