package com;
import java.math.BigDecimal;
import java.util.*;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Random;
import java.util.Vector;
public class Gene implements Comparable<Gene>{
	private Vector<Boolean> Code;											// The code that represents each gene
	private double Objective = 0.0;											// The objective value of the gene
	private double Fitness = 0.0;											// The fitness of the gene
	private boolean UpToDate = false;										// Indicates whether the objective and fitness values or up-to-date
	
	private BigInteger NP;
	
	private static BigInteger ObjectiveMax = null;
	private static BigInteger ObjectiveMin = null;
	
	public static void init(BigInteger P)
	{
		BigDecimal Root = new BigSquareRoot().get(P);
		System.out.println(" root value :"+Root);
		ObjectiveMax = P.add(BigInteger.ONE).add(new BigDecimal("2").multiply(Root).toBigInteger());
		ObjectiveMin = P.add(BigInteger.ONE).subtract(new BigDecimal("2").multiply(Root).toBigInteger());
	}
	
	/**
	 * Converts binary representation to BigInteger. Most significant bit first.
	 * @param Bin Binary representation
	 * @return BigInteger representation
	 */
	private static BigInteger binToBigInt(Vector<Boolean> Bin)
	{
		StringBuffer SB = new StringBuffer(Bin.size());
		for (int i = 0; i < Bin.size(); i++)
			SB.append(Bin.get(i) ? '1' : '0');
		
		return new BigInteger(SB.toString(), 2);
	}
	
	/**
	 * Compare two genes by their fitness values.
	 * @param G The other gene
	 * @return -1, 0, 1 if this gene's fitness value is less than, equal to, or greater than the other's, respectively
	 */
	public int compareTo(Gene G)
	{
		return ((int)Math.signum(Fitness - G.getFitness()));
	}
	
	/**
	 * Forces update of the objective and fitness values of the gene.
	 */
	private void update()
	{
		Parameters P = decode();
		
		BigInteger N = SchoofInterface.getNP(P.A, P.B, Parameters.P);
		NP = N;
		
		if (N == null)
		{
			// System.out.println("NULL: " + P.A + " " + P.B);
			Objective = 0.0;
			Fitness = 0.0;
			UpToDate = true;
			return;
		}
		
		BigDecimal Denominator = new BigDecimal(ObjectiveMax.subtract(ObjectiveMin));
		BigDecimal Numerator = new BigDecimal(N.subtract(ObjectiveMin));
		
		Objective = Numerator.divide(Denominator, 16, RoundingMode.HALF_EVEN).doubleValue();
		Fitness = Objective;
		
		UpToDate = true;
	}
	
	public BigInteger getNP()
	{
		if (!UpToDate)
			update();
		return NP;
	}
	
	/**
	 * Gene with specified length all initialized to zeros.
	 * @param Length Required length of the gene
	 */
	public Gene(int Length)
	{
		Code = new Vector<Boolean>(Length);
		for (int i = 0; i < Length; i++)
			Code.add(false);
	}
	
	/**
	 * Randomly initialized gene of specified length.
	 * @param Length Required length of the gene
	 * @param R Random object to be used
	 */
	public Gene(int Length, Random R)
	{
		Code = new Vector<Boolean>(Length);
		for (int i = 0; i < Length; i++)
		
			Code.add(R.nextBoolean());
		/*Iterator it=Code.iterator();
		while(it.hasNext()) 
			System.out.print("boolean type :"+it.next() + " "); */
	
	}
	
	/**
	 * Construct a gene with a given code.
	 * @param Bin The code with which to construct the gene
	 */
	public Gene(Vector<Boolean> Bin)
	{
		Code = new Vector<Boolean>(Bin);
	}
	
	/**
	 * Copy constructor
	 * @param Copy The gene of which to make a copy
	 */
	public Gene(Gene Copy)
	{
		Code = Copy.getCode();
	}
	
	/**
	 * Get the code of this gene.
	 * @return Code of this gene
	 */
	public Vector<Boolean> getCode()
	{
		return new Vector<Boolean>(Code);
	}
	
	/**
	 * Decode this gene into the constituent parameters.
	 * @return Parameters object
	 */
	public Parameters decode()
	{
		BigInteger TempA = binToBigInt(new Vector<Boolean>(Code.subList(0, Code.size() / 2)));
		BigInteger TempB = binToBigInt(new Vector<Boolean>(Code.subList(Code.size() / 2, Code.size())));
		
		BigInteger Binary = new BigInteger("2").pow(Code.size() / 2).subtract(BigInteger.ONE);
		BigInteger Delta = Parameters.Max.subtract(Parameters.Min);
		
		BigInteger A = new BigDecimal(TempA).divide(new BigDecimal(Binary), 16, RoundingMode.HALF_EVEN).multiply(new BigDecimal(Delta)).toBigInteger();
		BigInteger B = new BigDecimal(TempB).divide(new BigDecimal(Binary), 16, RoundingMode.HALF_EVEN).multiply(new BigDecimal(Delta)).toBigInteger();
		
		return new Parameters(A, B);
	}
	
	/**
	 * Get the updated objective value of this gene.
	 * @return Objective value
	 */
	public double getObjective()
	{
		if (!UpToDate)
			update();
		return Objective;
	}
	
	/**
	 * Get the updated fitness value of this gene.
	 * @return Fitness value
	 */
	public double getFitness()
	{
		if (!UpToDate)
			update();
		return Fitness;
	}
	
	/**
	 * Set an updated fitness value for this gene, without affecting its UpToDate flag.
	 * @param Value The new fitness
	 */
	public void setFitness(double Value)
	{
		update();
		Fitness = Value;
	}
	
	/**
	 * Get the length of this gene.
	 * @return Gene length
	 */
	public int getLength()
	{
		return Code.size();
	}
	
	/**
	 * Get the allele value of a single specified chromosome.
	 * @param Index Location of the chromosome
	 * @return Allele
	 */
	public Boolean get(int Index)
	{
		return Code.get(Index);
	}
	
	/**
	 * Set the allele value of a single specified chromosome.
	 * @param Index Location of the chromosome
	 * @param A The allele value to set
	 * @return The previous allele value
	 */	
	public Boolean set(int Index, Boolean A)
	{
		UpToDate = false;
		return Code.set(Index, A);
	}
	
	/**
	 * Flip the specified chromosome
	 * @param Index Location of the chromosome
	 */
	public void flip(int Index)
	{
		UpToDate = false;
		Code.set(Index, !Code.get(Index));
	}
	
	/**
	 * Hashcode of the gene.
	 * @return Hashcode
	 */
	@Override
	public int hashCode()
	{
		return Code.hashCode();
	}
	
	/**
	 * Checks equality between two genes.
	 * @param O The other gene
	 * @return True if equal code, false otherwise
	 */
	@Override
	public boolean equals(Object O)
	{
		return Code.equals(O);
	}
	
	/**
	 * String representation of the code of the gene.
	 * @return String representation of the gene
	 */
	@Override
	public String toString()
	{
		StringBuffer Return = new StringBuffer();
		
		for (int i = 0; i < Code.size(); i++)
			Return.append(Code.get(i) ? '1' : '0');
		
		return Return.toString();
	}
}
