package es.psp.sutil_mesa_unidad2.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.psp.sutil_mesa_unidad2.exception.BonoLotoException;
import es.psp.sutil_mesa_unidad2.models.Bonoloto;
import es.psp.sutil_mesa_unidad2.models.LotteryResults;
import es.psp.sutil_mesa_unidad2.utils.Checker;
import es.psp.sutil_mesa_unidad2.utils.ParserCSV;
import jakarta.servlet.http.HttpSession;


@RequestMapping(value = "/bonoloto", produces = {"application/json"})
@RestController
public class RestHandlerUnidad2
{
	/** Attribute - Logger de la clase */
	private static final Logger LOGGER = LogManager.getLogger();
	
	@RequestMapping(method = RequestMethod.POST, value = "/historicalStatistics", consumes = {"multipart/form-data"})
	public ResponseEntity<?> uploadHistoricalStatistics(@RequestParam(value="csvFile", required=false) MultipartFile csvFile, HttpSession session)
	{
		try
		{
			// Vamos a crear una instancia de la clase ParserCSV y con ella vamos a parsear el fichero de entrada csv
			// Una vez que este parseado lo añadiremos al mapa cuya clave es la fecha del sorteo y el valor el resultado del sorteo
			Map<Date, Bonoloto> map = new ParserCSV().parse(csvFile);
			
			// En sesion vamos a guardar con clave 'historical' un valor que sera el mapa que hemos creado antes
			session.setAttribute("historical", map);
			
			// Si todo ha ido bien llegaremos a esta linea que devolvera un ok (200)
			return ResponseEntity.ok().build() ;
		}
		catch (BonoLotoException exception)
		{	
			// Si llega aqui es porque el fichero no se ha leido correctamente o  no se ha parseado la fecha correctamente
			return ResponseEntity.status(400).body(exception.getBodyExceptionMessage()) ;
		}		
		catch (Exception exception) 
		{
			// Error desconocido
			String error = "Excepcion no controlada por el servidor";
			
			LOGGER.error(error, exception);
			return ResponseEntity.status(500).body(exception.getMessage()) ;
		}
	}
	
