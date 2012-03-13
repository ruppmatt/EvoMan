package evoman.ec.evolution.operators;


import java.util.*;

import evoict.*;
import evoman.config.*;
import evoman.ec.evolution.*;
import evoman.evo.pop.*;



/**
 * 
 * Elitist Selection takes a single population and produces a population
 * containing the type n (num) genotypes.
 * 
 * Parameters
 * num
 * number of genotypes to select
 * 
 * @author ruppmatt
 * 
 */

@ConfigProxy(proxy_for = EvolutionOpConfig.class)
@ConfigRegister(name = "ElitistSelection")
@EvolutionDescriptor(name = "ElitistSelection", max_in = 1, min_in = 1, selection = true)
public class ElitistSelection extends EvolutionOperator {

	private static final long	serialVersionUID	= 1L;



	public static void validate(EvolutionOpConfig conf) throws BadConfiguration {
		if (!conf.validate("num", Integer.class) || conf.I("num") < 0) {
			throw new BadConfiguration(conf.getName() + ": is not set or less than 0");
		}
	}



	public ElitistSelection(EvolutionPipeline pipeline, EvolutionOpConfig conf) {
		super(pipeline, conf);
	}



	@Override
	public Population produce() throws BadConfiguration {
		if (drainPipes()) {
			if (_received.size() != 1) {
				throw new BadConfiguration(getConfig().getName() + ": expected 1 population, received "
						+ _received.size());
			} else {
				Population p = (Population) _received.values().toArray()[0];
				return doSelect(p);
			}

		} else {
			throw new BadConfiguration(getConfig().getName() + ": unable to receive populations.");
		}
	}



	/**
	 * Select the top n (set by num) genotypes.
	 * 
	 * @param p
	 * @return
	 * @throws BadConfiguration
	 */
	protected Population doSelect(Population p) throws BadConfiguration {
		Population np = new Population();
		int num = getConfig().I("num");
		@SuppressWarnings("unchecked")
		ArrayList<Genotype> genotypes = (ArrayList<Genotype>) p.getGenotypes().clone();
		Collections.sort(genotypes, new FitnessComparator());
		if (num > p.size()) {
			throw new BadConfiguration("Elitist selection: more genotypes requested (num) than in initial population.");
		}
		for (int k = 0; k < num; k++) {
			np.addGenotype(genotypes.get(k));
		}

		return np;
	}
}
