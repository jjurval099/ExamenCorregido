package es.psp.sutil_mesa_unidad2.models;

import java.util.List;

public class LotteryResults
{
	/** Attribute - List of numbers of the bet */
	private List<Integer> numbers;
	
	/** Attribute - refundNumber of the bet */
	private int refundNumber;
	
	/** Attribute - Prize of the bet */
	private String prize;
	
	/**
	 * Default Constructor
	 */
	public LotteryResults()
	{
	}
	/**
	 * @param numbers numbers of the bet
	 * @param refundNumber refund of the bet
	 * @param prize in this bet
	 */
	public LotteryResults(List<Integer> numbers, int refundNumber, String prize)
	{
		this.numbers = numbers;
		this.refundNumber = refundNumber;
		this.prize = prize;
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
	 * @return the refundNumber
	 */
	public int getRefundNumber()
	{
		return this.refundNumber;
	}
	/**
	 * @param refundNumber the refundNumber to set
	 */
	public void setRefundNumber(int refundNumber)
	{
		this.refundNumber = refundNumber;
	}
	/**
	 * @return the prize
	 */
	public String getPrize()
	{
		return this.prize;
	}
	/**
	 * @param prize the prize to set
	 */
	public void setPrize(String prize)
	{
		this.prize = prize;
	}

	
	
}
