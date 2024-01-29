package es.psp.sutil_mesa_unidad2.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Bonoloto
{
	/** Attribute - Date of the bet */
	private Date date;
	
	/** Attribute - Numbers of the bet */
	private List<Integer> numbers;
	
	/** Attribute - Complement of the bet */
	private int complement;
	
	/** Attribute - Refund of the bet */
	private int refund;
	
	/**
	 * Default Constructor
	 */
	public Bonoloto()
	{
		this.date = new Date();
		this.numbers = new ArrayList<Integer>();
		
	}

	/**
	 * @param date the date of the bet
	 * @param numbers the numbers in this bet
	 * @param complement the complement in this bet
	 * @param refund the refund in this bet
	 */
	public Bonoloto(Date date, List<Integer> numbers, int complement, int refund)
	{
		this.date = date;
		this.numbers = numbers;
		this.complement = complement;
		this.refund = refund;
	}

	/**
	 * @return the date
	 */
	public Date getDate()
	{
		return this.date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date)
	{
		this.date = date;
	}

	/**
	 * @return the numbers
	 */
	public List<Integer> getNumbers()
	{
		return this.numbers;
	}

	/**
	 * @param numbers the numbers to set
	 */
	public void setNumbers(List<Integer> numbers)
	{
		this.numbers = numbers;
	}

	/**
	 * @return the complement
	 */
	public int getComplement()
	{
		return this.complement;
	}

	/**
	 * @param complement the complement to set
	 */
	public void setComplement(int complement)
	{
		this.complement = complement;
	}

	/**
	 * @return the refund
	 */
	public int getRefund()
	{
		return this.refund;
	}

	/**
	 * @param refund the refund to set
	 */
	public void setRefund(int refund)
	{
		this.refund = refund;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Bonoloto [date=");
		builder.append(this.date);
		builder.append(", numbers=");
		builder.append(this.numbers);
		builder.append(", complement=");
		builder.append(this.complement);
		builder.append(", refund=");
		builder.append(this.refund);
		builder.append("]");
		return builder.toString();
	}
	
	
}
