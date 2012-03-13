package evoman.ec.evolution.operators;


import java.util.*;

import evoict.*;
import evoman.config.*;
import evoman.ec.evolution.*;
import evoman.evo.*;
import evoman.evo.pop.*;



/**
 * 
 * Tournament selection takes more than one population and selects the most fit
 * individuals by competing tour_size genotypes against one another. Of the
 * tour_size genotypes, the most fit is selected to be included in the next
 * population. This process is repeated pop_size times to produce a single
 * output population. Genotypes are selected for competition at random, with
 * repalcement.
 * 
 * Parameters
 * 
 * pop_size
 * size of the final population
 * 
 * tour_size
 * size of the tournament
 * 
 * @author ruppmatt
 * 
 */

@ConfigProxy(proxy_for = EvolutionOpConfig.class)
@ConfigRegister(name = "TournamentSelection")
@EvolutionDescriptor(name = "TournamentSelection", selection = true)
public class TournamentSelection extends EvolutionOperator {

	private static final long	serialVersionUID	= 1L;
	protected int				pop_size;
	protected int				tour_size;



	public static void validate(EvolutionOpConfig conf) throws BadConfiguration {
		BadConfiguration bc = new BadConfiguration();
		if (conf.validate("pop_size", Integer.class)) {
			if (conf.I("pop_size") < 1) {
				conf.set("pop_size", Constants.ASINPUT);
			}
		} else {
			conf.set("pop_size", Constants.ASINPUT);
		}
		if (!conf.validate("tour_size", Integer.class)) {
			bc.append("Tournament Selection: tournament size is missing.");
		} else if (conf.I("tour_size") < 1) {
			bc.append("Tournament Selection: tournament sie is less than 1.");
		}
		bc.validate();
	}



	public TournamentSelection(EvolutionPipeline ep, EvolutionOpConfig conf) {
		super(ep, conf);
		pop_size = _conf.I("pop_size");
		tour_size = _conf.I("tour_size");
	}



	@Override
	public Population produce() throws BadConfiguration {
		if (drainPipes()) {
			int num_genotypes = 0;

			for (Population p : _received.values()) {
				num_genotypes += p.size();
			}
			ArrayList<Genotype> all = new ArrayList<Genotype>(num_genotypes);
			for (Population p : _received.values()) {
				all.addAll(p.getGenotypes());
			}
			Population selected = new Population();
			int new_size = (pop_size != Constants.ASINPUT) ? pop_size : num_genotypes;
			for (int i = 0; i < new_size; i++) {
				Genotype winner = all.get(_pipeline.getRandom().nextInt(num_genotypes));
				for (int k = 1; k < tour_size; k++) {
					Genotype competitor = all.get(_pipeline.getRandom().nextInt(num_genotypes));
					winner = (winner.getFitness() < competitor.getFitness()) ? competitor : winner;
				}
				selected.addGenotype((Genotype) winner.clone());
			}

			return selected;
		} else {
			throw new BadConfiguration(getConfig().getName() + ": input populations are not ready for production.");
		}
	}

}
