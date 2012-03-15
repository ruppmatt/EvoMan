package test.gp;


import static org.junit.Assert.*;

import org.junit.*;

import evoict.*;
import evoman.ec.*;
import evoman.ec.gp.*;
import evoman.ec.gp.init.*;
import evoman.ec.gp.nonterminals.*;
import evoman.ec.gp.terminals.*;
import evoman.evo.structs.*;



public class TestFullTree {

	@Test
	public void test() {
		EMHNode root = new EMHNode("root");

		GPTreeInitializer init = new GPFullTree();
		init.set("max_depth", 3);

		GPNodeDirectory nodes = new GPNodeDirectory();

		GPNodeConfig constOne = new GPNodeConfig(GPNodeDoubleConst.class);
		constOne.set("value", 1.0);
		nodes.addNodeConfig(constOne);
		nodes.addNodeConfig(new GPNodeConfig(GPAdd.class));

		GPTreeConfig tree_config = new GPTreeConfig(GPTree.class);
		tree_config.set("return_type", Double.class);
		tree_config.set("max_depth", 3);
		tree_config.setNodeDirectory(nodes);

		GPTree tree_one;
		try {
			tree_config.getNodeDirectory().init();
			tree_one = new GPTree(root, tree_config, init);
			try {
				assertEquals(4.0, tree_one.eval(null));
				assertEquals("((1.0,1.0)+,(1.0,1.0)+)+", tree_one.toString());
				assertEquals("((1.0,1.0)+<2.0>,(1.0,1.0)+<2.0>)+<4.0>", tree_one.toString(null));
			} catch (BadEvaluation be) {
				fail("Unable to evluate tree.");
			}
		} catch (BadConfiguration e1) {
			e1.printStackTrace();
			fail("Unable to construct tree.");
		}

		init.set("depth", 4);
		GPNodeConfig erc = new GPNodeConfig(GPDoubleERC.class);

		nodes.addNodeConfig(erc);

		erc.set("min", 0.0);
		erc.set("max", 3.0);
		nodes.addNodeConfig(erc);

		GPTree tree;
		try {
			tree = new GPTree(root, tree_config, init);
			try {
				tree.eval(null);
				tree.toString();
				tree.toString(null);
			} catch (BadEvaluation be) {
				fail("Unable to evaluate tree.");
				be.printStackTrace();
			}
		} catch (BadConfiguration e) {
			fail("Unable to construct tree.");
			e.printStackTrace();
		}

	}
}
