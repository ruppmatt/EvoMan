package evoman.ec.evolution.operators;


import java.util.*;

import evoict.*;
import evoman.config.*;
import evoman.ec.evolution.*;
import evoman.evo.pop.*;
import evoman.evo.vm.*;



/**
 * Takes in two populations a "background" and a "replacement". The "background"
 * population gets overwritten by the replacement. Replacement is abandoned
 * after "num_attempts" have elapsed without finding a candidate to replace.
 * Replacement genotypes are guaranteed to be in the final population unless no
 * repalcement can be found (e.g. they will not overwrite each other).
 * 
 * 
 * Parameters
 * 
 * background
 * Name of background-producing evolution operator
 * 
 * replacement
 * name of replacement-producing evolution operator
 * 
 * 
 * num_attempts
 * number of attempts at finding a candidate genotype for replacement
 * 
 * @author ruppmatt
 * 
 */

@ConfigProxy(proxy_for = EvolutionOpConfig.class)
@ConfigRegister(name = "Replace")
@EvolutionDescriptor(name = "Replace", max_in = 2, min_in = 2)
public class ReplaceOperator extends EvolutionOperator {

	private static final long	serialVersionUID	= 1L;



	public static void validate(EvolutionOpConfig conf) throws BadConfiguration {
		BadConfiguration bc = new BadConfiguration();
		if (!conf.validate("background", String.class)) {
			bc.append("ReplaceOperator: Background population not named.");
		}
		if (!conf.validate("replacement", String.class)) {
			bc.append("ReplaceOperator: Replacement population not named.");
		}
		if (!conf.validate("num_attempts", Integer.class) || conf.I("num_attempts") < 0) {
			bc.append("ReplaceOperator: num_attempts is not set or less than zero.");
		}
		bc.validate();
	}



	public ReplaceOperator(EvolutionPipeline pipeline, EvolutionOpConfig conf) {
		super(pipeline, conf);
	}



	@Override
	public Population produce(VariationManager vm) throws BadConfiguration {
		if (drainPipes()) {
			if (_received.size() != 2)
				throw new BadConfiguration(getConfig().getName() + ": expected 2 input populations, received "
						+ _received.size());
			Population background = null;
			Population replacement = null;
			for (EvolutionPipeConfig epc : _received.keySet()) {
				if (epc.getSender().getName().equals(getConfig().S("background"))) {
					background = _received.get(epc);
				}
				if (epc.getSender().getName().equals(getConfig().S("replacement"))) {
					replacement = _received.get(epc);
				}
			}
			if (background == null)
				throw new BadConfiguration(getConfig().getName() + ": no background population found.");
			if (replacement == null)
				throw new BadConfiguration(getConfig().getName() + ": no replacement population found.");
			if (background.size() < replacement.size()) {
				return (Population) replacement.clone();
			}

			Population new_pop = (Population) background.clone();
			@SuppressWarnings("unchecked")
			ArrayList<Genotype> old_gens = (ArrayList<Genotype>) new_pop.getGenotypes().clone();
			int num_attempts = getConfig().I("num_attempts");
			for (int k = 0; k < replacement.size(); k++) {
				int ndx = vm.getRandom().nextInt(old_gens.size());
				Genotype to_replace = old_gens.get(ndx);
				old_gens.remove(ndx);
				boolean replaced = false;
				int tries = 0;
				do {
					replaced = new_pop.replaceGenotype(to_replace, (Genotype) replacement.getGenotype(k).clone());
					tries++;
				} while (replaced == false && tries < num_attempts);
			}
			return new_pop;

		} else {
			throw new BadConfiguration(getConfig().getName() + ": unable to receive populations.");
		}
	}
}
