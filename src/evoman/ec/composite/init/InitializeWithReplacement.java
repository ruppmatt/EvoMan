package evoman.ec.composite.init;


import evoict.*;
import evoman.ec.composite.*;
import evoman.evo.pop.*;
import evoman.evo.structs.*;



public class InitializeWithReplacement extends CompositeInitializer {

	private static final long	serialVersionUID	= 1L;



	@Override
	public Representation retrieve(Composite dst, EvoPool src, int loc, EMState state) throws BadConfiguration {
		Population p = src.getPopulation();
		if (p == null) {
			throw new BadConfiguration("InitializeWithReplacement: Unable to locate a population in EvoPool "
					+ src.getFullName());
		}
		Genotype g = p.getRandomGenotype(state);
		if (g == null) {
			throw new BadConfiguration("InitializeWithReplacement: " + src.getFullName() + " returned a null genotype.");
		}
		return g.rep();
	}



	/**
	 * InitializeWithReplacement does not take any configuration parameters.
	 */
	@Override
	public void validate() throws BadConfiguration {
	}

}
