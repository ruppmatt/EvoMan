package evoman.ec.gp.mutation;


import evoman.ec.gp.*;
import evoman.ec.gp.find.*;
import evoman.ec.mutation.*;
import evoman.evo.pop.*;



@EvolutionDescriptor(name = "RandomizeSubtree", min_in = 1, max_in = 1, reptype = GPTree.class)
public class RandomizeSubtree extends EvolutionOperator {

	private static final long	serialVersionUID	= 1L;



	public static boolean validate(GPNodeConfig conf, String msg) {
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
		return true;
	}



	public RandomizeSubtree(EvolutionPipeline pipeline, EvolutionOpConfig conf) {
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
		double min_change = getConfig().D("min_avg_depth_change");
		double max_change = getConfig().D("max_avg_depth_change");
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
			Class<?> type = selection.getConfig().getConstraints().getReturnType();
			GPNodeDirectory dir = t.getNodeDirectory();
			GPNode replacement = makeSubtree(type);
			if (selection == t.getRoot()) {
				t.reRoot(replacement);
			} else {
				if (!selection.getParent().swap(selection, replacement)) {
					_pipeline.getNotifier().fatal("Unable to replace subtree in " + getConfig().getName());
				}
			}
			Genotype new_gen = _pipeline.makeGenotype(t);
			int ndx_replace = _pipeline.getRandom().nextInt(popsize);
			pop.placeGenotype(new_gen, parent);
		}

		return pop;
	}
}
