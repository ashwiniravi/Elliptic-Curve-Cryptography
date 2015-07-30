package com;
import java.math.BigInteger;
import java.util.Random;

public class Main {

	/**
	 * @param args
	 */
	
		// TODO Auto-generated method stub
		public static void applyReproduction(Population Pop)
		{
			double CumSum[] = new double[Pop.getPopSize()];
			for (int i = 0; i < CumSum.length; i++)
				CumSum[i] = Pop.get(i).getFitness();
			
			Pop.copy(Operators.reproduce(Pop, CumSum));
		}
		
		/**
		 * Describes how Crossover should be applied to the entire population.
		 * @param Pop Apply Crossover to this population
		 */
		public static void applyCrossover(Population Pop)
		{
			Pop.sort();
			Population NewPop = new Population(Pop.getPopSize(), Pop.getGeneLength());
			for (int i = 0; i < Pop.getPopSize(); i += 2)
			{
				Gene ParentA = Pop.get(i);
				Gene ParentB = Pop.get(i + 1);
				Gene ChildA = new Gene(Pop.getGeneLength());
				Gene ChildB = new Gene(Pop.getGeneLength());
				
				Operators.crossover(ParentA, ParentB, ChildA, ChildB);
				
				NewPop.set(i, ChildA);
				NewPop.set(i + 1, ChildB);
			}
			Pop.copy(NewPop);
		}
		
		/**
		 * Describes how Mutation should be applied to the entire population.
		 * @param Pop Apply Mutation to this population
		 */
		public static void applyMutation(Population Pop)
		{
			for (int i = 0; i < Pop.getPopSize(); i++)
			{
				Gene G = Pop.get(i);
				Operators.mutate(G);
				Pop.set(i, G);
			}
		}

	


public static void main(String[] args) {
	BigInteger Prime = new BigInteger("10007");
	Parameters.init(Prime);
	Gene.init(Prime);
	
	Population P = new Population(32, 16, new Random());
	
	for (int i = 0; i < 1024; i++)
	{
		Gene Best = P.getFittest();
		
		System.out.println(Best.decode() + " - " + Best.getNP() + " - " + Best.getFitness());
		if (Best.getFitness() == 1.0)
		{
			System.out.println("Terminated after " + i + " generation(s).");
			break;
		}
		Population OldP = new Population(P);
		//Operators.scale(P);
		applyReproduction(P);
		applyMutation(P);
		applyCrossover(P);
		Operators.elitism(OldP, P);
	}
	
	P.dumpResult(System.out);
	
	/*Population Old = new Population(8, 12, new Random());
	Population New = new Population(8, 12, new Random());
	New.sort();
	Old.sort();
	New.dumpInfo(System.out);
	Old.dumpInfo(System.out);
	Operators.elitism(Old, New, 0.5);
	New.dumpInfo(System.out);
	Operators.dumpStats();*/
}
}

