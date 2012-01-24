package evoman.ec.gp.mutation;


import java.util.*;

import evoman.ec.gp.*;
import evoman.ec.gp.find.*;
import evoman.ec.mutation.*;
import evoman.evo.*;
import evoman.evo.pop.*;



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
		if (!conf.validate("sample_trials", Integer.class) || conf.I("sample_trials") < 1) {
			bc.append(conf.getName() + ": sample_trials is either not set or out of range");
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
			Population p = ((Population) _received.values().toArray()[0]).clone();
			return doMutation(p);
		} else {
			throw new BadConfiguration(getConfig().getName() + ": no populations received.");
		}
	}



	protected Population doMutation(Population p) {
		System.err.println("CrossOver: doMutation entered");
		int psize = p.size();
		double prob = getConfig().D("prob");
		int samples = getConfig().I("sample_trials");
		double prob_leaf = getConfig().D("prob_leaf");

		int num_xover = _pipeline.getRandom().getBinomial(psize, prob);

		for (int k = 0; k < num_xover; k++) {

			// Find our first parent tree
			int ndx1 = _pipeline.getRandom().nextInt(psize);
			Genotype g1 = p.getGenotype(ndx1);
			GPTree t1 = (GPTree) g1.rep();
			GPTree nt = (GPTree) t1.clone();

			// Find our cross-over point
			ArrayList<GPNode> targets = new ArrayList<GPNode>();
			int trials = 0;

			while (targets.size() == 0 && trials < samples) {
				if (prob_leaf <= _pipeline.getRandom().nextDouble()) {
					FindLeaves f = new FindLeaves();
					nt.bfs(f);
					targets = f.collect();
				} else {
					FindInternalNodes f = new FindInternalNodes();
					nt.bfs(f);
					targets = f.collect();
				}
				trials++;
			}
			if (targets.size() == 0) {
				continue;
			}
			System.err.println("Exted first trial loop");
			int num_targets = targets.size();
			GPNode xover = targets.get(_pipeline.getRandom().nextInt(num_targets));

			targets = new ArrayList<GPNode>();
			Genotype g2 = null;

			while (targets.size() == 0 && trials < samples) {
				int ndx2 = _pipeline.getRandom().nextInt(psize);
				g2 = p.getGenotype(ndx2);
				GPTree t2 = (GPTree) g2.rep();
				FindReturnType find = new FindReturnType(xover.getConfig().getConstraints().getReturnType());
				t2.bfs(find);
				targets = find.collect();
				trials++;
			}

			if (targets.size() == 0) {
				continue;
			}
			GPNode xover2 = targets.get(_pipeline.getRandom().nextInt(targets.size())).clone(nt,
					xover.getParent());
			if (xover.getParent() == null) {
				nt.reRoot(xover2);
			} else {
				GPNode xover_parent = xover.getParent();
				xover_parent.swap(xover, xover2);
			}
			Genotype ng = _pipeline.makeGenotype(nt);
			p.placeGenotype(ng, g1, g2);
		}

		return p;
	}
}
