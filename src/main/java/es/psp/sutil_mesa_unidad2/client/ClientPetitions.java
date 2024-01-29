package es.psp.sutil_mesa_unidad2.client;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.psp.sutil_mesa_unidad2.exception.BonoLotoException;

/**
 * This class handles client petitions for BonoLoto service.
 */
public class ClientPetitions 
{
    private String url;
    public final Logger LOGGER = LogManager.getLogger();
    
    /**
     * Constructs a ClientPetitions object with the specified URL.
     * @param url the URL of the BonoLoto service
     */
    public ClientPetitions(String url) 
    {
        this.url = url;
    }

    /**
     * Retrieves hot numbers from the BonoLoto service.
     * @param httpClientRequest the HTTP client
     * @param date the date of the lottery
     * @param hitReference the number of hits reference
     * @throws BonoLotoException if an error occurs during the request
     */
    public void hotNumbers(CloseableHttpClient httpClientRequest, String date, int hitReference) throws BonoLotoException 
    {
        HttpGet request = new HttpGet(this.url + "/hotNumbers");
        request.addHeader("lotteryDate", date);
        request.addHeader("hitsReference", String.valueOf(hitReference));
        CloseableHttpResponse httpServerResponse = null;
        String responseBody = null;
        try 
        {
            httpServerResponse = httpClientRequest.execute(request);
            StatusLine statusLine = httpServerResponse.getStatusLine();
            if (statusLine.getStatusCode() != 200) 
            {
                responseBody = EntityUtils.toString(httpServerResponse.getEntity());
                String errorString = responseBody;
                LOGGER.error(errorString);
                throw new BonoLotoException(9, errorString);
            }
            responseBody = EntityUtils.toString(httpServerResponse.getEntity());
            System.out.println(responseBody);
        } 
        catch (IOException ioException) 
        {
            String errorString = "Error ";
            LOGGER.error(errorString, ioException);
        } 
        finally 
        {
            if (httpServerResponse != null) 
            {
                try 
                {
                    httpServerResponse.close();
                } 
                catch (IOException ioException) 
                {
                    String errorString = "Close error";
                    LOGGER.error(errorString, ioException);
                }
            }
        }
    }

    /**
     * Retrieves cold numbers from the BonoLoto service.
     * @param httpClientRequest the HTTP client
     * @param date the date of the lottery
     * @param hitReference the number of hits reference
     * @throws BonoLotoException if an error occurs during the request
     */
    public void coldNumbers(CloseableHttpClient httpClientRequest, String date, int hitReference) throws BonoLotoException 
    {
        HttpGet request = new HttpGet(this.url + "/coldNumbers");
        request.addHeader("lotteryDate", date);
        request.addHeader("hitsReference", String.valueOf(hitReference));
        CloseableHttpResponse httpServerResponse = null;
        String responseBody = null;
        try 
        {
            httpServerResponse = httpClientRequest.execute(request);
            StatusLine statusLine = httpServerResponse.getStatusLine();
            if (statusLine.getStatusCode() != 200) 
            {
                responseBody = EntityUtils.toString(httpServerResponse.getEntity());
                String errorString = responseBody;
                LOGGER.error(errorString);
                throw new BonoLotoException(10, errorString);
            }
            responseBody = EntityUtils.toString(httpServerResponse.getEntity());
            System.out.println(responseBody);
        } 
        catch (IOException ioException) 
        {
            String errorString = "Error ";
            LOGGER.error(errorString, ioException);
        } 
        finally 
        {
            if (httpServerResponse != null) 
            {
                try 
                {
                    httpServerResponse.close();
                } 
                catch (IOException ioException) 
                {
                    String errorString = "Close error";
                    LOGGER.error(errorString, ioException);
                }
            }
        }
    }

    /**
     * Checks a bet on BonoLoto service.
     * @param httpClientRequest the HTTP client
     * @param date the date of the lottery
     * @param number1 the first number of the bet
     * @param number2 the second number of the bet
     * @param number3 the third number of the bet
     * @param number4 the fourth number of the bet
     * @param number5 the fifth number of the bet
     * @param number6 the sixth number of the bet
     * @param refund the refund number of the bet
     * @throws BonoLotoException if an error occurs during the request
     */
    public void checkBet(CloseableHttpClient httpClientRequest, String date, int number1, int number2,
            int number3, int number4, int number5, int number6, int refund) throws BonoLotoException 
    {
        HttpPost request = new HttpPost(this.url + "/checkBet/" + number1 + "/" + number2 + "/" + number3 + "/" + number4
                + "/" + number5 + "/" + number6 + "/" + refund);
        request.addHeader("lotteryDate", date);
        CloseableHttpResponse httpServerResponse = null;
        String responseBody = null;
        try 
        {
            httpServerResponse = httpClientRequest.execute(request);
            StatusLine statusLine = httpServerResponse.getStatusLine();
            if (statusLine.getStatusCode() != 200) 
            {
                responseBody = EntityUtils.toString(httpServerResponse.getEntity());
                String errorString = responseBody;
                LOGGER.error(errorString);
                throw new BonoLotoException(8, errorString);
            }
            responseBody = EntityUtils.toString(httpServerResponse.getEntity());
            System.out.println(responseBody);
        } 
        catch (IOException ioException) 
        {
            String errorString = "Error ";
            LOGGER.error(errorString, ioException);
        } 
        finally 
        {
            if (httpServerResponse != null) 
            {
                try 
                {
                    httpServerResponse.close();
                } 
                catch (IOException ioException) 
                {
                    String errorString = "Close error";
                    LOGGER.error(errorString, ioException);
                }
            }
        }
    }

    /**
     * Sends a CSV file to the BonoLoto service.
     * @param httpClientRequest the HTTP client
     * @param path the path of the CSV file
     * @throws BonoLotoException if an error occurs during the request
     */
    public void sendCSVFile(CloseableHttpClient httpClientRequest, String path) throws BonoLotoException 
    {
        HttpPost request = new HttpPost(this.url + "/historicalStatistics");
        HttpEntity httpEntity = MultipartEntityBuilder.create()
                .addBinaryBody("csvFile", new File(path), ContentType.MULTIPART_FORM_DATA, "file.csv").build();
        request.setEntity(httpEntity);
        CloseableHttpResponse httpServerResponse = null;
        String responseBody = null;
        try 
        {
            httpServerResponse = httpClientRequest.execute(request);
            StatusLine statusLine = httpServerResponse.getStatusLine();
            if (statusLine.getStatusCode() != 200) 
            {
                responseBody = EntityUtils.toString(httpServerResponse.getEntity());
                String errorString = responseBody;
                LOGGER.error(errorString);
                throw new BonoLotoException(11, errorString);
            }
            LOGGER.info("File sent successfully");
        } 
        catch (IOException ioException) 
        {
            String errorString = "Error ";
            LOGGER.error(errorString, ioException);
        } 
        finally 
        {
            if (httpServerResponse != null) 
            {
                try 
                {
                    httpServerResponse.close();
                } 
                catch (IOException ioException) 
                {
                    String errorString = "Close error";
                    LOGGER.error(errorString, ioException);
                }
            }
        }
    }
}
