package evoman.ec.mutation;


import java.util.*;

import evoman.evo.*;
import evoman.evo.pop.*;



@EvolutionDescriptor(name = "ElitistSelection", max_in = 1, min_in = 1, selection = true)
public class ElitistSelection extends EvolutionOperator {

	private static final long	serialVersionUID	= 1L;



	public void validate(EvolutionOpConfig conf) throws BadConfiguration {
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
				return doMutation(p);
			}

		} else {
			throw new BadConfiguration(getConfig().getName() + ": unable to receive populations.");
		}
	}



	protected Population doMutation(Population p) throws BadConfiguration {
		Population np = new Population(p.getESParent());
		int num = getConfig().I("num");
		@SuppressWarnings("unchecked")
		ArrayList<Genotype> genotypes = (ArrayList<Genotype>) p.getGenotypes().clone();
		Collections.sort(genotypes, new FitnessComparator());

		for (int k = 0; k < num; k++) {
			np.addGenotype(genotypes.get(k));
		}

		return np;
	}
}
