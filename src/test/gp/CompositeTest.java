package test.gp;


import org.junit.*;

import evoman.ec.composite.*;
import evoman.ec.composite.init.*;
import evoman.ec.gp.*;
import evoman.ec.gp.init.*;
import evoman.ec.gp.nonterminals.*;
import evoman.ec.gp.terminals.*;
import evoman.evo.pop.*;
import evoman.evo.structs.*;



public class CompositeTest {

	protected GPNodeDirectory buildNodeDirectory() {
		GPNodeConfig gp_add = new GPNodeConfig(GPAdd.class);
		GPNodeConfig gp_sub = new GPNodeConfig(GPSubtract.class);
		GPNodeConfig gp_mult = new GPNodeConfig(GPMultiply.class);
		GPNodeConfig gp_max = new GPNodeConfig(GPMax.class);
		GPNodeConfig gp_min = new GPNodeConfig(GPMin.class);
		GPNodeConfig gp_cnd = new GPNodeConfig(GPConditional.class);
		GPNodeConfig gp_bool = new GPNodeConfig(GPBooleanERC.class);
		GPNodeConfig gp_greater = new GPNodeConfig(GPGreaterThan.class);

		GPNodeDirectory base_nodes = new GPNodeDirectory();
		base_nodes.addNodeConfig(gp_add);
		base_nodes.addNodeConfig(gp_sub);
		base_nodes.addNodeConfig(gp_mult);
		base_nodes.addNodeConfig(gp_max);
		base_nodes.addNodeConfig(gp_min);
		base_nodes.addNodeConfig(gp_cnd);
		base_nodes.addNodeConfig(gp_bool);
		base_nodes.addNodeConfig(gp_greater);

		return base_nodes;
	}



	@Test
	public void test() {

		GPNodeDirectory base_nodes = buildNodeDirectory();

		EvoPool root = new EvoPool("root");
		CompositeVariationManager vm_root = new CompositeVariationManager(root);
		vm_root.set("pop_size", 1000);

		// Configure EvoPool for GPTree 1
		EvoPool ep_0 = new EvoPool("one", root);
		GPVariationManager vm_0 = new GPVariationManager(ep_0);
		vm_0.set("pop_size", 1000);
		GPTreeConfig tconf_0 = new GPTreeConfig(GPTree.class);
		tconf_0.set("max_depth", 3);
		tconf_0.set("return_type", Double.class);
		GPNodeDirectory dir_0 = (GPNodeDirectory) base_nodes.clone();
		GPNodeConfig gp_double0 = new GPNodeConfig(GPDoubleERC.class);
		gp_double0.set("max", 1.0);
		gp_double0.set("min", -1.0);
		dir_0.addNodeConfig(gp_double0);
		tconf_0.setNodeDirectory(dir_0);
		vm_0.setTreeConfig(tconf_0);
		GPFullTree init_0 = new GPFullTree();
		init_0.set("max_depth", 3);
		vm_0.setTreeInitializer(init_0);

		EvoPool ep_1 = new EvoPool("two", root);
		EvoPool ep_2 = new EvoPool("three", root);

		GPVariationManager vm_1 = new GPVariationManager(ep_1);
		vm_1.set("pop_size", 1000);
		GPVariationManager vm_2 = new GPVariationManager(ep_2);
		vm_2.set("pop_size", 1000);

		GPNodeDirectory dir_1 = (GPNodeDirectory) base_nodes.clone();
		GPNodeConfig gp_double1 = new GPNodeConfig(GPDoubleERC.class);
		gp_double1.set("max", 2.0);
		gp_double1.set("min", -2.0);
		dir_1.addNodeConfig(gp_double1);

		GPNodeDirectory dir_2 = (GPNodeDirectory) base_nodes.clone();
		GPNodeConfig gp_double2 = new GPNodeConfig(GPDoubleERC.class);
		gp_double2.set("max", 3.0);
		gp_double2.set("min", -3.0);
		dir_2.addNodeConfig(gp_double2);

		// Tree initializers

		GPFullTree init_1 = new GPFullTree();
		init_1.set("max_depth", 3);
		GPFullTree init_2 = new GPFullTree();
		init_2.set("max_depth", 3);

		GPTreeConfig tconf_1 = new GPTreeConfig(GPTree.class);
		tconf_1.set("max_depth", 3);
		tconf_1.set("return_type", Double.class);
		tconf_1.setNodeDirectory(dir_1);
		GPTreeConfig tconf_2 = new GPTreeConfig(GPTree.class);
		tconf_2.set("max_depth", 3);
		tconf_2.set("return_type", Double.class);
		tconf_2.setNodeDirectory(dir_2);

		vm_1.setTreeConfig(tconf_1);
		vm_1.setTreeInitializer(init_1);
		vm_2.setTreeConfig(tconf_2);
		vm_2.setTreeInitializer(init_2);

		// Build composite
		CompositeInitializer comp_init = new InitializeWithReplacement();
		CompositeConfig comp_conf = new CompositeConfig(AutoFixedComposite.class);
		vm_root.setCompositeConfig(comp_conf);
		vm_root.setCompositeInit(comp_init);

		root.init();
		for (Genotype g : root.getPopulation().getGenotypes()) {
			System.err.println(g.rep().toString() + "\n\n");
		}
	}
}
