package evoman.ec.evolution.operators;


import evoict.*;
import evoman.config.*;
import evoman.ec.evolution.*;
import evoman.evo.*;
import evoman.evo.pop.*;
import evoman.evo.vm.*;



/**
 * 
 * Weighted merge operator takes in one or more population and produces a
 * population with a composition based on the weights of incoming pipes.
 * 
 * 
 * Parameters
 * 
 * pop_size
 * size of the resulting population
 * 
 * @author ruppmatt
 * 
 */

@ConfigProxy(proxy_for = EvolutionOpConfig.class)
@ConfigRegister(name = "WeightedMerge")
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
	public Population produce(VariationManager vm) throws BadConfiguration {
		if (drainPipes()) {
			WeightedIndex<Genotype> genotypes = new WeightedIndex<Genotype>();
			for (EvolutionPipeConfig pc : _received.keySet()) {
				double weight = pc.getWeight();
				Population p = _received.get(pc);
				for (Genotype g : p.getGenotypes()) {
					genotypes.add(g, weight);
				}
			}
			int pop_size = (getConfig().I("pop_size") == Constants.ASINPUT) ? vm.getPopSize()
					: getConfig()
							.I("pop_size");
			double total_weight = genotypes.totalWeight();

			Population new_pop = new Population();
			for (int k = 0; k < pop_size; k++) {
				double w = total_weight * vm.getRandom().nextDouble();
				Genotype inc = genotypes.get(w);
				new_pop.addGenotype((Genotype) inc.clone());
			}
			return new_pop;
		} else {
			throw new BadConfiguration(getConfig().getName() + ": unable to receive populations.");
		}
	}

}
