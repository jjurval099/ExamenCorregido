package es.psp.sutil_mesa_unidad2;

import java.io.File;
import java.io.IOException;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.psp.sutil_mesa_unidad2.client.ClientPetitions;
import es.psp.sutil_mesa_unidad2.exception.BonoLotoException;

/**
 * LauncherClient Class 
 */
public class LauncherClient
{
	
    /** The logger for this class. */
    public final static Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) throws BonoLotoException 
    {
        // Create an HTTP client to make requests            	
        CloseableHttpClient httpClientRequest = HttpClients.createDefault();
        
        // Initialize the petitions object to the client specifying the service URL         
        ClientPetitions petitions = new ClientPetitions("http://localhost:8080/bonoloto");
        
        // Send a CSV file to the service         
        petitions.sendCSVFile(httpClientRequest, File.separator + "historico.csv");
        
        // Make several queries to the service to check bets and get hot and cold numbers         
        petitions.checkBet(httpClientRequest, "28/12/2023", 4, 15, 20, 36, 37, 40, 3);
        petitions.hotNumbers(httpClientRequest, "28/11/2023", 7);
        petitions.coldNumbers(httpClientRequest, "28/11/2023", 2);
        
        // Close the HTTP client after usage
         
        if (httpClientRequest != null) 
        {
            try 
            {
                httpClientRequest.close();
            } 
            catch (IOException ioException)
            {
                // If an error occurs while closing the client, log an error message and throw a BonoLotoException
                String errorString = "Error closing the HTTP client";
                LOGGER.error(errorString, ioException);
                throw new BonoLotoException(8, errorString);
            }
        }
    }
}
