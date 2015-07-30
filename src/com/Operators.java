package com;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Random;
class Statistics
{
	public static int NumCrossCalls;
	public static int NumCross;
	public static int NumMutCalls;
	public static int NumMut;
	public static int NumEltCalls;
	public static int NumElt;
}
public class Operators {
	public static double PC = 0.9;											// Probability of crossover per gene
	public static double PM = 0.2;											// Probability of mutation per bit
	public static double SF = 2.0;											// Scaling factor
	public static double E = 0.1;											// Percentage of elite genes retained
	public static Random R = new Random();									// Random object
	
	public static boolean DebugMode = false;								// Global debug flag
	public static PrintStream Out = System.out;								// PrintStream used for all outputs
	
	/**
	 * Dump statistics of the various operators.
	 */
	public static void dumpStats()
	{
		Out.print("Number of crossovers: " + Statistics.NumCross + "/" + Statistics.NumCrossCalls);
		if (Statistics.NumCrossCalls != 0)
			Out.println(" (" + (double)Statistics.NumCross / Statistics.NumCrossCalls + ")");
		else
			Out.println(" (0.0)");
		Out.print("Number of mutations: " + Statistics.NumMut + "/" + Statistics.NumMutCalls);
		if (Statistics.NumMutCalls != 0)
			Out.println(" (" + (double)Statistics.NumMut / Statistics.NumMutCalls + ")");
		else
			Out.println( "(0.0)");
		Out.print("Number of elitisms: " + Statistics.NumElt + "/" + Statistics.NumEltCalls);
		if (Statistics.NumEltCalls != 0)
			Out.println(" (" + (double)Statistics.NumElt / Statistics.NumEltCalls + ")");
		else
			Out.println( "(0.0)");
	}
	
	/**
	 * Biased coin flip. Increase bias to increase probability of true occurring.
	 * @param Bias Probability of true occurring
	 * @return True or false
	 */
	private static boolean coinFlip(double Bias)
	{
		if (R.nextDouble() < Bias)
			return true;
		return false;
	}
	
	/**
	 * Roulette wheel selection. Runs in O(log(N)) time.
	 * @param Pop Population
	 * @param CumSum Cumulative sums of the fitness values
	 * @return The gene selected
	 */
	private static Gene rouletteSelection(Population Pop, double CumSum[])
	{
		double Select = R.nextDouble() * Pop.getSumFitness();
		int i = Arrays.binarySearch(CumSum, Select) * -1 - 1;
		if (i == CumSum.length)
			i--;
		
		return Pop.get(i);
	}
	
	/*
	private static int[] stochasticSelection(Population Pop)
	{
		int Selections[] = new int[Pop.getPopSize()];
		int Ind = 0;
		double Fitness[] = new double[Pop.getPopSize()];
		
		for (int i = 0; i < Pop.getPopSize() && Ind < Pop.getPopSize(); i++)
		{
			Fitness[i] = Pop.get(i).getFitness() / Pop.getSumFitness() * Pop.getPopSize();
			int Count = (int)Math.floor(Fitness[i]);
			
			for (int j = 0; j < Count && Ind < Pop.getPopSize(); j++)
				Selections[Ind++] = i;
			Fitness[i] -= Count;
		}
		
		int j = 0;
		while (Ind < Pop.getPopSize())
		{
			if (coinFlip(Fitness[j]))
				Selections[Ind++] = j;
			j = (j + 1) % Pop.getPopSize();
		}
		
		return Selections;
	}
	*/
	
	/*public static void crossover(Gene ParentA, Gene ParentB, Gene ChildA, Gene ChildB)
	{
	int BestSite = 0;
	double BestFitness = 0;
	
	for (int XSite = 0; XSite < ParentA.getLength() - 1; XSite++)
	{
	for (int i = 0; i <= XSite; i++)
	{
	ChildA.set(i, ParentA.get(i));
	ChildB.set(i, ParentB.get(i));
	}
	for (int i = XSite + 1; i < ParentA.getLength(); i++)
	{
	ChildA.set(i, ParentB.get(i));
	ChildB.set(i, ParentA.get(i));
	}
	
	double Fitness = Math.max(ChildA.getFitness(), ChildB.getFitness());
	if (Fitness > BestFitness)
	{
	BestFitness = Fitness;
	BestSite = XSite;
	}
	}
	
	for (int i = 0; i <= BestSite; i++)
	{
	ChildA.set(i, ParentA.get(i));
	ChildB.set(i, ParentB.get(i));
	}
	for (int i = BestSite + 1; i < ParentA.getLength(); i++)
	{
	ChildA.set(i, ParentB.get(i));
	ChildB.set(i, ParentA.get(i));
	}
	}*/
	
