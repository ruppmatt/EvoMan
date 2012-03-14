package evoman.ec.gp;


import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import evoict.*;
import evoman.ec.*;
import evoman.ec.gp.find.*;
import evoman.ec.gp.init.*;
import evoman.evo.structs.*;



/**
 * GPTrees are genetic-programming trees. Any node in a GPTree may be altered,
 * including the root node. Outside of the class, GPTrees are constructed and
 * initialized when created. Initialization begins with the root node. The
 * return type of the root node is identified. The tree initializer is consulted
 * to determine whether or not the root is to be a terminal node. If the
 * initializer's createTerminal method returns true, then the NodeDirectory,
 * contained in the configuration, will attempt to find a node configuration
 * that has no children and the correct return type; otherwise, a random
 * configuration with the correct return type. The selected node configuration
 * is then passed to the buildNode method where the GPNode is actually
 * constructed from the GPNodeConfig information. Once completed, the node is
 * then placed in the tree. All subsequent nodes follow a
 * similar construction pattern, with nodes being initialized in a depth-first
 * manner.
 * 
 * At evaluation time, the eval(Object) method is called. The parameter is the
 * context in which the tree is to be evaluated. A context, for example, might
 * just be a Stimulus object to be used when MethodDictionaries are required.
 * 
 * @author ruppmatt
 * 
 */

public class GPTree implements Representation, Serializable {

	/**
	 * Valdate the Tree configuration
	 * 
	 * @param conf
	 *            Tree configuration
	 * @throws BadConfiguration
	 */
	protected static void validate(GPTreeConfig conf) throws BadConfiguration {
		BadConfiguration bad = new BadConfiguration();
		if (!conf.validate("max_depth", Integer.class) || conf.getMaxDepth() < 1) {
			bad.append("GPTree: max_depth not set.");
		}
		if (conf._node_dir == null) {
			bad.append("GPTree: no node directory specified in configuration");
		} else {
			try {
				conf._node_dir.validate();
			} catch (BadConfiguration bc) {
				bad.append(bc.getMessage());
			}
		}
		if (!conf.validate("return_type", Class.class)) {
			bad.append("GPTree: no return_type specified");
		}

		bad.validate();
	}

	private static final long	serialVersionUID	= 1L;
	protected GPNode			_root				= null;
	protected GPTreeConfig		_config;



	/**
	 * Create an uninitialized GPTree
	 * 
	 * @param state
	 *            System state information
	 * @param conf
	 *            Configuration of the tree
	 */
	protected GPTree(GPTreeConfig conf) {
		_config = conf;
	}



	/**
	 * Construct a GPTree
	 * 
	 * @param state
	 *            System state information
	 * @param conf
	 *            Configuration of the tree
	 * @param init
	 *            Tree initializer
	 * @throws BadConfiguration
	 */
	public GPTree(EMState state, GPTreeConfig conf, GPTreeInitializer init) throws BadConfiguration {
		_config = conf;
		init(state, init);
	}



	/**
	 * Build the tree using a particular tree initializer.
	 * 
	 * @param init
	 *            Tree initializer
	 * @throws BadConfiguration
	 */
	public void init(EMState state, GPTreeInitializer init) throws BadConfiguration {
		_root = createNode(state, this, null, (Class<?>) getConfig().get("return_type"), new GPNodePos(), init);
		_root.init(state);
		_root.buildDescendents(state, this, init);
	}



	/**
	 * Return the root node
	 * 
	 * @return
	 */
	public GPNode getRoot() {
		return _root;
	}



	/**
	 * Return the configuration of the tree
	 * 
	 * @return
	 */
	public GPTreeConfig getConfig() {
		return _config;
	}



	/**
	 * Return the node configuration directory
	 * 
	 * @return
	 */
	public GPNodeDirectory getNodeDirectory() {
		return _config.getNodeDirectory();
	}



	/**
	 * Evaluate the tree
	 */
	@Override
	public Object eval(Object o) throws BadEvaluation {
		try {
			return _root.eval(o);
		} catch (BadNodeValue bv) {
			String msg = "GPTree contains a node with a bad value.\n"
					+ "Node subtree: " + bv.getNode().toString() + "\n"
					+ "Message: " + bv.getMessage();
			throw new BadEvaluation(msg);

		}
	}



	/**
	 * Write the tree as a string (without evaluating it)
	 */
	@Override
	public String toString() {
		return _root.toString();
	}



	/**
	 * Write the tree as a string with evaluation information.
	 */
	@Override
	public String toString(Object context) throws BadEvaluation {
		try {
			return _root.toString(context);
		} catch (BadNodeValue bv) {
			String msg = "GPTree contains a node with a bad value.\n"
					+ "Node subtree: " + bv.getNode().toString() + "\n"
					+ "Message: " + bv.getMessage();
			throw new BadEvaluation(msg);
		}
	}



