package test.gp;


import static org.junit.Assert.*;

import org.junit.*;

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
		init_conf.set("depth", 2);
		GPFullTree init = new GPFullTree(root, init_conf);

		GPNodeDirectory nodes = new GPNodeDirectory(root);

		GPNodeConfig constOne = new GPNodeConfig(GPNodeDoubleConst.class);
		constOne.set("value", 1.0);
		assertTrue(nodes.addNodeConfig(constOne));
		assertTrue(nodes.addNodeConfig(new GPNodeConfig(GPAdd.class)));

		GPTree tree_one = new GPTree(root, init, nodes);
		tree_one.init();
		assertEquals(tree_one.eval(null), 4.0);
		assertEquals(tree_one.toString(), "((1.0,1.0)+,(1.0,1.0)+)+");
		assertEquals(tree_one.lastEval(), "((1.0,1.0)+<2.0>,(1.0,1.0)+<2.0>)+<4.0>");
		assertEquals(tree_one.getRoot().last(), 4.0);

		init_conf.set("depth", 4);
		GPNodeConfig erc = new GPNodeConfig(GPDoubleERC.class);
		assertFalse(nodes.addNodeConfig(erc));
		erc.set("min", 0.0);
		erc.set("max", 3.0);
		assertTrue(nodes.addNodeConfig(erc));

		GPTree tree = new GPTree(root, init, nodes);
		tree.init();
		tree.eval(null);
		tree.toString();
		tree.lastEval();
		tree.last();
	}
}
