package test.gp;


import static org.junit.Assert.*;

import org.junit.*;

import evoict.*;
import evoman.ec.gp.*;
import evoman.ec.gp.init.*;
import evoman.ec.gp.nonterminals.*;
import evoman.ec.gp.terminals.*;
import evoman.evo.structs.*;



public class TestFullTree {

	@Test
	public void test() {
		EMHNode root = new EMHNode("root");

		GPInitConfig init_conf = new GPInitConfig();
		init_conf.set("depth", 3);
		GPFullTree init = new GPFullTree(root, init_conf);

		GPNodeDirectory nodes = new GPNodeDirectory(root);

		GPNodeConfig constOne = new GPNodeConfig(GPNodeDoubleConst.class);
		constOne.set("value", 1.0);
		try {
			nodes.addNodeConfig(constOne);
			nodes.addNodeConfig(new GPNodeConfig(GPAdd.class));
		} catch (BadConfiguration bc) {
			fail("Node configurations should not throw errrors.");
		}

		GPTreeConfig tree_config = new GPTreeConfig(nodes, init);
		tree_config.set("return_type", Double.class);
		tree_config.set("max_depth", 10);
		GPTree tree_one = new GPTree(root, tree_config);
		tree_one.init();
		assertEquals(4.0, tree_one.eval(null));
		assertEquals("((1.0,1.0)+,(1.0,1.0)+)+", tree_one.toString());
		assertEquals("((1.0,1.0)+<2.0>,(1.0,1.0)+<2.0>)+<4.0>", tree_one.toString(null));

		init_conf.set("depth", 4);
		GPNodeConfig erc = new GPNodeConfig(GPDoubleERC.class);
		try {
			nodes.addNodeConfig(erc);
			fail("ERC should throw a bad configuration at this point.");
		} catch (BadConfiguration e) {

		}
		erc.set("min", 0.0);
		erc.set("max", 3.0);
		try {
			nodes.addNodeConfig(erc);
		} catch (BadConfiguration bc) {
			fail("Erc should not throw a bad configuration at this point");
		}

		GPTree tree = new GPTree(root, tree_config);
		tree.init();
		tree.eval(null);
		tree.toString();
		tree.toString(null);
	}
}
