package evoman.ec.evolution;


import evoman.evo.pop.*;



@EvolutionDescriptor(name = "Terminal", min_in = 1, max_in = 1)
public class TerminalOperator extends EvolutionOperator {

	private static final long	serialVersionUID	= 1L;



	public TerminalOperator(EvolutionPipeline pipeline, EvolutionOpConfig conf) {
		super(pipeline, conf);
	}



	@Override
	public Population produce() {
		if (drainPipes()) {
			return (Population) _received.values().toArray()[0];
		} else {
			_pipeline.getNotifier().fatal("Unable to assign terminal population.");
			return null;
		}
	}

}
