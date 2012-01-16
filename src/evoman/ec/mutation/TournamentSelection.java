package evoman.ec.mutation;


import java.util.*;

import evoman.ec.mutation.*;
import evoman.evo.*;
import evoman.evo.pop.*;



@EvolutionDescriptor(name = "TournamentSelection")
public class TournamentSelection extends EvolutionOperator {

	private static final long	serialVersionUID	= 1L;
	protected int				pop_size;
	protected int				tour_size;



	public static boolean validate(EvolutionOpConfig conf) {
		if (conf.validate("pop_size", Integer.class)) {
			if (conf.I("pop_size") < 1) {
				conf.set("pop_size", Constants.ASINPUT);
			}
		}
		if (!conf.validate("tour_size", Integer.class)) {
			return false;
		} else if (conf.I("tour_size") < 1) {
			return false;
		}
		return true;
	}



	public TournamentSelection(EvolutionPipeline ep, EvolutionOpConfig conf) {
		super(ep, conf);
		pop_size = _conf.I("pop_size");
		tour_size = _conf.I("tour_size");
	}



	@Override
	public Population produce() {
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
			_pipeline.getNotifier().fatal(getConfig().getName() + ": input populations are not ready for production.");
			return null;
		}
	}

}
