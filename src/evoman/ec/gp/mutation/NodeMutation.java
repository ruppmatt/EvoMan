package evoman.ec.gp.mutation;


import evoict.*;
import evoman.ec.evolution.*;
import evoman.ec.gp.*;
import evoman.ec.gp.find.*;
import evoman.evo.*;
import evoman.evo.pop.*;



@EvolutionDescriptor(name = "GPNodeMutation", min_in = 1, max_in = 1, reptype = GPTree.class)
public class NodeMutation extends EvolutionOperator {

	private static final long	serialVersionUID	= 1L;



	public static void validate(EvolutionOpConfig conf) throws BadConfiguration {
		if (!conf.validate("prob", Double.class) || conf.D("prob") < 0.0 || conf.D("prob") > 1.0) {
			throw new BadConfiguration("NodeMutation: missing or invalid mutation probability.");
		}
	}



	public NodeMutation(EvolutionPipeline pipeline, EvolutionOpConfig conf) {
		super(pipeline, conf);
	}



	@Override
	public Population produce() throws BadConfiguration {
		if (drainPipes()) {
			if (_received.size() != 1) {
				throw new BadConfiguration(getConfig().getName() + " expected 1 input population, received "
						+ _received.size());
			} else {
				Population p = ((Population) _received.values().toArray()[0]).clone();
				Population retval = doMutation(p);
				if (retval == null) {
					throw new BadConfiguration(getConfig().getName() + ": unable to perform mutations");
				} else {
					return retval;
				}
			}
		} else {
			throw new BadConfiguration(getConfig().getName() + ": unable to receive population.");
		}
	}



	protected Population doMutation(Population p) {
		Double prob = getConfig().D("prob");
		for (Genotype g : p.getGenotypes()) {
			GPTree t = (GPTree) g.rep();
			FindMutables finder = new FindMutables();
			t.bfs(finder);
			for (GPNode n : finder.collect()) {
				if (_pipeline.getRandom().nextDouble() <= prob) {
					((GPMutableNode) n).mutate();
				}
			}
		}
		return p;
	}
}
