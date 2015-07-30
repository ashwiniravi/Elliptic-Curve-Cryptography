package com;
import java.io.PrintStream;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;
public class Population {
	private Vector<Gene> Pop;												// Collection of genes
	private double SumFitness;												// Sum of the fitness values of each gene in the population
	private double SumObjective;											// Sum of the objective values of each gene in the population
	
	/**
	 * Randomly generate a population.
	 * @param PopSize Size of the population to generate
	 * @param GeneLength Length of the genes
	 * @param R Random object to use
	 */
	public Population(int PopSize, int GeneLength, Random R)
	{
		Pop = new Vector<Gene>(PopSize);
		
		for (int i = 0; i < PopSize; i++)
		{
			Gene G = new Gene(GeneLength, R);
			SumObjective += G.getObjective();
			SumFitness += G.getFitness();			
			Pop.add(G);
		}
	}
	
	/**
	 * Generate a population initialized to zeros.
	 * @param PopSize Size of the population to generate
	 * @param GeneLength Length of the genes
	 */
	public Population(int PopSize, int GeneLength)
	{
		Pop = new Vector<Gene>(PopSize);
		
		for (int i = 0; i < PopSize; i++)
		{
			Gene G = new Gene(GeneLength);
			SumObjective += G.getObjective();
			SumFitness += G.getFitness();			
			Pop.add(G);
		}
	}
	
	/**
	 * Copy constructor.
	 * @param Old Copy this population
	 */
	public Population(Population Old)
	{
		Pop = new Vector<Gene>(Old.getPopSize());
		
		for (int i = 0; i < Old.getPopSize(); i++)
		{
			Gene G = Old.get(i);
			SumObjective += G.getObjective();
			SumFitness += G.getFitness();
			Pop.add(G);
		}
	}
	
	/**
	 * Dumps the entire population.
	 * @return String representation of the population
	 */
	@Override
	public String toString()
	{
		StringBuffer Return = new StringBuffer(Pop.size() * Pop.firstElement().getLength() + Pop.size());
		
		for (int i = 0; i < Pop.size() - 1; i++)
		{
			Return.append(Pop.get(i));
			Return.append('\n');
		}
		Return.append(Pop.lastElement());
		
		return Return.toString();
	}
	
	/**
	 * Get a specific gene in the population.
	 * @param Index Index into the population
	 * @return The specified gene
	 */
	public Gene get(int Index)
	{
		return new Gene(Pop.get(Index));
	}
	
	/**
	 * Set a specific gene in the population.
	 * @param Index Index into the population
	 * @param G Set the gene at Index to this
	 * @return The old gene that was at position Index
	 */
	public Gene set(int Index, Gene G)
	{
		Gene Old = Pop.set(Index, G);
		SumObjective -= Old.getObjective();
		SumFitness -= Old.getFitness();
		SumObjective += G.getObjective();
		SumFitness += G.getFitness();
		return Old;
	}
	
	/**
	 * Get the sum of the fitness values.
	 * @return Sum of the fitness values of the entire population
	 */
	public double getSumFitness()
	{
		return SumFitness;
	}
	
	/**
	 * Get the sum of the objective values.
	 * @return Sum of the objective values of the entire population
	 */
	public double getSumObjective()
	{
		return SumObjective;
	}
	
	/**
	 * Get the fittest gene in the population.
	 * @return The fittest gene
	 */
	public Gene getFittest()
	{
		Gene Fittest = Pop.firstElement();
		double Fitness = Fittest.getFitness();
		Gene G = null;
		
		for (int i = 1; i < Pop.size(); i++)
		{
			G = Pop.get(i);
			if (G.getFitness() > Fitness)
			{
				Fittest = G;
				Fitness = Fittest.getFitness();
			}
		}
		
		return new Gene(Fittest);
	}
	
	/**
	 * Get the weakest gene in the population.
	 * @return The weakest gene
	 */
	public Gene getWeakest()
	{
		Gene Weakest = Pop.firstElement();
		double Fitness = Weakest.getFitness();
		Gene G = null;
		
		for (int i = 1; i < Pop.size(); i++)
		{
			G = Pop.get(i);
			if (G.getFitness() < Fitness)
			{
				Weakest = G;
				Fitness = Weakest.getFitness();
			}
		}
		
		return new Gene(Weakest);
	}
	
