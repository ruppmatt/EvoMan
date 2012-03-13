package evoman.ec.gp.mutation;


import evoict.*;
import evoman.config.*;
import evoman.ec.evolution.*;
import evoman.ec.gp.*;
import evoman.ec.gp.find.*;
import evoman.evo.pop.*;



/**
 * Node mutation takes a single population and, with probability "prob" select a
 * genotype to receive a node mutation. Node mutations occur only in
 * GPutableNodes and do not change the return type of the node or any child
 * types.
 * 
 * 
 * Paramters
 * 
 * prob
 * The probabilty of selecting a tree for a node mutation.
 * 
 * @author ruppmatt
 * 
 */

@ConfigRegister(name = "NodeMutation")
@ConfigProxy(proxy_for = GPNodeConfig.class)
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
				Population p = (Population) ((Population) _received.values().toArray()[0]).clone();
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
		Double prob = getConfig().D("prob"); // The probability of mutation

		/**
		 * For all genotypes in the tree, collect the ones that are mutable.
		 * Even if the genotype is mutable, check to see if there are
		 * other restrictions placed on it by the tree (e.g. if it is the
		 * root of a fixed-root tree, it can't mutate). Mutate the node
		 * randomly with a probably set by "prob".
		 */
		for (Genotype g : p.getGenotypes()) {
			GPTree t = (GPTree) g.rep();
			FindMutables finder = new FindMutables();
			t.bfs(finder);
			for (GPNode n : finder.collect()) {
				if (!t.canAlter(n))
					continue;
				if (_pipeline.getRandom().nextDouble() <= prob) {
					((GPMutableNode) n).mutate();
				}
			}
			// if (GPTreeUtil.maxDepth(t.getRoot()) >
			// t.getConfig().getMaxDepth()) {
			// System.err.println("Tree height error in NodeMutation.");
			// }
		}
		return p;
	}
}