	/**
	 * Perform crossover.
	 * @param ParentA The first parent
	 * @param ParentB The second parent
	 * @param ChildA The first child
	 * @param ChildB The second child
	 */
	public static void crossover(Gene ParentA, Gene ParentB, Gene ChildA, Gene ChildB)
	{
		boolean Debug = false;

		Statistics.NumCrossCalls++;
		if (!coinFlip(PC))
		{
			return;
		}
		Statistics.NumCross++;

		int XSite = R.nextInt(ParentA.getLength() - 1);
		for (int i = 0; i <= XSite; i++)
		{
			ChildA.set(i, ParentA.get(i));
			ChildB.set(i, ParentB.get(i));
		}
		for (int i = XSite + 1; i < ParentA.getLength(); i++)
		{
			ChildA.set(i, ParentB.get(i));
			ChildB.set(i, ParentA.get(i));
		}

		if (Debug || DebugMode)
		{
			Out.println(ParentA + " + " + ParentB + " -(" + XSite + ")-> " + ChildA + " + " + ChildB);
		}
	}
	
	/**
	 * Perform mutation.
	 * @param G The gene to mutate
	 */
	public static void mutate(Gene G)
	{
		boolean Debug = false;
		
		Statistics.NumMutCalls += G.getLength();
		
		if (Debug || DebugMode)
			Out.print(G + " -> ");
		for (int i = 0; i < G.getLength(); i++)
		{
			if (coinFlip(PM))
			{
				G.flip(i);
				Statistics.NumMut++;
			}
		}
		
		if (Debug || DebugMode)
			Out.println(G);
	}
	
	/**
	 * Perform elitism.
	 * @param Old The old population from which to retain the fittest genes
	 * @param New The new population in which the weakest genes have to be replaced
	 */
	public static void elitism(Population Old, Population New)
	{
		boolean Debug = false;
		
		Statistics.NumEltCalls++;
		
		int N = (int)Math.round(E * Old.getPopSize());
		Old.sort();
		New.sort();
		
		int Count = 0;
		for (int i = 0; i < N; i++)
		{
			Gene OldGene = Old.get(Old.getPopSize() - 1 - i);
			if (New.binarySearch(OldGene) < 0)
			{
				if (New.get(Count).getFitness() < OldGene.getFitness())
					New.set(Count++, OldGene);
				else
					break;
			}
		}
		
		Statistics.NumElt += Count;
	}
	
	/*
	public static int select(int Selections[], int N)
	{
		int Index = R.nextInt(N);
		
		Selections[Index] = Selections[N - 1];
		
		return Index;
	}
	*/
	
	/**
	 * Select a gene from the population using some technique.
	 * @param Pop Population
	 * @param CumSum Cumulative sums of the fitness values
	 * @return The selected gene
	 */
	public static Gene select(Population Pop, double CumSum[])
	{
		return rouletteSelection(Pop, CumSum);
	}
	
	/*
	public static Population reproduce(Population Pop)
	{
		Population NewPop = new Population(Pop.getPopSize(), Pop.getGeneLength());
		
		int Selections[] = stochasticSelection(Pop);
		
		for (int i = 0; i < NewPop.getPopSize(); i++)
			NewPop.set(i, Pop.get(select(Selections, Pop.getPopSize() - i)));
		
		return NewPop;
	}
	*/
	
	/**
	 * Perform reproduction on the population.
	 * @param Pop Population
	 * @param CumSum Cumulative sums of the fitness values
	 * @return The new population
	 */
	public static Population reproduce(Population Pop, double CumSum[])
	{
		Population NewPop = new Population(Pop.getPopSize(), Pop.getGeneLength());
		
		for (int i = 0; i < NewPop.getPopSize(); i++)
			NewPop.set(i, select(Pop, CumSum));
		
		return NewPop;
	}
	
	/**
	 * Scale the fitness values of the population.
	 * @param Pop Population
	 */
	public static void scale(Population Pop)
	{
		boolean Debug = false;
		
		double FAvg = Pop.getSumFitness() / Pop.getPopSize();
		double FMin = Pop.getWeakest().getFitness();
		double FMax = Pop.getFittest().getFitness();
		
		double A = 0.0;
		double B = 0.0;
		
		if (FMin < ((SF * FAvg - FMax) / (SF - 1.0)))
		{
			A = FAvg / (FAvg - FMin);
			B = FAvg * FMin / (FMin - FAvg);
		}
		else
		{
			A = FAvg * (SF - 1.0) / (FMax - FAvg);
			B = FAvg * (FMax - SF * FAvg) / (FMax - FAvg);
		}
		
		if (Double.isInfinite(A) || Double.isInfinite(B) || Double.isNaN(A) || Double.isNaN(B))
			return;
		
		for (int i = 0; i < Pop.getPopSize(); i++)
		{
			Gene G = Pop.get(i);
			G.setFitness(G.getFitness() * A + B);
			Pop.set(i, G);
		}
		
		if (Debug || DebugMode)
		{
			System.out.println("FMin: " + FMin + "\tFAvg: " + FAvg + "\tFMax: " + FMax);
			System.out.println("A: " + A + "\tB: " + B);
			System.out.println("F'Min: " + (A*FMin + B) + "\tF'Avg: " + (A*FAvg + B) + "\tF'Max: " + (A*FMax + B));
		}
	}
}
