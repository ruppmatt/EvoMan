package evoman.ec.evolution;


import evoict.*;
import evoman.evo.*;
import evoman.evo.pop.*;



@EvolutionDescriptor(name = "WeightedMerge", min_in = 1, max_in = 255)
public class WeightedMergeOperator extends EvolutionOperator {

	private static final long	serialVersionUID	= 1L;



	public static void validate(EvolutionOpConfig conf) throws BadConfiguration {
		if (!conf.validate("pop_size", Integer.class)) {
			conf.set("pop_size", Constants.ASINPUT);
		}
	}



	public WeightedMergeOperator(EvolutionPipeline pipeline, EvolutionOpConfig conf) {
		super(pipeline, conf);
	}



	@Override
	public Population produce() throws BadConfiguration {
		if (drainPipes()) {
			WeightedIndex<Genotype> genotypes = new WeightedIndex<Genotype>();
			for (EvolutionPipeConfig pc : _received.keySet()) {
				double weight = pc.getWeight();
				Population p = _received.get(pc);
				for (Genotype g : p.getGenotypes()) {
					genotypes.add(g, weight);
				}
			}
			int pop_size = (getConfig().I("pop_size") == Constants.ASINPUT) ? _pipeline._vm.getPopSize() : getConfig()
					.I("pop_size");
			double total_weight = genotypes.totalWeight();

			Population new_pop = new Population(_pipeline._vm);
			for (int k = 0; k < pop_size; k++) {
				double w = total_weight * _pipeline.getRandom().nextDouble();
				// System.err.println("Total weight= " + total_weight +
				// "\t find=" + w);
				Genotype inc = genotypes.get(w);
				new_pop.addGenotype((Genotype) inc.clone());
			}
			return new_pop;
		} else {
			throw new BadConfiguration(getConfig().getName() + ": unable to receive populations.");
		}
	}

}
