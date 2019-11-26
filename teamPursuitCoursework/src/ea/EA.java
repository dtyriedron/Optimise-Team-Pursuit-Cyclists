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


import java.math.BigDecimal;
import java.math.BigInteger;
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
	//combinations
    private final int n =22;
    private BigInteger bi = BigInteger.ZERO;
    private BigDecimal rows = new BigDecimal(Math.pow(2,n));
    private int k=0;
    private boolean[][] bigBoolArray = new boolean[rows.intValue()][n];


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
//			getBest(population).evaluate(teamPursuit);
//			printStats();
//		}
//
//		Individual best = getBest(population);
//		best.print();

		// TODO: 22/11/2019 new islands strat maybe? 
		// TODO: 22/11/2019 could look back at a certain number of iterations if there is no change after a certain number of iterations maybe change the strat
		//hill_climber();
		//multiple_hills_climber();
		//SA();
		//tabu();
		//basicEA();
		societyEA();


//        initialisePopulation();
//        System.out.println("finished init pop");
//        //calc all the different permutations of populations transition strategy
//        calcPermutations();
//        //for each individual in the population, change the transition strategy to a permutation
//        for(int i=0;i<population.size();i++){
//            population.get(i).transitionStrategy = bigBoolArray[i];
//            population.get(i).evaluate(teamPursuit);
//            printStats();
//            iteration++;
//        }
//		Individual best = getBest(population);
//		best.print();

	}

	private void printStats() {		
		System.out.println("" + iteration + "\t" + getBest(population) + "\t" + getWorst(population));		
	}

	//iterated hill climber
	private void hill_climber(){
		initialisePopulation();
		System.out.println("finished init pop");
		iteration = 0;
		double bestFitness = 0.0;
		int localCounter = 0;
		int hill_climb_rate = 2;
		Individual y;

		while(iteration < Parameters.maxIterations || localCounter == 800) {
			iteration++;

			int randomPos = Parameters.rnd.nextInt(population.size());
			Individual x = population.get(randomPos);
			//Evaluate x
			x.evaluate(teamPursuit);

			y = x.copy();

			if(bestFitness == getBest(population).getFitness()){
				//increase local optima counter
				localCounter++;
			}
			else{
				localCounter = 0;
			}
//			bestFitness = getBest(population).getFitness();
//			if(localCounter >99){
//				System.out.println("Theres too many of them!");
//				for (int i = 0; i < hill_climb_rate; i++) {
//					int index = Parameters.rnd.nextInt(y.transitionStrategy.length);
//					y.transitionStrategy[index] = !y.transitionStrategy[index];
//				}
//			}


			//move two random positions
//			for (int i = 0; i < hill_climb_rate; i++) {
//				int index = Parameters.rnd.nextInt(y.transitionStrategy.length);
//				y.transitionStrategy[index] = !y.transitionStrategy[index];
//			}
			for (int i = 0; i < hill_climb_rate; i++) {
				int index = Parameters.rnd.nextInt(y.transitionStrategy.length);
				y.transitionStrategy[index] = !y.transitionStrategy[index];
			}

			for (int i = 0; i < hill_climb_rate; i++) {
				int index = Parameters.rnd.nextInt(y.pacingStrategy.length);
				int randomnum = Parameters.rnd.nextInt(1000) + 200;
//				//try and get the random number to be closer to the middle
//				if(randomnum < 600){
//					randomnum = randomnum+ Parameters.rnd.nextInt(300) + 100;
//				}
//				else{
//					randomnum = randomnum - Parameters.rnd.nextInt(300) - 100;
//				}
				y.pacingStrategy[index] = randomnum;
			}


			//evaulate y
			y.evaluate(teamPursuit);

			if (y.getFitness() < x.getFitness()) {
				//replace y in random pos if its better than x
				population.set(randomPos, y);
			}
			//for every 100 iterations
//			if(iteration % 100 == 0){
//				//if the saved best is not equal to the current best or has not been saved yet (in first 100 iterations) then
//				if(bestFitness != getBest(population).getFitness() || iteration==100){
//					//save the current best
//					bestFitness = getBest(population).getFitness();
//				}
//				//if the saved best is equal to the current best and the iteration is a multiple of 100
//				else {
//					//mutate
//				}
//
//			}

			printStats();
		}
		Individual best = getBest(population);
		best.print();
	}

	//iterated multiple hills climber
	// TODO: 23/11/2019 seems to change the best in the popluation and make it worse sometimes
	void multiple_hills_climber(){
		initialisePopulation();
		System.out.println("finished init pop");
		iteration = 0;
		for(Individual a : population){
			a.evaluate(teamPursuit);
		}

		while(iteration < Parameters.maxIterations) {
			iteration++;

			int randomPos = Parameters.rnd.nextInt(population.size());
			Individual x = population.get(randomPos);
			ArrayList<Individual> hills = new ArrayList<Individual>();

			int hill_climb_rate = 2;
			int num_of_hills = 3;

			//copy x
			Individual y = x.copy();

			//add copies of x and store them in the list
			for (int i =0; i< num_of_hills;i++) {
				hills.add(y);
			}

			//move two random positions for every hill in hills
			for (Individual a: hills) {
				for (int i = 0; i < hill_climb_rate; i++) {
					int index = Parameters.rnd.nextInt(a.transitionStrategy.length);
					a.transitionStrategy[index] = !a.transitionStrategy[index];
				}
			}

			//Evaluate hills after changes made
			for (Individual a : hills) {
				a.evaluate(teamPursuit);
			}

			//find the best fitness from the hills list and replace x
			if (getBest(hills).getFitness() < x.getFitness()) {
				//replace y in random pos if its better than x
				population.set(randomPos, getBest(hills));
			}
			else{
				replace(x);
			}
			printStats();
		}
		Individual best = getBest(population);
		best.print();
	}

	//Simulated Annealing
	void SA(){
		initialisePopulation();
		System.out.println("finished init pop");
		iteration = 0;
		for(Individual a : population){
			a.evaluate(teamPursuit);
		}
		int randomPos = Parameters.rnd.nextInt(population.size());
		Individual x = population.get(randomPos);
		double probability = 1.0;

		x.evaluate(teamPursuit);

		Individual y = x.copy();

		int move = 2;

		while(iteration < Parameters.maxIterations) {
			iteration++;

			for (int i = 0; i < move; i++) {
				int index = Parameters.rnd.nextInt(y.transitionStrategy.length);
				y.transitionStrategy[index] = !y.transitionStrategy[index];
			}

			y.evaluate(teamPursuit);

			if(y.getFitness() < x.getFitness()){
				x = y.copy();
				population.set(randomPos, y);
			}
			else if(probability > 0.0){
				x = y.copy();
				population.set(randomPos,y);
				probability = probability-0.1;
			}
			else{
				replace(x);
			}

			printStats();
		}
		Individual best = getBest(population);
		best.print();

	}

	//tabu search
	void tabu(){
		initialisePopulation();
		System.out.println("finished init pop");
		iteration = 0;
		for(Individual a : population){
			a.evaluate(teamPursuit);
		}

		ArrayList<Individual> randoms = new ArrayList<Individual>();
		for (int i =0; i< 4;i++) {
			randoms.add(population.get(Parameters.rnd.nextInt(population.size())));
		}
		getBest(randoms);

		ArrayList<Individual> lastrandoms = new ArrayList<Individual>();


		while(iteration < Parameters.maxIterations) {
			iteration++;

			//copy the last set of elements
			lastrandoms = (ArrayList<Individual>) randoms.clone();

			//change one element for each in the list
			for (Individual a: randoms) {
				int index = Parameters.rnd.nextInt(a.transitionStrategy.length);
				a.transitionStrategy[index] = !a.transitionStrategy[index];
			}

			for (Individual a: randoms) {
				a.evaluate(teamPursuit);
			}

			//check if move hasnt been made in the last 4 iterations
			for (int i = 0; i<randoms.size();i++) {
				//if the move improves fitness then move
				if(randoms.get(i).getFitness() < lastrandoms.get(i).getFitness()){
					population.add(randoms.get(i));
				}
				else{
					replace(randoms.get(i));
				}
			}

			printStats();
		}

		Individual best = getBest(population);
		best.print();
	}

	//basic EA
	void basicEA(){
		initialisePopulation();
		System.out.println("finished init pop");
		iteration = 0;

		while(iteration < Parameters.maxIterations) {
			iteration++;
			Individual par1 = tournamentSelection();
			Individual par2 = tournamentSelection();
			Individual child = crossover(par1, par2);
			child = mutate(child);
			child.evaluate(teamPursuit);
			replace(child);

			printStats();
		}
		Individual best = getBest(population);
		best.print();
	}

	//society EA
	void societyEA(){
		initialisePopulation();
		System.out.println("finished init pop");
		iteration = 0;
		double bestFitness = 0.0;
		int localCounter = 0;



		while(iteration < Parameters.maxIterations || localCounter == 800) {
			iteration++;

			if(bestFitness == getBest(population).getFitness()){
				//increase local optima counter
				localCounter++;
			}
			else{
				localCounter = 0;
			}


			//create a society-select the best 16 in the population
			ArrayList<Individual> society = new ArrayList<Individual>();

			bubbleOrganisePop(population);
			//add members of society to society
			for(int i=0;i<population.size()/4*3+1;i++){
				society.add(population.get(i));
			}
			//crossover half of society with the other half
			for(int i = 0; i< society.size();i++) {
				int rand1 = Parameters.rnd.nextInt(society.size());
				int rand2 = Parameters.rnd.nextInt(society.size());
				Individual par1 = society.get(rand1);
				Individual par2 = society.get(rand2);
				Individual child = crossover(par1, par2);
				child = mutate(child);
				child.evaluate(teamPursuit);
				replace(child);
			}


			printStats();
		}
		Individual best = getBest(population);
		best.print();
	}


	private void calcPermutations(){
        while(bi.compareTo(rows.toBigInteger())<0){
            String bin = bi.toString(2);
            while(bin.length()<n){
                bin = "0" + bin;
            }
            char[] chars = bin.toCharArray();
            boolean[] boolArray = new boolean[n];
            for(int j=0;j<chars.length;j++){
                boolArray[j] = chars[j] == '0' ? true:false;
            }
            //System.out.println(Arrays.toString(boolArray));
            bigBoolArray[k] = boolArray;
            k++;
            bi = bi.add(BigInteger.ONE);
        }
        //System.out.print(bigBoolArray.length + " k = " + k);
	}


	//DEO
	//organise the pop to order by fitness value
	//should be a bubble sort that puts the pops fitness values into ascending order
	private void bubbleOrganisePop(ArrayList<Individual> aPopulation) {
		int n = aPopulation.size();
		for (int i = 0; i < n-1; i++)
			for (int j = 0; j < n-i-1; j++){
				if (aPopulation.get(j).getFitness() > aPopulation.get(j+1).getFitness()) {
					// swap j with j+1
					Individual temp = aPopulation.get(j);
					aPopulation.set(j, aPopulation.get(j+1));
					aPopulation.set(j+1, temp);
				}
			}
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
				//change the pacing strat
				int index2 = Parameters.rnd.nextInt(child.pacingStrategy.length);
				child.pacingStrategy[index2] = Parameters.rnd.nextInt(1000)+200;
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
