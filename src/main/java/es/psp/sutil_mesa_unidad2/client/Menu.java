package es.psp.sutil_mesa_unidad2.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.http.impl.client.CloseableHttpClient;

import es.psp.sutil_mesa_unidad2.exception.BonoLotoException;

public class Menu 
{

    /**
     * Displays the menu options.
     */
    public void getMenu() 
    {
        System.out.println("------------------Options------------------");
        System.out.println("1.- Send csv file");
        System.out.println("2.- Check bet");
        System.out.println("3.- Check hot numbers");
        System.out.println("4.- Check cold numbers");
        System.out.print("-> ");
    }

    /**
     * Selects an option based on user input.
     * @param option the selected option
     * @param scanner the scanner for user input
     * @param httpClientRequest the HTTP client
     * @throws BonoLotoException if an error occurs during the request
     */
    public void selectOption(int option, Scanner scanner, CloseableHttpClient httpClientRequest) throws BonoLotoException 
    {
        switch (option) 
        {
            case 1 -> 
            {
                this.sendFile(scanner, httpClientRequest);
            }
            case 2 -> 
            {
                this.checkBet(scanner, httpClientRequest);
            }
            //case 3 -> this.hotNumbers(scanner, httpClientRequest);
            //case 4 -> this.coldNumbers(scanner, httpClientRequest);
            default -> 
            {
                System.out.println("Incorrect option");
            }
        }
    }

    /**
     * Allows user to input numbers for checking a bet.
     * @param scanner the scanner for user input
     * @param httpClientRequest the HTTP client
     * @throws BonoLotoException if an error occurs during the request
     */
    private void checkBet(Scanner scanner, CloseableHttpClient httpClientRequest) throws BonoLotoException 
    {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < 6; i++) 
        {
            System.out.print("Introduce number: ");
            list.add(scanner.nextInt());
        }
        ClientPetitions petitions = new ClientPetitions("http://localhost:8080/bonoloto");
        petitions.checkBet(httpClientRequest, null, 0, 0, 0, 0, 0, 0, 0);
    }

    /**
     * Allows user to input the file path for sending a CSV file.
     * @param scanner the scanner for user input
     * @param httpClientRequest the HTTP client
     * @throws BonoLotoException if an error occurs during the request
     */
    private void sendFile(Scanner scanner, CloseableHttpClient httpClientRequest) throws BonoLotoException 
    {
        System.out.print("Introduce the path where is the file to send: ");
        String path = scanner.nextLine();
        ClientPetitions petitions = new ClientPetitions("http://localhost:8080/bonoloto");
        petitions.sendCSVFile(httpClientRequest, path);
    }
}
