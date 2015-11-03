package Clients;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import Intents.Entities;
import Intents.Entity;
import Intents.Outcome;
import exceptions.WitException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class WitClient {
	private final String endpoint = "https://api.wit.ai/";
	private final String token = "JZKCMFUAZKZ5FQZT3JXEZVJM2XVNNPXI";
	private final String dateFormat = "yyyyMMdd";
	//curl -H 'Authorization:Bearer JZKCMFUAZKZ5FQZT3JXEZVJM2XVNNPXI' 'https://api.wit.ai/message?v=20151030&q=How%20many%20animals%20have%20fins%3F'
	
	public WitResponse sendMessage(String message) throws WitException {
	DefaultHttpClient httpClient = new DefaultHttpClient();
//
	URI uri;
	try {
		uri = new URIBuilder(endpoint + "message")
		    .addParameter("v", new DateTime().toString(dateFormat))
		    .addParameter("q", URLEncoder.encode(message, "UTF-8"))
		    .build();
	} catch (UnsupportedEncodingException | URISyntaxException e) {
		throw new WitException(e.getMessage());
	}
	HttpGet getRequest = new HttpGet(uri);
	getRequest.addHeader("accept", "application/json");
	getRequest.addHeader("Authorization", "Bearer "+ token);

	HttpResponse response;
	try {
		response = httpClient.execute(getRequest);
	} catch (IOException e) {
		throw new WitException(e.getMessage());
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
	} catch (UnsupportedOperationException | IOException e) {
		throw new WitException(e.getMessage());
	}
	httpClient.getConnectionManager().shutdown();
	return resp;

  } 
}
