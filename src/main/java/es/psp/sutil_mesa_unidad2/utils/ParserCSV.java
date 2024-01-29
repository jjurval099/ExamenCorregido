package es.psp.sutil_mesa_unidad2.utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import es.psp.sutil_mesa_unidad2.exception.BonoLotoException;
import es.psp.sutil_mesa_unidad2.models.Bonoloto;

public class ParserCSV
{
    /** Attribute - Logger of the class */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Parses the CSV file and returns a map of Bonoloto objects.
     * 
     * @param csvFile The CSV file to parse
     * @return A map of Bonoloto objects
     * @throws BonoLotoException if an error occurs during parsing
     */
    public Map<Date, Bonoloto> parse(MultipartFile csvFile) throws BonoLotoException
    {
    	// Creamos un mapa que almacenara como clave la fechas del sorteo, y su valor el resultado del sorteo
        Map<Date, Bonoloto> map = new TreeMap<>();
        
        Scanner scanner = null;
        
        try
        {
        	// El contenido del fichero se esta almacenando dentro de un string
            String content = new String(csvFile.getBytes());
            
            // Para poder leer cada linea del fichero utilizaremos el scanner
            scanner = new Scanner(content);
            
            // Nos saltamos la primera linea porque es la cabecera del documento
            scanner.nextLine();

            // Iteraremos las lineas del fichero hasta llegar al final del fichero
            while (scanner.hasNext())
            {
            	// Leemos una linea y separamos por coma uitlizando el metodo split
                String[] line = scanner.nextLine().split(",");

                // Vamos a convertir una fecha en formato string a Date
                Date date = new SimpleDateFormat("dd/MM/yyyy").parse(line[0]);

                // Creamos una lista donde almacenaremos los 6 numeros del sorteo
                List<Integer> numbers = new ArrayList<>();
                
                // Empezamos en el indice 1 para evitar coger las fechas y  terminamos en el indice 6 para evitar coger el complementario y el reintegro
                for (int i = 1; i < line.length - 2; i++)
                {
                	// Añadimos el numero en formato integer a la lista
                    numbers.add(Integer.valueOf(line[i]));
                }

                // Creamos un numero entero que almacenara el complementario. Lo cogemos del indice 7
                int complement = Integer.valueOf(line[7]);
                
                // Como en sorteos antiguos no hay reintegros hay que tener cuidado a la hora de obtenerlos 
                int refund = -1;
                
                // Solo aquellos sorteos que tienen 9 campos en cada fila son los que tienen el reintegro
                if (line.length == 9)
                {
                	// En caso de tener 9 campos se añadira el reintegro
                    refund = Integer.valueOf(line[8]);
                }
                
                // Si todo ha ido bien se guardara la clave date y el valor date, numbers, complement, refund
                map.put(date, new Bonoloto(date, numbers, complement, refund));
            }
            return map;

        }        
        // Dara error si no se puede leer bien el archivo csv
        catch (IOException ioException)
        {
            String message = "Error reading the file";
            LOGGER.error(message, ioException);
            throw new BonoLotoException(1, message, ioException);
        }
        // Dara error si no se ha leido correctamente la fecha
        catch (ParseException parseException)
        {
            String message = "Error reading a date";
            LOGGER.error(message, parseException);
            throw new BonoLotoException(2, message, parseException);
        }
        finally
        {
            if (scanner != null)
            {
            	// Si el escanner se ha abierto previamente habra que cerrarlo
                scanner.close();
            }
        }
    }
}
