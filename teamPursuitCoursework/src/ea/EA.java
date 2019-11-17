package ea;

/***
 * This is an example of an EA used to solve the problem
 *  A chromosome consists of two arrays - the pacing strategy and the transition strategy
 * This algorithm is only provided as an example of how to use the code and is very simple - it ONLY evolves the transition strategy and simply sticks with the default
 * pacing strategy
 * The default settings in the parameters file make the EA work like a hillclimber:
 * 	the population size is set to 1, and there is no crossover, just mutation
 * The pacing strategy array is never altered in this version- mutation and crossover are only
 * applied to the transition strategy array
 * It uses a simple (and not very helpful) fitness function - if a strategy results in an
 * incomplete race, the fitness is set to 1000, regardless of how much of the race is completed
 * If the race is completed, the fitness is equal to the time taken
 * The idea is to minimise the fitness value
 */


import java.util.ArrayList;
import java.util.Arrays;

import teamPursuit.TeamPursuit;
import teamPursuit.WomensTeamPursuit;

public class EA implements Runnable{
	
	// create a new team with the default settings
	public static TeamPursuit teamPursuit = new WomensTeamPursuit(); 
	
	private ArrayList<Individual> population = new ArrayList<Individual>();
	private ArrayList<Individual> subpopbest = new ArrayList<Individual>();
	private ArrayList<Individual> subpopmidddle = new ArrayList<Individual>();
	private ArrayList<Individual> subpopworst = new ArrayList<Individual>();
	//initialise the ps arraylist which will input 10 random points from each generation
	private ArrayList<Individual> ps = new ArrayList<Individual>();
	private int iteration = 0;
	
	public EA() {
		
	}

	
	public static void main(String[] args) {
		EA ea = new EA();
		ea.run();
	}

	public void run() {
//		initialisePopulation();
//		System.out.println("finished init pop");
//		iteration = 0;
//		while(iteration < Parameters.maxIterations){
//			iteration++;
//			Individual parent1 = tournamentSelection();
//			Individual parent2 = tournamentSelection();
//			Individual child = crossover(parent1, parent2);
//			child = mutate(child);
//			child.evaluate(teamPursuit);
//			replace(child);
//			printStats();
//		}
//		Individual best = getBest(population);
//		best.print();


//		initialisePopulation();
//		System.out.println("finished init pop");
//		iteration = 0;
//
//		while(iteration < Parameters.maxIterations){
//			iteration++;
//			bubbleOrganisePop(population);
//			splitIntoSubPops();
//			mutateMultiSub();
//			for (Individual a: population) {
//				a.evaluate(teamPursuit);
//			}
//			printStats();
//		}
//		Individual best = getBest(population);
//		best.print();

		initialisePopulation();
		for (Individual a:population) {
			a.initialise_default();
		}
		System.out.println("finished init pop");
		ArrayList<boolean[]> combinations = new ArrayList<boolean[]>();
		iteration = 0;

		combinations.add(0, new boolean[] {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true});

		Individual temp2 = population.get(0);
		while(iteration < Parameters.maxIterations){

			boolean[] temp = {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true};
			//while the temp is already a combination
			// TODO: 17/11/2019 for some reason when the temp is being changed in line 101, that changes the temp stored in the combinations arraylist which then makes it an endless loop
			while(combinations.contains(temp)){
				//change a random element of temp
				int index = Parameters.rnd.nextInt(population.get(iteration).transitionStrategy.length);
				temp[index] = !temp[index];
//				System.out.println("yo");
			}

			//set the current iteration to a new combination and move onto the next one
			temp2 = population.get(iteration);
			temp2.transitionStrategy = temp;
			population.set(iteration, temp2);

			//add temp to the combinations tried
			combinations.add(combinations.size(), temp);

			//evaluate its time
			population.get(iteration).evaluate(teamPursuit);
			//print the best time and other stuff
			printStats();
			iteration++;
		}

		Individual best = getBest(population);
		best.print();

	}

	//DEO
	//organise the pop to order by fitness value
	//should be a bubble sort that puts the pops fitness values into ascending order
	private void bubbleOrganisePop(ArrayList<Individual> aPopulation) {
		int n = aPopulation.size();
		for (int i = 0; i < n-1; i++)
			for (int j = 0; j < n-i-1; j++)
				if (aPopulation.get(j).getFitness() > aPopulation.get(j+1).getFitness()) {
					// swap j with j+1
					Individual temp = aPopulation.get(j);
					aPopulation.set(j, aPopulation.get(j+1));
					aPopulation.set(j+1, temp);
				}
	}

	private void printStats() {		
		System.out.println("" + iteration + "\t" + getBest(population) + "\t" + getWorst(population));		
	}

	//DEO
	//spit into sub populations to do different things with them
	private void splitIntoSubPops(){
		//clear the current subpops
		subpopbest.clear();
		subpopmidddle.clear();
		subpopworst.clear();
		//spilt the population into sub populations
		for(int i=0; i<population.size(); ++i){
			if(i<population.size()/3){
				subpopbest.add(population.get(i));
			}
			if(i<population.size()/3*2 && i>= population.size()/3){
				subpopmidddle.add(population.get(i));
			}
			else{
				subpopworst.add(population.get(i));
			}
		}
	}

