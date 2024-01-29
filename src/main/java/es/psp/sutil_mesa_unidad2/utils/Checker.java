package es.psp.sutil_mesa_unidad2.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.psp.sutil_mesa_unidad2.exception.BonoLotoException;
import es.psp.sutil_mesa_unidad2.models.Bonoloto;

/**
 * Esta clase se encargara de hacer de las validaciones 
 */
public class Checker
{
	
	/** Attribute - Logger de la clase */
	private static final Logger LOGGER = LogManager.getLogger();
	
	public void checkNumbers(int number1,int number2,int number3,int number4,int number5,int number6, int refund) throws BonoLotoException
	{
		// Primero, vamos a comprobar que el rango de los números que nos envió el usuario están dentro de los permitidos
		
		// Comprobamos el primero número
		this.checkRangeBetween1and49(number1);
		// Comprobamos el segundo número
		this.checkRangeBetween1and49(number2);
		// Comprobamos el tercer número
		this.checkRangeBetween1and49(number3);
		// Comprobamos el cuarto número
		this.checkRangeBetween1and49(number4);
		// Comprobamos el quinto número
		this.checkRangeBetween1and49(number5);
		// Comprobamos el sexto número
		this.checkRangeBetween1and49(number6);
		
		// Comprobamos el reintegro
		this.checkRangeBetween0and9(refund);
		
		// Vamos a comprobar que no existen duplicados entre los números del boleto
		this.checkDuplicated(number1, number2, number3, number4, number5, number6);
	}

	private void checkDuplicated(int number1, int number2, int number3, int number4, int number5, int number6) throws BonoLotoException
	{
		// Vamos a comprobar que el número 1 no coincide con el resto. Si coincide es que el usuario se equivocó enviando los números
		if(number1 == number2 || number1 == number3 || number1 == number4 || number1 == number5 || number1 == number6) 
		{
			String error = "Duplicated numbers: " + number1 ;
			LOGGER.error(error);
			throw new BonoLotoException(3, error);
		}
		// Vamos a comprobar que el número 2 no coincide con el resto
		else if (number2 == number3 || number2 == number4 || number2 == number5 || number2 == number6)
		{
			String error = "Duplicated numbers: " + number2 ;
			LOGGER.error(error);
			throw new BonoLotoException(3, error);			
		}
		// Vamos a comprobar que el número 3 no coincide con el resto
		else if (number3 == number4 || number3 == number5 || number3 == number6)
		{
			String error = "Duplicated numbers: " + number3 ;
			LOGGER.error(error);
			throw new BonoLotoException(3, error);			
		}
		// Vamos a comprobar que el número 4 no coincide con el resto
		else if (number4 == number5 || number4 == number6)
		{
			String error = "Duplicated numbers: " + number4 ;
			LOGGER.error(error);
			throw new BonoLotoException(3, error);			
		}
		// Vamos a comprobar que el número 5 no coincide con el resto
		else if (number5 == number6)
		{
			String error = "Duplicated numbers: " + number5 ;
			LOGGER.error(error);
			throw new BonoLotoException(3, error);			
		}
	}

	private void checkRangeBetween1and49(int number) throws BonoLotoException
	{
		// Comprobamos que si el numero no esta dentro de los permitidos lance un error
		if (number < 1 || number > 49)
		{
			String error ="Incorrect range number for lottery: " + number ;
			
			LOGGER.error(error);
			throw new BonoLotoException(2, error);
		}
	}
		
	private void checkRangeBetween0and9(int refund) throws BonoLotoException
	{
		// Comprobamos que si el reintegro no esta dentro de los permitidos lance un error
		if (refund <= -1 || refund >= 10)
		{
			String error = "Invalid refund: " + refund ;
			
			LOGGER.error(error);
			throw new BonoLotoException(3, error);
		}
	}
	
