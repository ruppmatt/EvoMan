package test.gp;


import static org.junit.Assert.*;

import org.junit.*;

import evoict.*;
import evoman.ec.*;
import evoman.ec.evolution.*;
import evoman.ec.evolution.operators.*;
import evoman.ec.gp.*;
import evoman.ec.gp.init.*;
import evoman.ec.gp.mutation.*;
import evoman.ec.gp.nonterminals.*;
import evoman.ec.gp.terminals.*;
import evoman.evo.pop.*;
import evoman.evo.structs.*;



public class TestPi {

	@Test
	public void test() {

		// Configuration-time
		// =====================================================================
		EvoPool root = new EvoPool("root", null);

		GPVariationManager vm = new GPVariationManager(root);
		vm.set("pop_size", 1000);

		GPFullTree init = new GPFullTree();
		init.set("max_depth", 5);

		GPTreeConfig tree_conf = new GPTreeConfig(GPTree.class);
		tree_conf.set("max_depth", 10);
		tree_conf.set("return_type", Double.class);

		GPNodeDirectory dir = new GPNodeDirectory();
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
		gp_erc.set("max", 100.0);
		gp_erc.set("min", -100.0);
		GPNodeConfig gp_berc = new GPNodeConfig(GPBooleanERC.class);
		// GPNodeConfig gp_const = new GPNodeConfig(GPNodeDoubleConst.class);
		// gp_const.set("value", Math.PI);

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
		// dir.addNodeConfig(gp_const);

		tree_conf.setNodeDirectory(dir);

		EvolutionPipeline ep = new EvolutionPipeline(vm);

		EvolutionOpConfig elitism = new EvolutionOpConfig("Elitism",
				ElitistSelection.class);
		elitism.set("num", 50);
		EvolutionOpConfig tour_sel = new
				EvolutionOpConfig("TournamentSelect", TournamentSelection.class);
		tour_sel.set("tour_size", 3);
		EvolutionOpConfig xover = new EvolutionOpConfig("CrossOver",
				CrossOver.class);
		xover.set("prob", 0.10);
		xover.set("prob_leaf", 0.10);
		xover.set("max_tries", 10);
		EvolutionOpConfig pointmut = new EvolutionOpConfig("NodeMutation",
				NodeMutation.class);
		pointmut.set("prob", 0.20);
		EvolutionOpConfig subtreerep = new
				EvolutionOpConfig("SubtreeReplace", ReplaceSubtreeVarDepth.class);
		subtreerep.set("prob", 0.20);
		subtreerep.set("max_depth", 2.25);
		subtreerep.set("min_depth", 0.75);
		subtreerep.set("max_tries", 10);
		EvolutionOpConfig merge = new EvolutionOpConfig("Merge",
				WeightedMergeOperator.class);
		EvolutionOpConfig replace = new EvolutionOpConfig("Replace",
				ReplaceOperator.class);
		replace.set("background", "Merge");
		replace.set("replacement", "Elitism");
		replace.set("num_attempts", 1000);
		try {
			ep.addOperator(elitism);
			ep.addOperator(tour_sel);
			ep.addOperator(xover);
			ep.addOperator(pointmut);
			ep.addOperator(subtreerep);
			ep.addOperator(merge);
			ep.addOperator(replace);
			ep.createPipe(tour_sel, xover);
			ep.createPipe(tour_sel, subtreerep);
			ep.createPipe(xover, pointmut);
			ep.createPipe(subtreerep, merge);
			ep.createPipe(pointmut, merge);
			ep.createPipe(elitism, replace);
			ep.createPipe("Merge", "Replace");
		} catch (BadConfiguration e) {
			fail(e.getMessage());
		}

		vm.setTreeInitializer(init);
		vm.setTreeConfig(tree_conf);
		vm.setEvoPipeline(ep);

		root.setVM(vm);

		// Ready for
		// run-time=============================================================
		root.init();

		double first_best_pi = Double.MAX_VALUE;
		double last_best = Double.MIN_VALUE;
		double last_best_pi = Double.MAX_VALUE;
		for (int gen = 0; gen < 100; gen++) {
			System.err.println("~~~~~" + gen);
			double best_pi = Double.MAX_VALUE;
			double best_w = Double.MIN_VALUE;
			for (Genotype g : root.getPopulation().getGenotypes()) {
				double v;
				try {
					// System.err.println(g.rep().toString(null));
					Object retval = g.rep().eval(null);
					v = (Double) retval;
					double w = Math.pow(Math.abs(v - Math.PI), -2);
					g.setFitness(w);
					best_pi = (Math.abs(Math.PI - v) < Math.abs(Math.PI - best_pi)) ? v : best_pi;
					best_w = (w > best_w) ? w : best_w;
				} catch (BadEvaluation e) {
					System.err.println("Bad evaluation: " + e.getMessage());
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
					fail("Problem.");
				}
			}
			try {
				root.getVM().evolve();
			} catch (BadConfiguration e) {
				e.getMessage();
				e.printStackTrace();
				fail("Unable to execute evolution pipeline.");
			}
			assertTrue(last_best <= best_w); // Should never get worse because
												// of elitism
			last_best = best_w;
			last_best_pi = best_pi;
			if (gen == 0) {
				first_best_pi = best_pi;
			}
		}

		int count = 0;
		GPTree deepest = null;
		int max_found = 0;
		for (Genotype g : root.getPopulation().getGenotypes()) {
			try {
				g.rep().eval(null);
			} catch (Exception e) {
				count++;
			}
			GPNode r = ((GPTree) g.rep()).getRoot();
			int max_depth = GPTreeUtil.maxDepth(r);
			max_found = (max_depth > max_found) ? max_depth : max_found;
			deepest = (GPTree) ((deepest == null || max_depth > max_found) ? g.rep() : deepest);
			if (max_depth > tree_conf.getMaxDepth()) {
				// System.err.println(max_depth + " should be <= " +
				// tree_conf.getMaxDepth());
				fail();
			}
		}
		System.err.println(deepest.toString() + " " + max_found);

		assertEquals(count, 0);
		assertTrue((Math.abs(last_best_pi - Math.PI) < 0.1));
		assertTrue((Math.abs(first_best_pi - Math.PI) >= Math.abs(last_best_pi - Math.PI)));
		System.err.println(first_best_pi + " " + last_best_pi);
	}
}
