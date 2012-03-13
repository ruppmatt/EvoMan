package evoman.ec.evolution.operators;


import evoict.*;
import evoman.ec.evolution.*;
import evoman.evo.pop.*;



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
	public Population produce() throws BadConfiguration {
		if (drainPipes()) {
			Population received = (Population) _received.values().toArray()[0];
			if (received.size() != _pipeline.getVM().getPopSize()) {
				throw new BadConfiguration(
						"Final population of pipeline does not equal constraint set by variation manager.");
			}
			return received;
		} else {
			_pipeline.getNotifier().fatal("Unable to assign terminal population.");
			return null;
		}
	}

}
