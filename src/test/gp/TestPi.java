package test.gp;


import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import evoman.ec.gp.*;
import evoman.ec.gp.init.*;
import evoman.ec.gp.mutation.*;
import evoman.ec.gp.nonterminals.*;
import evoman.ec.gp.terminals.*;
import evoman.ec.mutation.*;
import evoman.evo.*;
import evoman.evo.pop.*;
import evoman.evo.structs.*;
import evoman.evo.vm.*;



public class TestPi {

	@Test
	public void test() {

		EvoPool root = new EvoPool("root", null);

		VariationManagerConfig vm_conf = new VariationManagerConfig();

		try {
			GPVariationManager.validate(vm_conf);
			fail("Variation managers shouldn't be valid");
		} catch (BadConfiguration bc) {
		}

		vm_conf.set("pop_size", 1000);

		try {
			GPVariationManager.validate(vm_conf);
		} catch (BadConfiguration bc) {
			fail("Variation manager should be valid.");
		}

		GPVariationManager vm = new GPVariationManager(root, vm_conf);

		GPNodeDirectory dir = new GPNodeDirectory(root);
		GPNodeConfig gp_add = new GPNodeConfig(GPAdd.class);
		GPNodeConfig gp_sub = new GPNodeConfig(GPSubtract.class);
		GPNodeConfig gp_mult = new GPNodeConfig(GPMultiply.class);
		GPNodeConfig gp_div = new GPNodeConfig(GPDivide.class);
		GPNodeConfig gp_max = new GPNodeConfig(GPMax.class);
		GPNodeConfig gp_min = new GPNodeConfig(GPMin.class);
		GPNodeConfig gp_grt = new GPNodeConfig(GPGreaterThan.class);
		GPNodeConfig gp_lss = new GPNodeConfig(GPLessThan.class);
		GPNodeConfig gp_cnd = new GPNodeConfig(GPConditional.class);
		GPNodeConfig gp_erc = new GPNodeConfig(GPDoubleERC.class);
		GPNodeConfig gp_berc = new GPNodeConfig(GPBooleanERC.class);
		gp_erc.set("max", 100.0);
		gp_erc.set("min", -100.0);

		try {
			dir.addNodeConfig(gp_add);
			dir.addNodeConfig(gp_sub);
			dir.addNodeConfig(gp_mult);
			dir.addNodeConfig(gp_div);
			dir.addNodeConfig(gp_max);
			dir.addNodeConfig(gp_min);
			dir.addNodeConfig(gp_grt);
			dir.addNodeConfig(gp_lss);
			dir.addNodeConfig(gp_cnd);
			dir.addNodeConfig(gp_erc);
			dir.addNodeConfig(gp_berc);
		} catch (BadConfiguration bc) {
			System.err.println(bc.getMessage());
		}

		GPInitConfig tree_init_conf = new GPInitConfig();
		tree_init_conf.set("depth", 5);
		GPFullTree init = new GPFullTree(root, tree_init_conf);

		GPTreeConfig tree_conf = new GPTreeConfig(dir, init);
		tree_conf.set("max_depth", 2);
		tree_conf.set("return_type", Double.class);
		vm.setTreeConfig(tree_conf);

		EvolutionPipeline ep = new EvolutionPipeline(vm);
		EvolutionOpConfig elitism = new EvolutionOpConfig("Elitism", ElitistSelection.class);
		elitism.set("num", 100);
		EvolutionOpConfig tour_sel = new EvolutionOpConfig("TournamentSelect", TournamentSelection.class);
		tour_sel.set("tour_size", 3);
		EvolutionOpConfig xover = new EvolutionOpConfig("CrossOver", CrossOver.class);
		xover.set("prob", 0.10);
		xover.set("prob_leaf", 0.10);
		xover.set("sample_trials", 10);
		EvolutionOpConfig pointmut = new EvolutionOpConfig("NodeMutation", NodeMutation.class);
		pointmut.set("prob", 0.05);
		EvolutionOpConfig subtreerep = new EvolutionOpConfig("SubtreeReplace", ReplaceSubtreeVarDepth.class);
		subtreerep.set("prob", 0.10);
		EvolutionOpConfig merge = new EvolutionOpConfig("Merge", WeightedMergeOperator.class);
		EvolutionOpConfig replace = new EvolutionOpConfig("Replace", ReplaceOperator.class);
		replace.set("background", "Merge");
		replace.set("replacement", "Elitism");
		try {
			ep.addOperator(elitism);
			ep.addOperator(tour_sel);
			ep.addOperator(xover);
			ep.addOperator(pointmut);
			ep.addOperator(subtreerep);
			ep.addOperator(merge);
			ep.addOperator(replace);
			ep.createPipe(tour_sel, xover);
			ep.createPipe(tour_sel, pointmut);
			ep.createPipe(pointmut, subtreerep);
			ep.createPipe(subtreerep, merge);
			ep.createPipe(xover, merge);
			ep.createPipe(elitism, replace);
			ep.createPipe(merge, replace);
		} catch (BadConfiguration e) {
			fail(e.getMessage());
		}

		root.getVM().addEP(ep);
		root.init();

		ArrayList<String> results = new ArrayList<String>();
		for (int k = 0; k < 500; k++) {
			double avg_w = 0.0;
			double avg_pi = 0.0;
			double best = 0.0;
			for (Genotype g : root.getPopulation().getGenotypes()) {
				double v = (Double) g.rep().eval(null);
				double w = Math.pow(v - Math.PI, -2);
				g.setFitness(w);
				avg_w += w / root.getPopulation().size();
				avg_pi += v / root.getPopulation().size();
				best = (Math.abs(Math.PI - v) < Math.abs(Math.PI - best)) ? v : best;
			}
			results.add(Integer.toString(k) + "\t" + Double.toString(avg_w) + "\t" + Double.toString(avg_pi) + "\t"
					+ Double.toString(best));
			root.getVM().evolve();
		}

		System.err.println("Done evaluating.  Pop size =" + root.getPopulation().size());

		for (int k = 0; k < results.size(); k++) {
			System.err.println(results.get(k));
		}
		int count = 0;
		for (Genotype g : root.getPopulation().getGenotypes()) {
			try {
				g.rep().eval(null);
			} catch (Exception e) {
				count++;
			}
		}

		System.err.println("Invalid genotypes: " + count);

	}
}