	/**
	 *  Teniendo como referencia la información almacenada en sesión, enviamos al servidor una apuesta del usuario para ver si ha acertado
	 * @param number1 Value for the first number of the lottery
	 * @param number2 Value for the second number of the lottery
	 * @param number3 Value for the third number of the lottery
	 * @param number4 Value for the fourth number of the lottery
	 * @param number5 Value for the five number of the lottery
	 * @param number6 Value for the six number of the lottery
	 * @param refund Value for the refund of the lottery
	 * @param lotteryDate Value for the date of the lottery
	 * @param session	Value for the HttpSession of the lottery
	 * @return lotteryResult Values of the result of the lottery
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/checkBet/{number1}/{number2}/{number3}/{number4}/{number5}/{number6}/{refundNumber}"	)
	public ResponseEntity<?> checkBet(@PathVariable(value="number1", required = true) Integer number1,
			@PathVariable(value="number2", required = true) Integer number2,
			@PathVariable(value="number3", required = true) Integer number3,
			@PathVariable(value="number4", required = true) Integer number4,
			@PathVariable(value="number5", required = true) Integer number5,
			@PathVariable(value="number6", required = true) Integer number6,
			@PathVariable(value="refundNumber", required = true) Integer refund,
			@RequestParam(value="lotteryDate", required =false) String lotteryDate, 
			HttpSession session
			)
	{
		try
		{
			
			// Obtenemos de sesion el mapa cuya  clave es historical 
			Map<Date, Bonoloto> historical = (Map<Date, Bonoloto>) session.getAttribute("historical");
			
			// Creamos una instancia de la clase que validarta los parametros de entrada
			
			Checker checker = new Checker();
			
			// Comprovacion de que los numeros son los permitidos
			checker.checkNumbers(number1, number2, number3, number4, number5, number6, refund);
			
			// Comprobacion de que la fecha se ha parseado correctamente de string a date 
			checker.checkDate(lotteryDate, historical);
			
			// Inicializacion de Date
			Date date = new Date();
			if(lotteryDate != null) 
			{
				// Si lotteryDate contiene algo parsea date para convertirlo de string a Date
				date = new SimpleDateFormat("dd/MM/yyyy").parse(lotteryDate);
			}
			
			String prize = checker.checkPrize(historical.get(date),number1, number2, number3, number4, number5, number6, refund);
			
			
			return ResponseEntity.ok().body(new LotteryResults(historical.get(date).getNumbers(), historical.get(date).getRefund(), prize)) ;
		}
		catch (BonoLotoException exception)
		{
			return ResponseEntity.status(400).body(exception.getBodyExceptionMessage()) ;
		}
		catch (Exception exception) 
		{
			String error = "Excepcion no controlada por el servidor";
			
			LOGGER.error(error, exception);
			return ResponseEntity.status(500).body(exception.getMessage()) ;
		}
	}
	
	
	
	

	@RequestMapping(method = RequestMethod.GET, value = "/hotNumbers")
	public ResponseEntity<?> getHotNumberss(
			@RequestHeader(value="lotteryDate", required =false) String lotteryDate,
			@RequestHeader(value="hitsReference", required =true) Integer hitsReference,
			HttpSession session)
	{
		try
		{
			Map<Date, Bonoloto> historical = (Map<Date, Bonoloto>) session.getAttribute("historical");

			// Creamos una instancia de la clase que validarta los parametros de entrada			
			Checker checker = new Checker();
			
			this.checkReference(hitsReference);
			Date date = new Date();
			if (lotteryDate!= null)
			{
				date = checker.checkDate(lotteryDate, historical);
			}
						
			return ResponseEntity.ok().body(this.hotNumbers(hitsReference, historical, date)) ;
		}
		catch (BonoLotoException exception)
		{
			return ResponseEntity.status(400).body(exception.getBodyExceptionMessage()) ;
		}
		catch (Exception exception) 
		{
			String error = "Excepcion no controlada por el servidor";
			
			LOGGER.error(error, exception);
			return ResponseEntity.status(500).body(exception.getMessage()) ;
		}
	}
	
	// Este método maneja las solicitudes GET a la ruta "/coldNumbers".
	@RequestMapping(method = RequestMethod.GET, value = "/coldNumbers")
	public ResponseEntity<?> getColdNumberss(
	        // El valor del encabezado "lotteryDate" se asigna a la variable lotteryDate.
	        @RequestHeader(value="lotteryDate", required =false) String lotteryDate,
	        // El valor del encabezado "hitsReference" se asigna a la variable hitsReference.
	        @RequestHeader(value="hitsReference", required =true) Integer hitsReference,
	        // Se recibe la sesión HTTP como parámetro.
	        HttpSession session)
	{
	    try
	    {
	        // Se obtiene el objeto "historical" de la sesión.
	        Map<Date, Bonoloto> historical = (Map<Date, Bonoloto>) session.getAttribute("historical");

	        // Creamos una instancia de la clase Checker para validar los parámetros de entrada.
	        Checker checker = new Checker();
	        
	        // Se valida el valor de hitsReference.
	        this.checkReference(hitsReference);
	        // Se crea una nueva instancia de Date.
	        Date date = new Date();
	        // Si lotteryDate no es nulo, se valida y se asigna a date.
	        if (lotteryDate!= null)
	        {
	            date = checker.checkDate(lotteryDate, historical);
	        }
	        
	        // Se devuelve una respuesta OK con el resultado de la función coldNumbers.
	        return ResponseEntity.ok().body(this.coldNumbers(hitsReference, historical, date)) ;
	    }
	    catch (BonoLotoException exception)
	    {
	        // Si se produce una excepción de BonoLotoException, se devuelve un ResponseEntity con un código de estado 400 y el mensaje de la excepción.
	        return ResponseEntity.status(400).body(exception.getBodyExceptionMessage()) ;
	    }
	    catch (Exception exception) 
	    {
	        // Si se produce una excepción general, se registra un mensaje de error y se devuelve un ResponseEntity con un código de estado 500 y el mensaje de la excepción.
	        String error = "Excepcion no controlada por el servidor";
	        
	        LOGGER.error(error, exception);
	        return ResponseEntity.status(500).body(exception.getMessage()) ;
	    }
	}

	// Devuelve un mapa de números calientes.
	private Map<Integer, Integer> hotNumbers(int hitReference, Map<Date,Bonoloto> map, Date date)
	{
	    // Se crea un nuevo TreeMap para almacenar los resultados.
	    Map<Integer, Integer> result = new TreeMap<Integer, Integer>();
	    
	    // Se crea un nuevo TreeMap para contar la frecuencia de cada número.
	    Map<Integer, Integer> counts = new TreeMap<Integer, Integer>();
	    // Se itera sobre los valores del mapa para contar la frecuencia de los números.
	    for (Bonoloto bonoloto: map.values())
	    {
	        // Se verifica si la fecha del bonoloto es igual o posterior a la fecha especificada.
	        if (bonoloto.getDate().equals(date) || bonoloto.getDate().after(date))
	        {
	            // Se itera sobre los números del bonoloto y se cuentan.
	            for (Integer integer : bonoloto.getNumbers())
	            {
	                if (!counts.containsKey(integer))
	                {
	                    counts.put(integer, 1);
	                }
	                else 
	                {
	                    counts.put(integer, counts.get(integer)+1);
	                }
	            }
	        }
	    }
	    
	    // Se filtran los números que se repiten igual o más veces que hitReference.
	    for (Entry<Integer,Integer> entry : counts.entrySet())
	    {
	        if (entry.getValue() >= hitReference)
	        {
	            result.put(entry.getKey(), entry.getValue());
	        }
	    }
	    return result;
	}

	/**
	 * Este método devuelve un mapa de números fríos.
	 * @param hitReference La referencia de veces que un número debe repetirse para no ser considerado "frío".
	 * @param map Un mapa que contiene objetos Bonoloto asociados a fechas.
	 * @param date La fecha a partir de la cual se deben considerar los números para calcular su frecuencia.
	 * @return Un mapa que contiene los números "fríos" como clave y su frecuencia como valor.
	 */
	private Map<Integer, Integer> coldNumbers(int hitReference, Map<Date,Bonoloto> map, Date date){
	    // Se crea un nuevo TreeMap para almacenar los resultados.
	    Map<Integer, Integer> result = new TreeMap<Integer, Integer>();
	    
	    // Se crea un nuevo TreeMap para contar la frecuencia de cada número.
	    Map<Integer, Integer> counts = new TreeMap<Integer, Integer>();
	    
	    // Se itera sobre los valores del mapa para contar la frecuencia de los números.
	    for (Bonoloto bonoloto: map.values()) {
	        // Se verifica si la fecha del bonoloto es igual o posterior a la fecha especificada.
	        if (bonoloto.getDate().equals(date) || bonoloto.getDate().after(date)) {
	            // Se itera sobre los números del bonoloto y se cuentan.
	            for (Integer integer : bonoloto.getNumbers()) {
	                if (!counts.containsKey(integer)) {
	                    counts.put(integer, 1);
	                } else {
	                    counts.put(integer, counts.get(integer)+1);
	                }
	            }
	        }
	    }
	    
	    // Se filtran los números que se repiten menos veces que hitReference.
	    for (Entry<Integer,Integer> entry : counts.entrySet()) {
	        if (entry.getValue() < hitReference) {
	            result.put(entry.getKey(), entry.getValue());
	        }
	    }
	    return result;
	}


	// Valida si el valor de referencia n es negativo o cero.
	private void checkReference(int number)throws BonoLotoException 
	{
	    // Si number es menor que cero, se envia un mensaje de error y se lanza una excepción BonoLotoException.
	    if ( number < 0)
	    {
	        String error = "HitsReference is negative or zero" ;
	        
	        LOGGER.error(error);
	        throw new BonoLotoException(5, error);
	    }
	}

}
