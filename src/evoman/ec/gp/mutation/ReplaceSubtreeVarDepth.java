package evoman.ec.gp.mutation;


import java.util.*;

import evoman.ec.gp.*;
import evoman.ec.gp.find.*;
import evoman.ec.gp.init.*;
import evoman.ec.mutation.*;
import evoman.evo.pop.*;



@EvolutionDescriptor(name = "ReplaceSubtreeVarDepth", min_in = 1, max_in = 1, reptype = GPTree.class)
public class ReplaceSubtreeVarDepth extends EvolutionOperator {

	private static final long	serialVersionUID	= 1L;



	public static boolean validate(EvolutionOpConfig conf, String msg) {
		if (!conf.validate("max_avg_depth_change", Double.class)) {
			conf.set("max_depth_change", 1.15);
		} else if (!conf.validate("min_avg_depth_change", Double.class)) {
			conf.set("min_depth_change", 0.0);
		}
		if (conf.D("max_avg_depth_change") < conf.D("min_avg_depth_change")) {
			msg = "Maximum average depth < Minimum average depth";
			return false;
		}
		if (!conf.validate("prob", Double.class) || conf.D("prob") < 0.0 || conf.D("prob") > 1.0) {
			msg = "prob not set or is not in range (0.0,1.0)";
			return false;
		}
		if (!conf.validate("tries", Integer.class) || conf.I("tries") < 0) {
			conf.set("tries", 10);
		}
		return true;
	}



	public ReplaceSubtreeVarDepth(EvolutionPipeline pipeline, EvolutionOpConfig conf) {
		super(pipeline, conf);
	}



	@Override
	public Population produce() {
		if (drainPipes()) {
			if (_received.size() == 1) {
				Population received = (Population) _received.values().toArray()[0];
				Population newpop = received.clone();
				return doMutation(newpop);
			} else {
				_pipeline.getNotifier()
						.fatal(getConfig().getName() + " expected 1 population, got " + _received.size());
			}

		} else {
			_pipeline.getNotifier().fatal("Unable to process population for operator " + getConfig().getName());
		}
		return null;
	}



	protected Population doMutation(Population pop) {

		double p = getConfig().D("prob");
		int popsize = pop.size();
		int n = _pipeline.getRandom().getBinomial(popsize, p);

		for (int k = 0; k < n; k++) {
			int gen_ndx = _pipeline.getRandom().nextInt(popsize);
			Genotype parent = pop.getGenotype(gen_ndx);
			GPTree t = (GPTree) parent.rep().clone();
			int node_ndx = _pipeline.getRandom().nextInt(t.getSize());
			FindByIndex finder = new FindByIndex(node_ndx);
			t.bfs(finder);
			GPNode selection = finder.collect().get(0);
			GPNode replacement = makeSubtree(t, selection);
			if (replacement == null) {
				_pipeline.getNotifier().fatal("Unable to generate replacement subtree in " + getConfig().getName());
			}
			if (selection == t.getRoot()) {
				t.reRoot(replacement);
			} else {
				if (!selection.getParent().swap(selection, replacement)) {
					_pipeline.getNotifier().fatal("Unable to replace subtree in " + getConfig().getName());
				}
			}
			Genotype new_gen = _pipeline.makeGenotype(t);
			pop.placeGenotype(new_gen, parent);
		}

		return pop;
	}



	protected GPNode makeSubtree(GPTree t, GPNode selection) {

		// Find the allowed depths
		double min_change = getConfig().D("min_avg_depth_change");
		double max_change = getConfig().D("max_avg_depth_change");
		FindLeaves leaves = new FindLeaves();
		t.bfs(leaves);
		double sum_depth = 0;
		for (GPNode leaf : leaves.collect()) {
			sum_depth += leaf.getDepth();
		}
		double avg_depth = (sum_depth == 0.0) ? 0.0 : sum_depth / leaves.collect().size();
		int min_avg_depth = (int) Math.floor(min_change * avg_depth);
		int max_avg_depth = (int) Math.ceil(max_change * avg_depth);
		if (min_avg_depth < 1) {
			min_avg_depth = 1;
		}

		GPInitConfig conf = new GPInitConfig();
		conf.set("min_depth", min_avg_depth);
		conf.set("max_depth", max_avg_depth);
		String msg = null;
		if (!GPVarDepth.validate(conf, msg)) {
			_pipeline.getNotifier().warn(msg);
			return null;
		}
		GPVarDepth init = new GPVarDepth(_pipeline.getESParent(), conf);

		GPNode root = t.createNode(selection.getParent(),
				selection.getConfig().getConstraints().getReturnType(),
				(GPNodePos) selection.getPosition().clone(), init);
		if (root == null) {
			_pipeline.getNotifier().warn("Cannot create root of replacement subtree.");
			return null;
		} else {
			Stack<GPNode> populate = new Stack<GPNode>();
			populate.push(root);
			while (!populate.isEmpty()) {
				GPNode cur = populate.pop();
				int pos = 0;
				for (Class<?> cl : cur.getConfig().getConstraints().getChildTypes()) {
					GPNode child = t.createNode(cur, cl, cur.getPosition().newPos(pos), init);
					pos++;
					if (child == null) {
						_pipeline.getNotifier().warn("Problem instantiating non-root element in subtree.");
						return null;
					}
					populate.push(child);
				}
			}
			return root;
		}
	}
}
