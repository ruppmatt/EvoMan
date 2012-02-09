package evoman.ec.evolution;


import java.util.*;

import evoman.evo.pop.*;



//Descending sort
public class FitnessComparator implements Comparator<Genotype> {

	@Override
	public int compare(Genotype arg0, Genotype arg1) {
		if (arg0.getFitness() > arg1.getFitness()) {
			return -1;
		} else if (arg0.getFitness() < arg1.getFitness()) {
			return 1;
		} else {
			return 0;
		}
	}

}
