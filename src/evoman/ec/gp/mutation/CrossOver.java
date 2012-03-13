package evoman.ec.gp.mutation;


import java.util.*;

import evoict.*;
import evoman.config.*;
import evoman.ec.evolution.*;
import evoman.ec.gp.*;
import evoman.ec.gp.find.*;
import evoman.evo.pop.*;



/**
 * Cross over take a single population and, with probability "prob" selects a
 * genotype for cross over. A cross-over point is then selected in the base
 * gentype for replacement, with "prob_leaf" probability of it being a leaf
 * node. Next, a cross-over candidate genotype is selected (or "max_tries" are
 * attempted to find a candidate) with a comptable subtree/node for the
 * replacement. If a candidate is successfully identified, a new tree is created
 * from the cross of the original genotype and the cross-over candidate. This
 * genotype is then randomly placed in the population.
 * 
 * 
 * Parameters
 * 
 * prob
 * probability of selecting a genotype for crossover base
 * 
 * max_tries
 * the number of tries for identifying successful candidates before abandoning
 * 
 * prob_leaf
 * the probability that a leaf is selected for the crossover point.
 * 
 * 
 * @author ruppmatt
 * 
 */
@ConfigRegister(name = "GPCrossOver")
@ConfigProxy(proxy_for = EvolutionOpConfig.class)
@EvolutionDescriptor(name = "GPCrossOver", min_in = 1, max_in = 1, reptype = GPTree.class)
public class CrossOver extends EvolutionOperator {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;



	public static void validate(EvolutionOpConfig conf) throws BadConfiguration {
		BadConfiguration bc = new BadConfiguration();
		if (!conf.validate("prob", Double.class) || conf.D("prob") < 0.0 || conf.D("prob") > 1.0) {
			bc.append(conf.getName() + ": prob is either not set or out of range");
		}
		if (!conf.validate("max_tries", Integer.class) || conf.I("max_tries") < 1) {
			bc.append(conf.getName() + ": max_tries is either not set or out of range");
		}
		if (!conf.validate("prob_leaf", Double.class) || conf.D("prob_leaf") < 0.0 || conf.D("prob_leaf") > 1.0) {
			bc.append(conf.getName() + ": prob_leaf is either not set or out of range");
		}
		bc.validate();
	}



	public CrossOver(EvolutionPipeline pipeline, EvolutionOpConfig conf) {
		super(pipeline, conf);
	}



	@Override
	public Population produce() throws BadConfiguration {
		if (drainPipes()) {
			if (_received.size() != 1) {
				throw new BadConfiguration(getConfig().getName() + ": expected 1 input population, received "
						+ _received.size());
			}
			Population p = (Population) ((Population) _received.values().toArray()[0]).clone();
			return doMutation(p);
		} else {
			throw new BadConfiguration(getConfig().getName() + ": no populations received.");
		}
	}



	protected Population doMutation(Population p) {
		int psize = p.size();
		double prob = getConfig().D("prob");
		int max_tries = getConfig().I("max_tries");
		int num_xover = _pipeline.getRandom().getBinomial(psize, prob);

		for (int k = 0; k < num_xover; k++) {
			int tries = 0;
			while (tries < max_tries) {
				tries++;
				GPTree base_original = findRandomTree(p);
				GPTree base_clone = (GPTree) base_original.clone();
				GPNode replaced = findNodeToBeReplace(base_clone);
				if (replaced == null) {
					continue;
				}

				GPTree subtree_original = findRandomTree(p);
				GPNode inserted_original = findNodeToInsert(subtree_original, replaced);
				if (inserted_original == null) {
					continue;
				}
				GPNode inserted_clone = inserted_original.clone(base_clone, replaced.getParent());
				if (replaced.getParent() == null) {
					base_clone.reRoot(inserted_clone);
				} else {
					replaced.getParent().swap(replaced, inserted_clone);
				}
				// if (GPTreeUtil.maxDepth(base_clone.getRoot()) >
				// base_clone.getConfig().getMaxDepth()) {
				// System.err.println("Tree height error in CrossOver.");
				// }
				Genotype new_gen = _pipeline.makeGenotype(base_clone);
				p.placeGenotype(new_gen, _pipeline);
				break;
			}
		}
		return p;
	}



	protected GPTree findRandomTree(Population p) {
		int ndx = _pipeline.getRandom().nextInt(p.size());
		Genotype g = p.getGenotype(ndx);
		return (GPTree) g.rep();
	}



	protected GPNode findNodeToBeReplace(GPTree t) {
		int max_tries = getConfig().I("max_tries");
		double prob_leaf = getConfig().D("prob_leaf");
		ArrayList<GPNode> candidates = new ArrayList<GPNode>();
		int tries = 0;
		do {
			if (prob_leaf <= _pipeline.getRandom().nextDouble()) {
				FindLeaves f = new FindLeaves();
				t.bfs(f);
				candidates = f.collect();
			} else {
				FindInternalNodes f = new FindInternalNodes();
				t.bfs(f);
				candidates = f.collect();
			}
			tries++;
		} while (tries < max_tries && candidates.isEmpty());

		if (candidates.isEmpty()) {
			return null;
		}

		GPNode target = null;
		int num_targets = candidates.size();
		tries = 0;
		do {
			int ndx = _pipeline.getRandom().nextInt(num_targets);
			target = candidates.get(ndx);
			tries++;
		} while (!t.canAlter(target) && tries < max_tries);

		if (!t.canAlter(target)) {
			return null;
		} else {
			return target;
		}
	}



	protected GPNode findNodeToInsert(GPTree t, GPNode to_replace) {
		Class<?> ret_type = to_replace.getConfig().getConstraints().getReturnType();
		FindReturnType find = new FindReturnType(ret_type);
		t.bfs(find);

		ArrayList<GPNode> candidates = find.collect();
		ArrayList<GPNode> good_size = new ArrayList<GPNode>();
		int replacement_depth = to_replace.getDepth();
		for (GPNode n : candidates) {
			int max_depth = GPTreeUtil.maxSubtreeDepth(n);
			if (max_depth + replacement_depth <= to_replace.getTree().getConfig().getMaxDepth()) {
				good_size.add(n);
			}
		}
		if (good_size.isEmpty()) {
			return null;
		} else {
			int use_ndx = _pipeline.getRandom().nextInt(good_size.size());
			return good_size.get(use_ndx);
		}
	}
}
