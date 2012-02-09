package evoman.ec.gp.mutation;


import java.util.*;

import evoict.*;
import evoman.ec.evolution.*;
import evoman.ec.gp.*;
import evoman.ec.gp.find.*;
import evoman.ec.gp.init.*;
import evoman.evo.*;
import evoman.evo.pop.*;



@EvolutionDescriptor(name = "ReplaceSubtreeVarDepth", min_in = 1, max_in = 1, reptype = GPTree.class)
public class ReplaceSubtreeVarDepth extends EvolutionOperator {

	private static final long	serialVersionUID	= 1L;



	public static void validate(EvolutionOpConfig conf) throws BadConfiguration {
		BadConfiguration bc = new BadConfiguration();
		if (!conf.validate("max_depth", Double.class)) {
			conf.set("max_depth", 1.15);
		}
		if (!conf.validate("min_depth", Double.class)) {
			conf.set("min_depth", 0.0);
		}
		if (conf.D("max_depth") < conf.D("min_depth")) {
			bc.append("Maximum average depth < Minimum average depth");

		}
		if (!conf.validate("prob", Double.class) || conf.D("prob") < 0.0 || conf.D("prob") > 1.0) {
			bc.append("prob not set or is not in range (0.0,1.0)");
		}
		if (!conf.validate("tries", Integer.class) || conf.I("tries") < 0) {
			conf.set("tries", 10);
		}
		bc.validate();
	}



	public ReplaceSubtreeVarDepth(EvolutionPipeline pipeline, EvolutionOpConfig conf) {
		super(pipeline, conf);
	}



	@Override
	public Population produce() throws BadConfiguration {
		if (drainPipes()) {
			if (_received.size() == 1) {
				Population received = (Population) _received.values().toArray()[0];
				Population newpop = received.clone();
				return doMutation(newpop);
			} else {
				throw new BadConfiguration(getConfig().getName() + " expected 1 population, got " + _received.size());
			}

		} else {
			throw new BadConfiguration("Unable to process population for operator " + getConfig().getName());
		}
	}



	protected Population doMutation(Population pop) throws BadConfiguration {

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
				throw new BadConfiguration("Unable to generate replacement subtree in " + getConfig().getName());
			}
			if (selection == t.getRoot()) {
				t.reRoot(replacement);
			} else {
				if (!selection.getParent().swap(selection, replacement)) {
					throw new BadConfiguration("Unable to replace subtree in " + getConfig().getName());
				}
			}
			Genotype new_gen = _pipeline.makeGenotype(t);
			pop.placeGenotype(new_gen, parent);
		}

		return pop;
	}



	protected GPNode makeSubtree(GPTree t, GPNode selection) throws BadConfiguration {

		// Find the allowed depths
		double min_change = getConfig().D("min_depth");
		double max_change = getConfig().D("max_depth");
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
			throw new BadConfiguration("Cannot create root of replacement subtree.");
		} else {
			Stack<GPNode> populate = new Stack<GPNode>();
			populate.push(root);
			while (!populate.isEmpty()) {
				GPNode cur = populate.pop();
				int pos = 0;
				for (Class<?> cl : cur.getConfig().getConstraints().getChildTypes()) {
					GPNode child = t.createNode(cur, cl, cur.getPosition().newPos(pos), init);
					cur.getChildren().add(child);
					pos++;
					if (child == null) {
						throw new BadConfiguration("Problem instantiating non-root element in subtree.");
					}
					populate.push(child);
				}
			}
			return root;
		}
	}
}