	//DEO
	//Mutate multi-sub mutation strategy
	private void mutateMultiSub(){

		// choose how many elements to alter
		int mutationRate = 1 + Parameters.rnd.nextInt(Parameters.mutationRateMax);

		//loop for all in the worst subpop
		for(int i=0; i< subpopworst.size();++i){
			//loop for all booleans in each of the worst subpop
			for(int j=0;j< subpopworst.get(i).transitionStrategy.length;++j){
				//if the current boolean is equal to the best in the whole population, then change the boolean (exploration)
				if(subpopworst.get(i).transitionStrategy[j] == getBest(population).transitionStrategy[j]){
					subpopworst.get(i).transitionStrategy[j] = !subpopworst.get(i).transitionStrategy[j];
				}
			}
		}

		//put 10 random points from this generation into the ps arraylist
		for(int i =0; i<11; ++i){
			ps.add(population.get(Parameters.rnd.nextInt(population.size())));
		}

		//loop for all in middle subpop
		for(int i=0; i< subpopmidddle.size();++i){
			//choose a random number of components to be changed in the current Individual
			for(int j = 0; j < mutationRate; ++j){
				//get a random boolean
				int index = Parameters.rnd.nextInt(subpopmidddle.get(i).transitionStrategy.length);
				//if the random boolean is not equal to the best in the ps arraylist then change it to match the best (exploitation).
				if(subpopmidddle.get(i).transitionStrategy[index] != getBest(ps).transitionStrategy[index]) {
					subpopmidddle.get(i).transitionStrategy[index] = !subpopmidddle.get(i).transitionStrategy[index];
				}
			}
			//then randomly change some components of the same individual (exploration)
			for(int j=0; j< mutationRate; ++j){
				//get a random boolean
				int index = Parameters.rnd.nextInt(subpopmidddle.get(i).transitionStrategy.length);
				//swap that random boolean
				subpopmidddle.get(i).transitionStrategy[index] = !subpopmidddle.get(i).transitionStrategy[index];
			}
		}

		//loop for all in the best subpop
		for(int i=0; i< subpopbest.size();++i){
			//get the best in the population and compare to each in the best sub pop with some random changes (exploitation)
			//copy current for comparison
			Individual temp = subpopbest.get(i);
			for(int j = 0; j < mutationRate; ++j){
				//get a random boolean
				int index = Parameters.rnd.nextInt(temp.transitionStrategy.length);
				//swap that random boolean
				temp.transitionStrategy[index] = !temp.transitionStrategy[index];
			}
			//compare and replace if better fitness than itself
			if(temp.getFitness() < subpopbest.get(i).getFitness()){
				subpopbest.set(i, temp);
			}
		}
	}


	private void replace(Individual child) {
		Individual worst = getWorst(population);
		if(child.getFitness() < worst.getFitness()){
			int idx = population.indexOf(worst);
			population.set(idx, child);
		}
	}


	private Individual mutate(Individual child) {
		if(Parameters.rnd.nextDouble() > Parameters.mutationProbability){
			return child;
		}
		
		// choose how many elements to alter
		int mutationRate = 1 + Parameters.rnd.nextInt(Parameters.mutationRateMax);
		
		// mutate the transition strategy

			//mutate the transition strategy by flipping boolean value
			for(int i = 0; i < mutationRate; i++){
				int index = Parameters.rnd.nextInt(child.transitionStrategy.length);
				child.transitionStrategy[index] = !child.transitionStrategy[index];
			}
		
		return child;
	}


	private Individual crossover(Individual parent1, Individual parent2) {
		if(Parameters.rnd.nextDouble() > Parameters.crossoverProbability){
			return parent1;
		}
		Individual child = new Individual();
		
		int crossoverPoint = Parameters.rnd.nextInt(parent1.transitionStrategy.length);
		
		// just copy the pacing strategy from p1 - not evolving in this version
		for(int i = 0; i < parent1.pacingStrategy.length; i++){			
			child.pacingStrategy[i] = parent1.pacingStrategy[i];
		}
		
		
		for(int i = 0; i < crossoverPoint; i++){
			child.transitionStrategy[i] = parent1.transitionStrategy[i];
		}
		for(int i = crossoverPoint; i < parent2.transitionStrategy.length; i++){
			child.transitionStrategy[i] = parent2.transitionStrategy[i];
		}
		return child;
	}


	/**
	 * Returns a COPY of the individual selected using tournament selection
	 * @return
	 */
	private Individual tournamentSelection() {
		ArrayList<Individual> candidates = new ArrayList<Individual>();
		for(int i = 0; i < Parameters.tournamentSize; i++){
			candidates.add(population.get(Parameters.rnd.nextInt(population.size())));
		}
		return getBest(candidates).copy();
	}


	private Individual getBest(ArrayList<Individual> aPopulation) {
		double bestFitness = Double.MAX_VALUE;
		Individual best = null;
		for(Individual individual : aPopulation){
			if(individual.getFitness() < bestFitness || best == null){
				best = individual;
				bestFitness = best.getFitness();
			}
		}
		return best;
	}

	private Individual getWorst(ArrayList<Individual> aPopulation) {
		double worstFitness = 0;
		Individual worst = null;
		for(Individual individual : population){
			if(individual.getFitness() > worstFitness || worst == null){
				worst = individual;
				worstFitness = worst.getFitness();
			}
		}
		return worst;
	}
	
	private void printPopulation() {
		for(Individual individual : population){
			System.out.println(individual);
		}
	}

	private void initialisePopulation() {
		while(population.size() < Parameters.popSize){
			Individual individual = new Individual();
			individual.initialise();			
			individual.evaluate(teamPursuit);
			population.add(individual);
							
		}		
	}	
}
