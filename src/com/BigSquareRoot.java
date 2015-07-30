package com;
import java.math.BigDecimal;
import java.math.BigInteger;
public class BigSquareRoot {
	public static final int DEFAULT_MAX_ITERATIONS = 64;
	public static final int DEFAULT_SCALE = 16;
	
	private static BigDecimal ZERO = new BigDecimal("0");
	private static BigDecimal ONE = new BigDecimal("1");
	private static BigDecimal TWO = new BigDecimal("2");
	private BigDecimal error;
	private int iterations;
	private boolean traceFlag;
	private int scale = DEFAULT_SCALE;
	private int maxIterations = DEFAULT_MAX_ITERATIONS;
	
	public BigDecimal getError()
	{
		return error;
	}
	
	public int getIterations()
	{
		return iterations;
	}
	
	public boolean getTraceFlag()
	{
		return traceFlag;
	}
	
	public void setTraceFlag(boolean flag)
	{
		traceFlag = flag;
	}
	
	public int getScale()
	{
		return scale;
	}
	
	public void setScale(int scale)
	{
		this.scale = scale;
	}
	
	public int getMaxIterations()
	{
		return maxIterations;
	}
	
	public void setMaxIterations(int maxIterations)
	{
		this.maxIterations = maxIterations;
	}
	
	private  BigDecimal getInitialApproximation(BigDecimal n)
	{
		BigInteger integerPart = n.toBigInteger();
		int length = integerPart.toString().length();
		if ((length % 2) == 0)
			length--;
		length /= 2;
		BigDecimal guess = ONE.movePointRight(length);
		//System.out.println("guess"+guess);
		
		return guess;
	}
	
	public  BigDecimal get(BigInteger n)
	{
		return get(new BigDecimal(n));
	}
	
	public  BigDecimal get(BigDecimal n)
	{
		BigDecimal initialGuess = getInitialApproximation(n);
		trace("Initial guess " + initialGuess.toString());
		BigDecimal lastGuess = ZERO;
		BigDecimal guess = new BigDecimal(initialGuess.toString());
		
		iterations = 0;
		boolean more = true;
		while (more)
		{
			lastGuess = guess;
			guess = n.divide(guess, scale, BigDecimal.ROUND_HALF_UP);
			guess = guess.add(lastGuess);
			guess = guess.divide(TWO, scale, BigDecimal.ROUND_HALF_UP);
			trace("Next guess " + guess.toString());
			error = n.subtract(guess.multiply(guess));
			if (++iterations >= maxIterations)
				more = false;
			else if (lastGuess.equals(guess))
				more = error.abs().compareTo(ONE) >= 0;
		}
		
		return guess;
	}
	
	private void trace(String s)
	{
		if (traceFlag)
			System.out.println(s);
	}
	
	public static BigInteger getRandomBigInteger(int nDigits)
	{
		StringBuffer sb = new StringBuffer();
		java.util.Random r = new java.util.Random();
		for (int i = 0; i < nDigits; i++)
			sb.append(r.nextInt(10));
		return new BigInteger(sb.toString());
	}
}
