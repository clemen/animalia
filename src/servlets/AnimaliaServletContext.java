package servlets;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.http.impl.client.DefaultHttpClient;

import clients.database.FactProcessor;
import clients.database.QueryProcessor;
import clients.wit.WitClient;

import com.google.gson.Gson;

@WebListener
public class AnimaliaServletContext implements ServletContextListener {
	public static WitClient witClient;
	public static DatabaseConfig dbConfig;
	public static FactProcessor factProcessor;
	public static QueryProcessor queryProcessor;
	public static DefaultHttpClient httpClient;
	public static Gson gson = new Gson();
	
	public void contextInitialized(ServletContextEvent servletContextEvent) 
	{

		System.out.println("####### contextInitialized(ServletContextEvent e)");
		httpClient = new DefaultHttpClient();
		witClient = new WitClient(httpClient);
		dbConfig = new DatabaseConfig();
		try {
			dbConfig.setup();
		} catch (Exception e) {
			throw new RuntimeException("Failed to setup the database connection " + e.getMessage());
		}
		factProcessor = new FactProcessor(dbConfig);
		queryProcessor = new QueryProcessor(dbConfig);
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) 
	{
		httpClient.getConnectionManager().shutdown();
	}

}
