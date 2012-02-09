package evoman.ec.evolution;


import java.util.*;

import evoict.*;
import evoman.evo.*;
import evoman.evo.pop.*;



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
		if (!conf.validate("attempts", Integer.class) || conf.I("attempts") < 0) {
			bc.append("ReplacementOperator: maximum replacement attempts not set or less than 0.");
		}
		bc.validate();
	}



	public ReplaceOperator(EvolutionPipeline pipeline, EvolutionOpConfig conf) {
		super(pipeline, conf);
	}



	@Override
	public Population produce() throws BadConfiguration {
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
				return replacement.clone();
			}

			Population new_pop = background.clone();
			@SuppressWarnings("unchecked")
			ArrayList<Genotype> old_gens = (ArrayList<Genotype>) new_pop.getGenotypes().clone();
			for (int k = 0; k < replacement.size(); k++) {
				int ndx = _pipeline.getRandom().nextInt(old_gens.size());
				Genotype to_replace = old_gens.get(ndx);
				old_gens.remove(ndx);
				new_pop.replaceGenotype(to_replace, (Genotype) replacement.getGenotype(k).clone());

			}
			return new_pop;

		} else {
			throw new BadConfiguration(getConfig().getName() + ": unable to receive populations.");
		}
	}
}
