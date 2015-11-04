package servlets;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

import clients.database.FactProcessor;
import clients.wit.WitClient;

@WebListener
public class AnimaliaServletContext implements ServletContextListener {
	public static WitClient witClient;
	public static DatabaseConfig dbConfig;
	public static FactProcessor psqlClient;
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
		psqlClient = new FactProcessor(dbConfig);
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) 
	{
		httpClient.getConnectionManager().shutdown();
	}

}
