package test.gp;


import static org.junit.Assert.*;

import org.junit.*;

import evoict.*;
import evoman.ec.composite.*;
import evoman.ec.composite.init.*;
import evoman.ec.gp.*;
import evoman.ec.gp.init.*;
import evoman.ec.gp.nonterminals.*;
import evoman.ec.gp.terminals.*;
import evoman.evo.structs.*;
import evoman.evo.vm.*;



public class CompositeTest {

	@Test
	public void test() {

		EvoPool root = new EvoPool("root");
		EvoPool ep_0 = new EvoPool("one", root);
		EvoPool ep_1 = new EvoPool("two", root);
		EvoPool ep_2 = new EvoPool("three", root);

		// Initialize Subtrees
		GPNodeDirectory dir_0 = new GPNodeDirectory(ep_0);
		GPNodeDirectory dir_1 = new GPNodeDirectory(ep_1);
		GPNodeDirectory dir_2 = new GPNodeDirectory(ep_2);

		GPNodeConfig gp_add = new GPNodeConfig(GPAdd.class);
		GPNodeConfig gp_sub = new GPNodeConfig(GPSubtract.class);
		GPNodeConfig gp_mult = new GPNodeConfig(GPMultiply.class);
		GPNodeConfig gp_max = new GPNodeConfig(GPMax.class);
		GPNodeConfig gp_min = new GPNodeConfig(GPMin.class);
		GPNodeConfig gp_cnd = new GPNodeConfig(GPConditional.class);
		GPNodeConfig gp_bool = new GPNodeConfig(GPBooleanERC.class);

		GPNodeConfig gp_double0 = new GPNodeConfig(GPDoubleERC.class);
		gp_double0.set("max", 1.0);
		gp_double0.set("min", -1.0);

		GPNodeConfig gp_double1 = new GPNodeConfig(GPDoubleERC.class);
		gp_double1.set("max", 2.0);
		gp_double1.set("min", -2.0);

		GPNodeConfig gp_double2 = new GPNodeConfig(GPDoubleERC.class);
		gp_double2.set("max", 3.0);
		gp_double2.set("min", -3.0);

		try {
			dir_0.addNodeConfig(gp_add);
			dir_0.addNodeConfig(gp_sub);
			dir_0.addNodeConfig(gp_mult);
			dir_0.addNodeConfig(gp_max);
			dir_0.addNodeConfig(gp_min);
			dir_0.addNodeConfig(gp_cnd);
			dir_0.addNodeConfig(gp_bool);
			dir_0.addNodeConfig(gp_double0);
		} catch (BadConfiguration bc) {
			fail("Unable to initialize node directory for tree 1.\n" + bc.getMessage());
		}

		try {
			dir_1.addNodeConfig(gp_add);
			dir_1.addNodeConfig(gp_sub);
			dir_1.addNodeConfig(gp_mult);
			dir_1.addNodeConfig(gp_max);
			dir_1.addNodeConfig(gp_min);
			dir_1.addNodeConfig(gp_cnd);
			dir_1.addNodeConfig(gp_bool);
			dir_1.addNodeConfig(gp_double1);
		} catch (BadConfiguration bc) {
			fail("Unable to initialize node directory for tree 2.\n" + bc.getMessage());
		}

		try {
			dir_2.addNodeConfig(gp_add);
			dir_2.addNodeConfig(gp_sub);
			dir_2.addNodeConfig(gp_mult);
			dir_2.addNodeConfig(gp_max);
			dir_2.addNodeConfig(gp_min);
			dir_2.addNodeConfig(gp_cnd);
			dir_2.addNodeConfig(gp_bool);
			dir_2.addNodeConfig(gp_double2);
		} catch (BadConfiguration bc) {
			fail("Unable to initialize node directory for tree 3\n" + bc.getMessage());
		}

		GPInitConfig init_conf_0 = new GPInitConfig();
		init_conf_0.set("depth", 3);
		GPInitConfig init_conf_1 = new GPInitConfig();
		init_conf_1.set("depth", 3);
		GPInitConfig init_conf_2 = new GPInitConfig();
		init_conf_2.set("depth", 3);

		GPFullTree init_0 = new GPFullTree(ep_0, init_conf_0);
		GPFullTree init_1 = new GPFullTree(ep_1, init_conf_1);
		GPFullTree init_2 = new GPFullTree(ep_2, init_conf_2);

		GPTreeConfig tconf_0 = new GPTreeConfig(dir_0, init_0);
		tconf_0.set("depth", 3);
		GPTreeConfig tconf_1 = new GPTreeConfig(dir_1, init_1);
		tconf_1.set("depth", 3);
		GPTreeConfig tconf_2 = new GPTreeConfig(dir_2, init_2);
		tconf_2.set("depth", 3);

		VariationManagerConfig vm_config_0 = new VariationManagerConfig();
		vm_config_0.set("pop_size", 100);
		VariationManagerConfig vm_config_1 = new VariationManagerConfig();
		vm_config_1.set("pop_size", 100);
		VariationManagerConfig vm_config_2 = new VariationManagerConfig();
		vm_config_2.set("pop_size", 100);

		GPVariationManager vm_0 = new GPVariationManager(ep_0, vm_config_0);
		GPVariationManager vm_1 = new GPVariationManager(ep_1, vm_config_1);
		GPVariationManager vm_2 = new GPVariationManager(ep_2, vm_config_2);

		vm_0.setTreeConfig(tconf_0);
		vm_1.setTreeConfig(tconf_1);
		vm_2.setTreeConfig(tconf_2);

		// Build composite

		CompositeInitConfig comp_init_conf = new CompositeInitConfig(OneEachWithReplacement.class);
		CompositeInitializer comp_init = new OneEachWithReplacement(comp_init_conf);
		CompositeRepConfig cconf = new CompositeRepConfig(FixedUniqueComposite.class, comp_init_conf);

		VariationManagerConfig vm_config_cmp = new VariationManagerConfig();
		vm_config_cmp.set("pop_size", 100);
		CompositeVariationManager vm_cmp = new CompositeVariationManager(null, vm_config_cmp);

		root.init();
	}
}
