package evoman.ec.gp.mutation;


import evoict.*;
import evoman.config.*;
import evoman.ec.evolution.*;
import evoman.ec.gp.*;
import evoman.ec.gp.find.*;
import evoman.ec.gp.init.*;
import evoman.evo.pop.*;
import evoman.evo.structs.*;
import evoman.evo.vm.*;



/**
 * 
 * Replace Subtree Variable Depth creates a new subtree (of variable depth) at a
 * node. It is like cross-over but the replaced subtree is created from scratch
 * rather than being received from another genotype.
 * 
 * 
 * max_depth
 * maximum (by ratio) size of the new subtree relative to the old
 * 
 * min_depth
 * minimum (by ratio) size of new tree relative to old subtree
 * 
 * prob
 * probability of selecting a tree for the replacement
 * 
 * max_tries
 * maximum attempts at either finding a candidate for subtree replacement within
 * a selected tree
 * 
 * 
 * @author ruppmatt
 * 
 */

@ConfigProxy(proxy_for = EvolutionOpConfig.class)
@ConfigRegister(name = "GPReplaceSubtree")
@EvolutionDescriptor(name = "ReplaceSubtreeVarDepth", min_in = 1, max_in = 1, reptype = GPTree.class)
public class ReplaceSubtreeVarDepth extends EvolutionOperator {

	private static final long	serialVersionUID	= 1L;



	public static void validate(EvolutionOpConfig conf) throws BadConfiguration {
		BadConfiguration bc = new BadConfiguration();
		if (!conf.validate("max_depth", Double.class)) {
			bc.append("ReplaceSubtreeVarDepth: max_depth is not set.");
		}
		if (!conf.validate("min_depth", Double.class)) {
			bc.append("ReplaceSubtreeVarDepth: min_depth is not set.");
		}
		bc.validate();
		if (conf.D("max_depth") < conf.D("min_depth")) {
			bc.append("ReplaceSubtreeVarDepth: Maximum average depth < Minimum average depth");
		}
		if (!conf.validate("prob", Double.class) || conf.D("prob") < 0.0 || conf.D("prob") > 1.0) {
			bc.append("ReplaceSubtreeVarDepth: prob not set or is not in range (0.0,1.0)");
		}
		if (!conf.validate("max_tries", Integer.class) || conf.I("max_tries") < 0) {
			bc.append("ReplaceSubtreeVarDepth: max_tries is either not set or invalid.");
		}
		bc.validate();
	}



	public ReplaceSubtreeVarDepth(EvolutionPipeline pipeline, EvolutionOpConfig conf) {
		super(pipeline, conf);
	}



	@Override
	public Population produce(VariationManager vm) throws BadConfiguration {
		if (drainPipes()) {
			if (_received.size() == 1) {
				Population received = (Population) _received.values().toArray()[0];
				Population newpop = (Population) received.clone();
				return doMutation(newpop, vm);
			} else {
				throw new BadConfiguration(getConfig().getName() + " expected 1 population, got " + _received.size());
			}

		} else {
			throw new BadConfiguration("Unable to process population for operator " + getConfig().getName());
		}
	}



	/**
	 * 
	 * @param pop
	 * @return a mutated population
	 * @throws BadConfiguration
	 */
	protected Population doMutation(Population pop, VariationManager vm) throws BadConfiguration {

		// Find the number of trees to have sub-tree replacements
		double p = getConfig().D("prob");
		int popsize = pop.size();
		int n = vm.getRandom().getBinomial(popsize, p);

		/*
		 * Find n GP trees that need to have a subtree replaced. Once a random
		 * tree is picked, clone it. Then find a random node in the tree to
		 * serve as the replacement. This selected node needs to be alterable.
		 */
		for (int k = 0; k < n; k++) {
			int gen_ndx = vm.getRandom().nextInt(popsize);
			Genotype parent = pop.getGenotype(gen_ndx);
			GPTree t = (GPTree) parent.rep().clone();
			GPNode selection = null;
			int tries = 0;
			int max_tries = getConfig().I("max_tries");
			do {
				int node_ndx = vm.getRandom().nextInt(t.getSize());
				FindByIndex finder = new FindByIndex(node_ndx);
				t.bfs(finder);
				selection = finder.collect().get(0);
				tries++;
			} while (selection == null || !t.canAlter(selection) || tries < max_tries);

			// If we can't find a node to do the replacement, skip this and go
			// to the next
			if (selection == null || !t.canAlter(selection)) {
				continue;
			}

			// Construct a new subtree to replace the current one
			try {
				GPNode replacement = makeSubtree(t, selection, vm);

				// Try to reRoot the tree if necessary or swap the selection
				// with
				// the new replacement subtree
				if (selection == t.getRoot()) {
					t.reRoot(replacement);
				} else {
					if (!selection.getParent().swap(selection, replacement)) {
						throw new BadConfiguration("Unable to replace subtree in " + getConfig().getName());
					}
				}

				// if (GPTreeUtil.maxDepth(t.getRoot()) >
				// t.getConfig().getMaxDepth()) {
				// System.err.println("Tree height error in RepalceSubtreeVarDepth.");
				// }
				// Construct a new genotype for the replacement tree and place
				// it in
				// the population
				Genotype new_gen = vm.makeGenotype(t);
				pop.placeGenotype(new_gen, vm, parent);

			} catch (BadConfiguration bc) {
				bc.prepend("Unable to generate replacement subtree in " + getConfig().getName());
				throw bc;
			}

		}

		return pop;
	}



	/**
	 * Make a new subtree based on a node that is to be replaced.
	 * 
	 * @param t
	 *            The tree to place the new subtree
	 * @param selection
	 *            The node to be replaced
	 * @return
	 *         The root of the new subtree
	 * @throws BadConfiguration
	 */
	protected GPNode makeSubtree(GPTree t, GPNode selection, EMState state) throws BadConfiguration {

		// Find the allowed depths
		// Min and max depth are based on multiples of the average depth of the
		// selected subtree
		double min_change = getConfig().D("min_depth");
		double max_change = getConfig().D("max_depth");

		double avg_depth = GPTreeUtil.averageDepth(selection);
		if (avg_depth == Double.NaN)
			return selection; // Can't make any changes

		int min_avg_depth = (int) Math.floor(min_change * avg_depth);
		int max_avg_depth = (int) Math.ceil(max_change * avg_depth);
		if (min_avg_depth < 1) {
			min_avg_depth = 1;
		}

		// Set up a new tree initializer
		GPVarDepth init = new GPVarDepth();
		init.set("min_depth", min_avg_depth);
		init.set("max_depth", max_avg_depth);
		try {
			init.validate();
		} catch (BadConfiguration bc) {
			throw bc;
		}

		// Create the root of the new tree.
		try {
			GPTreeConfig subtree_config = (GPTreeConfig) t.getConfig().clone();
			subtree_config.set("return_type", selection.getConfig().getConstraints().getReturnType());
			subtree_config.set("max_depth", 1 + t.getConfig().getMaxDepth() - selection.getPosition().getDepth());
			GPTree subtree = new GPTree(state, subtree_config, init);
			return subtree.getRoot();
		} catch (BadConfiguration bc) {
			bc.prepend("ReplaceSubtreeVarDepth: Cannot create root of replacement subtree.");
			throw bc;
		}

	}
}