	/**
	 * 
	 * @param lotteryDate fecha del sorteo
	 * @param map fecha del sorteo y los numeros de la bonoloto
	 * @return fecha del sorteo
	 * @throws BonoLotoException
	 */
	public Date checkDate(String lotteryDate, Map<Date,Bonoloto> map) throws BonoLotoException 
	{
		
		try
		{
			// Parsear la fecha de la loteria de string a Date
			Date date = new SimpleDateFormat("dd/MM/yyyy").parse(lotteryDate);
			
			// Vamos a comprobar que en el mapa contiene una clave con el valor de date
			if (!map.containsKey(date))
			{
				// Si el mapa no contiene la clave date dara el error de no haber encontrado la fecha del sorteo 
				String error = "Lottery date was not found: " + lotteryDate ;
				
				LOGGER.error(error);
				throw new BonoLotoException(6, error);
			}
			
			// Devolvera la fecha del sorteo 
			return date;
		}
		catch (ParseException parseException)
		{			
			// Saltara el error si no se ha podido parsear correctamente la fecha  de string a date
			String error = "Error al parsear el csv " ;
			
			LOGGER.error(error, parseException);
			throw new BonoLotoException(1, error);
		}
	}
	
	/**
	 * Method to check the price of the lottery introduced
	 * @param bonoloto	Variable of the bonoloto numbers
	 * @param number1	Value of the numbers
	 * @param number2
	 * @param number3
	 * @param number4
	 * @param number5
	 * @param number6
	 * @param refund
	 * @return
	 */
	public String checkPrize(Bonoloto bonoloto, int number1,int number2,int number3,int number4,int number5,int number6,int refund) 
	{
		
		int cont = 0;
		
		// Comprobara que el numero de la  bonoloto sea la misma que el number1
		if (bonoloto.getNumbers().contains(number1))
		{
			cont ++;
		}
		// Comprobara que el numero de la  bonoloto sea la misma que el number2
		if (bonoloto.getNumbers().contains(number2))
		{
			cont ++;
		}
		// Comprobara que el numero de la  bonoloto sea la misma que el number3
		if (bonoloto.getNumbers().contains(number3))
		{
			cont ++;
		}
		// Comprobara que el numero de la  bonoloto sea la misma que el number4
		if (bonoloto.getNumbers().contains(number4))
		{
			cont ++;
		}// Comprobara que el numero de la  bonoloto sea la misma que el number5
		if (bonoloto.getNumbers().contains(number5))
		{
			cont ++;
		}
		// Comprobara que el numero de la  bonoloto sea la misma que el number6
		if (bonoloto.getNumbers().contains(number6))
		{
			cont ++;
		}
		
		// Devolvera el numero de aciertos con la bonoloto, el number1, el number2, el number3, el numbe4, el number5 y el number6 
		return this.selectPrize(cont, bonoloto, number1, number2, number3, number4, number5, number6, refund);
		
	}
	
	public String selectPrize(int cont, Bonoloto bonoloto, int number1,int number2,int number3,int number4,int number5,int number6,int refund) 
	{
		String response ="";
		
		// Dependiendo de los aciertos que contenga la bonoloto entrara en un case diferente
		switch (cont)
		{
			case 6:
			{
				response = "1a Categoria";
				break;
			}
			case 5:
			{
				if (this.checkComplement(bonoloto, number1, number2, number3, number4, number5, number6))
				{
					response = "2a Categoria";
				}
				else 
				{
					response = "3a Categoria";
				}
				break;
			}
			case 4:
			{
				response = "4a Categoria";
				break;
			}
			case 3:
			{
				response = "5a Categoria";
				break;
			}
			default:
			{
				// Si no entra en ninguna categoria mirara si el refund es igual que la de la bonoloto
				if(refund == bonoloto.getRefund()) 
				{
					response = "Refund";
				}
				else 
				{
					response = "No Prize";
				}
				break;
			}	
		}
		
		// Respondera con una categoria o refund o No Prize.
		return response;
	}
	
	private boolean checkComplement(Bonoloto bonoloto, int number1,int number2,int number3,int number4,int number5,int number6)
	{		
		if (bonoloto.getNumbers().contains(bonoloto.getComplement()))
		{
			// Devuelve false si  los numeros de la bonoloto tiene el complementario 
			return false;
		}
		else 
		{ 
			// Devuelve el complementario si coincide el complementario  con algun numero de la bonoloto 
			return (bonoloto.getComplement() == number1) || (bonoloto.getComplement() == number2) || (bonoloto.getComplement() == number3) || (bonoloto.getComplement() == number4) || (bonoloto.getComplement() == number5) || (bonoloto.getComplement() == number6);
		}
	}

}
