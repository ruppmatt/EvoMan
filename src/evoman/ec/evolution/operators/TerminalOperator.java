package evoman.ec.evolution.operators;


import evoict.*;
import evoman.ec.evolution.*;
import evoman.evo.pop.*;
import evoman.evo.vm.*;



/**
 * Evolution terminator is used internally to store the final population. It
 * should not be included in a configuration file. The population received by
 * this operator must be the same size as the population size constraint set in
 * the variation manager.
 * 
 * @author ruppmatt
 * 
 */

@EvolutionDescriptor(name = "Terminal", min_in = 1, max_in = 1)
public class TerminalOperator extends EvolutionOperator {

	private static final long	serialVersionUID	= 1L;



	public TerminalOperator(EvolutionPipeline pipeline, EvolutionOpConfig conf) {
		super(pipeline, conf);
	}



	@Override
	public Population produce(VariationManager vm) throws BadConfiguration {
		if (drainPipes()) {
			Population received = (Population) _received.values().toArray()[0];
			return received;
		} else {
			throw new BadConfiguration("Unable to assign terminal population.");
		}
	}

}
