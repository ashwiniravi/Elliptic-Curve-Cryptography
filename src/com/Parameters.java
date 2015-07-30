package com;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Vector;

public class Parameters {
	public BigInteger A;
	public BigInteger B;
	public static BigInteger P = null;
	
	// Bounds of the parameters
	public static BigInteger Max = null;
	public static BigInteger Min = null;
	
	private static BigDecimal Denom = null;
	
	public static void init(BigInteger Prime)
	{
		P = Prime;
		Max = P.subtract(BigInteger.ONE);
		Min = BigInteger.ZERO;
		Denom = new BigDecimal(Max).subtract(new BigDecimal(Min));
	}
	
	/**
	 * Convert a BigInteger into its binary representation. Most significant bit first.
	 * @param Int Integer to convert
	 * @param Length Length of the binary representation to create
	 * @return The binary representation
	 */
	private static Vector<Boolean> bigIntToBin(BigInteger Int, int Length)
	{
		Vector<Boolean> Val = new Vector<Boolean>();
		
		int Extra = Length - Int.bitLength();
		
		for (int i = 0; i < Extra; i++)
			Val.add(false);
		for (int i = Int.bitLength() - 1; i >= 0; i++)
			Val.add(Int.testBit(i));
		
		return Val;
	}
	
	/**
	 * Standard constructor
	 * @param a Parameter A
	 * @param b Parameter B
	 */
	public Parameters(BigInteger a, BigInteger b)
	{
		A = a;
		B = b;
	}
	
	/**
	 * Encode the parameters into a gene.
	 * @param Length Length of the gene code
	 * @return An encoded gene
	 */
	public Gene encode(int Length)
	{
		BigInteger Binary = new BigInteger("2").pow(Length / 2).subtract(BigInteger.ONE);
		BigDecimal TempA = new BigDecimal(A).divide(Denom, 16, RoundingMode.HALF_EVEN).multiply(new BigDecimal(Binary));
		BigInteger EncodedA = TempA.toBigInteger();		
		BigDecimal TempB = new BigDecimal(B).divide(Denom, 16, RoundingMode.HALF_EVEN).multiply(new BigDecimal(Binary));
		BigInteger EncodedB = TempB.toBigInteger();
		
		Vector<Boolean> Code = bigIntToBin(EncodedA, Length / 2);
		Code.addAll(bigIntToBin(EncodedB, Length / 2));
		
		Gene G = new Gene(Code);
		
		return G;
	}
	
	/**
	 * String representation of the parameters.
	 * @return String
	 */
	@Override
	public String toString()
	{
		return "A: " + A + " B: " + B;
	}

}
