package ea;

import java.math.BigDecimal;
import java.util.Random;

public class Parameters {

	public static Random rnd = new Random(System.currentTimeMillis());
	
	/**
	 * used as a seed
	 * 
	 */	
	static final boolean [] DEFAULT_WOMENS_TRANSITION_STRATEGY = {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true};
	//public static final int [] DEFAULT_WOMENS_PACING_STRATEGY = {300, 300, 300, 300, 300, 300, 300, 350, 350, 300, 300, 350, 350, 350, 350, 300, 300, 350, 350, 350, 350, 300, 300};
	public static final int [] DEFAULT_WOMENS_PACING_STRATEGY = {900, 400, 400, 400, 400, 400, 400, 400, 400, 400, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300};

	
	//public static BigDecimal popSize = new BigDecimal(Math.pow(2,DEFAULT_WOMENS_TRANSITION_STRATEGY.length));
	public static int popSize = 4;
	public static int tournamentSize = 5;
	
	public static int mutationRateMax = 6;//out of len
	public static double mutationProbability = 0.5;
	public static double crossoverProbability = 1.0;
	
	public static int maxIterations = 5000;
	
	
}