	/**
	 * Try to create a new node with a particular type. This method consults the
	 * tree's initializer to determine whether or not the node to be created is
	 * a terminal.
	 * 
	 * @param state
	 *            The state object that contains getRandom()
	 * @param parent
	 *            The parent of the new node to create
	 * @param ret_type
	 *            The return type of the new node
	 * @param pos
	 *            The position of the new node in the tree
	 * @param init
	 *            The initializer to consult as to whether the node is a
	 *            terminal
	 * @return
	 */
	public static GPNode createNode(EMState state, GPTree t, GPNode parent, Class<?> ret_type, GPNodePos pos,
			GPTreeInitializer init)
			throws BadConfiguration {
		boolean terminal = init.createTerminal(t, parent, ret_type, state);
		GPNodeConfig cl_con = null;

		if (terminal == true) {
			cl_con = t.getConfig().getNodeDirectory().randomTerminal(state, ret_type);
			if (cl_con == null) {
				throw new BadConfiguration("No terminal nodes found with type: " + ret_type.getName());
			}
		} else {
			cl_con = t.getConfig().getNodeDirectory().randomFunction(state, ret_type);
			if (cl_con == null) {
				throw new BadConfiguration("No function nodes for return type found: " + ret_type.getName());
			}
		}
		return buildNode(t, parent, cl_con, pos);
	}



	/**
	 * Construct a node.
	 * 
	 * @param parent
	 *            The parent of the new node
	 * @param conf
	 *            The configuration of the node to be constructed
	 * @param pos
	 *            The position of the node in the tree
	 * @return
	 *         The new node
	 */
	protected static GPNode buildNode(GPTree t, GPNode parent, GPNodeConfig conf, GPNodePos pos)
			throws BadConfiguration {
		try {
			Constructor<? extends GPNode> construct =
					conf.getNodeClass().getConstructor(GPNodeConfig.class, GPNode.class, GPNodePos.class);
			GPNode new_node = construct.newInstance(conf, parent, pos);
			return new_node;

		} catch (Exception e) {
			throw new BadConfiguration("Unable to instantiate GPNode:" + conf.getNodeClass());
		}
	}



	/**
	 * Serialize the tree into a bitstream.
	 */
	@Override
	public void serializeRepresentation(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
	}



	/**
	 * Get the size of the tree
	 * 
	 * @return
	 */
	public int getSize() {
		LinkedList<GPNode> q = new LinkedList<GPNode>();
		q.add(_root);
		int count = 0;
		while (!q.isEmpty()) {
			count++;
			GPNode cur = q.poll();
			for (int k = 0; k < cur.numChildren(); k++) {
				q.add(cur.getChildren()[k]);
			}
		}
		return count;
	}



	/**
	 * Clone the tree
	 */
	@Override
	public Object clone() {
		GPTree newtree = new GPTree(_config);
		newtree._root = _root.clone(null);
		return newtree;
	}



	/**
	 * Re-root the tree (essentially replacing the tree) with a new node.
	 * 
	 * @param newroot
	 *            New root of the tree
	 * @return
	 *         true if successful
	 */
	public boolean reRoot(GPNode newroot) {
		_root = newroot;
		_root.rebase(null, (byte) 0); // The second parameter is discarded
		return true;
	}



	/**
	 * Return true if the node can be replaced or altered.
	 * 
	 * @param n
	 * @return
	 */
	public boolean canAlter(GPNode n) {
		return true;
	}



	/**
	 * Do a breadth first search, starting with the node start, and collecting
	 * all nodes that meet the criteria set by the FindeNode object
	 * 
	 * @param fn
	 *            Match criteria
	 * @param start
	 *            Start node
	 */
	public static void bfs(FindNode fn, GPTree tree, GPNode start) {
		LinkedList<GPNode> q = new LinkedList<GPNode>();
		q.add(start);
		while (!q.isEmpty() && !fn.done()) {
			GPNode n = q.poll();
			fn.examine(tree, n);
			GPNode[] children = n.getChildren();
			if (children != null) {
				for (int k = 0; k < children.length; k++) {
					q.add(children[k]);
				}
			}
		}
	}



	/**
	 * Do a breadth first search, starting with the root node, and collecting
	 * all nodes that meet the criteria set by the FindNode object
	 * 
	 * @param fn
	 *            Match criteria
	 */

	public void bfs(FindNode fn) {
		GPTree.bfs(fn, this, _root);
	}



	/**
	 * Do a depth first search, starting with the node start, collecting nodes
	 * that meet the criteria set by the FindNode object
	 * 
	 * @param fn
	 *            criteria to match
	 * @param start
	 */
	public static void dfs(FindNode fn, GPTree tree, GPNode start) {
		Stack<GPNode> q = new Stack<GPNode>();
		q.add(start);
		while (!q.isEmpty() && !fn.done()) {
			GPNode n = q.pop();
			fn.examine(tree, n);
			for (GPNode child : n.getChildren()) {
				q.add(child);
			}
		}
	}



	/**
	 * Do a depth first search, collecting notes that meet the FindNode object
	 * criteria
	 * 
	 * @param fn
	 *            FindNode object to match
	 */
	public void dfs(FindNode fn) {
		GPTree.dfs(fn, this, _root);
	}

}