	/**
	 * Get the population size.
	 * @return Population size
	 */
	public int getPopSize()
	{
		return Pop.size();
	}
	
	/**
	 * Get the length of the genes.
	 * @return Length of the genes
	 */
	public int getGeneLength()
	{
		return Pop.firstElement().getLength();
	}
	
	/**
	 * Make this population a copy of the specified population.
	 * @param NewPop The population of which this population has to be a copy
	 */
	public void copy(Population NewPop)
	{
		SumFitness = 0.0;
		SumObjective = 0.0;
		for (int i = 0; i < NewPop.getPopSize(); i++)
		{
			Pop.set(i, NewPop.get(i));
			SumObjective += Pop.get(i).getObjective();
			SumFitness += Pop.get(i).getFitness();
		}
	}
	
	/**
	 * Sort this population by fitness values in ascending order.
	 */
	public void sort()
	{
		Collections.sort(Pop);
	}
	
	/**
	 * Perform a binary search on this population for the specified gene. Assumes that the population has already been sorted.
	 * @param G The gene to search for
	 * @return The index in the population at which the specified gene is located
	 */
	public int binarySearch(Gene G)
	{
		return Collections.binarySearch(Pop, G);
	}
	
	/**
	 * Dump the entire population info to the specified stream.
	 * @param Out PrintStream to which the output has to be dumped
	 */
	public void dumpInfo(PrintStream Out)
	{
		double BestFitness = 0.0;
		double WorstFitness = SumFitness;
		double BestObjective = 0.0;
		double WorstObjective = SumObjective;
		
		Out.println("---------------------");
		Out.println("Population Info Dump:");
		Out.println("---------------------");
		Out.println("Population Size = " + Pop.size());
		Out.println("Gene Length = " + Pop.firstElement().getLength());
		for (int i = 0; i < Pop.size(); i++)
		{
			Gene G = Pop.get(i);
			double Fitness = G.getFitness();
			double Objective = G.getObjective();
			BestFitness = Math.max(BestFitness, Fitness);
			WorstFitness = Math.min(WorstFitness, Fitness);
			BestObjective = Math.max(BestObjective, Objective);
			WorstObjective = Math.min(WorstObjective, Objective);
			Out.println(i + 1 + ". " + G + " - " + Objective + " - " + Fitness);
		}
		Out.println("Sum of objective = " + SumObjective);
		Out.println("Sum of fitness = " + SumFitness);
		Out.println("Best objective = " + BestObjective);
		Out.println("Best fitness = " + BestFitness);
		Out.println("Worst objective = " + WorstObjective);
		Out.println("Worst fitness = " + WorstFitness);
		Out.println("Average objective = " + SumObjective / Pop.size());
		Out.println("Average fitness = " + SumFitness / Pop.size());
		
		Out.println("---------------------");
		Out.println();
	}
	
	/**
	 * Dump results to the specified stream.
	 * @param Out PrintStream to which results have to be dumped
	 */
	public void dumpResult(PrintStream Out)
	{
		Out.println();
		Out.println("------------------------");
		Out.println("Genetic Algorithm Output");
		Out.println("------------------------");
		
		Gene Fittest = getFittest();
		Gene Weakest = getWeakest();
		
		Out.println("Best objective = " + Fittest.getObjective());
		Out.println("Best fitness = " + Fittest.getFitness());
		Out.println("Worst objective = " + Weakest.getObjective());
		Out.println("Worst fitness = " + Weakest.getFitness());
		Out.println("Average objective = " + SumObjective / Pop.size());
		Out.println("Average fitness = " + SumFitness / Pop.size());
		
		Out.println();
		Out.println("Best solution: " + Fittest);
		Out.println("Corresponds to: " + Fittest.decode());
		Out.println("Worst solution: " + Weakest);
		Out.println("Corresponds to: " + Weakest.decode());
		
		Out.println("------------------------");
		Out.println();
	}
}
