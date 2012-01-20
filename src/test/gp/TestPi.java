package test.gp;


import static org.junit.Assert.*;

import org.junit.*;

import evoman.ec.gp.*;
import evoman.ec.gp.init.*;
import evoman.ec.gp.nonterminals.*;
import evoman.ec.gp.terminals.*;
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

		vm_conf.set("pop_size", 100);

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

		root.init();

		assertEquals(root.getPopulation().size(), 100);
		for (Genotype g : root.getPopulation().getGenotypes()) {
			System.err.println(g.rep().toString());
			System.err.println();
		}

		for (Genotype g : root.getPopulation().getGenotypes()) {
			Double d = (Double) g.rep().eval(null);
			System.err.println(d);
			System.err.println(g.rep().lastEval());
			System.err.println(g.rep().toString());
			System.err.println();
		}

	}
}
