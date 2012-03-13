package evoman.ec.gp;


import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import evoict.*;
import evoict.io.*;
import evoman.ec.gp.find.*;
import evoman.ec.gp.init.*;
import evoman.evo.pop.*;
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

public class GPTree implements Representation, EMState, Serializable {

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
		}
		if (!conf.validate("return_type", Class.class)) {
			bad.append("GPTree: no return_type specified");
		}
		try {
			conf._node_dir.validate();
		} catch (BadConfiguration bc) {
			bad.append(bc.getMessage());
		}
		bad.validate();
	}

	private static final long	serialVersionUID	= 1L;
	protected GPNode			_root				= null;
	protected GPTreeConfig		_config;
	protected EMState			_state;



	/**
	 * Create an uninitialized GPTree
	 * 
	 * @param state
	 *            System state information
	 * @param conf
	 *            Configuration of the tree
	 */
	protected GPTree(EMState state, GPTreeConfig conf) {
		_state = state;
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
	 */
	public GPTree(EMState state, GPTreeConfig conf, GPTreeInitializer init) {
		_state = state;
		_config = conf;
		init(init);
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
	public Object eval(Object o) {
		try {
			return _root.eval(o);
		} catch (BadNodeValue bv) {
			getNotifier().fatal("GPTree contains a node with a bad value."
					+ getNotifier().endl
					+ "Node subtree: " + bv.getNode().toString()
					+ getNotifier().endl
					+ "Message: " + bv.getMessage());
		}
		return null;
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
	public String toString(Object context) {
		try {
			return _root.toString(context);
		} catch (BadNodeValue bv) {
			getNotifier().fatal("GPTree contains a node with a bad value."
					+ getNotifier().endl
					+ "Node subtree: " + bv.getNode().toString()
					+ getNotifier().endl
					+ "Message: " + bv.getMessage());
			return null;
		}
	}



	/**
	 * Try to create a new node with a particular type. This method consults the
	 * tree's initializer to determine whether or not the node to be created is
	 * a terminal.
	 * 
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
	public GPNode createNode(GPNode parent, Class<?> ret_type, GPNodePos pos, GPTreeInitializer init) {
		boolean terminal = init.createTerminal(this, parent, ret_type, _state);
		GPNodeConfig cl_con = null;

		if (terminal == true) {
			cl_con = getConfig().getNodeDirectory().randomTerminal(ret_type);
			if (cl_con == null) {
				getNotifier().fatal("No terminal nodes found with type: " + ret_type.getName());
			}
		} else {
			cl_con = getConfig().getNodeDirectory().randomFunction(ret_type);
			if (cl_con == null) {
				getNotifier().fatal("No function nodes for return type found: " + ret_type.getName());
			}
		}
		return buildNode(parent, cl_con, pos);
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
	protected GPNode buildNode(GPNode parent, GPNodeConfig conf, GPNodePos pos) {
		try {
			Constructor<? extends GPNode> construct =
					conf.getNodeClass().getConstructor(GPTree.class, GPNodeConfig.class, GPNode.class, GPNodePos.class);
			GPNode new_node = construct.newInstance(this, conf, parent, pos);
			return new_node;

		} catch (Exception e) {
			e.printStackTrace();
			e.getCause().printStackTrace();
			getNotifier().fatal("Unable to instantiate GPNode:" + conf.getNodeClass());
		}
		return null; // Should never be reached
	}



	/**
	 * Serialize the tree into a bitstream.
	 */
	@Override
	public void serializeRepresentation(ObjectOutputStream out) throws IOException {
		EMState temp = _state;
		_state = null;
		out.defaultWriteObject();
		_state = temp;
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
		GPTree newtree = new GPTree(_state, _config);
		newtree._root = _root.clone(newtree, null);
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
	public void bfs(FindNode fn, GPNode start) {
		LinkedList<GPNode> q = new LinkedList<GPNode>();
		q.add(start);
		while (!q.isEmpty() && !fn.done()) {
			GPNode n = q.poll();
			fn.examine(n);
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
		bfs(fn, _root);
	}



	/**
	 * Do a depth first search, starting with the node start, collecting nodes
	 * that meet the criteria set by the FindNode object
	 * 
	 * @param fn
	 *            criteria to match
	 * @param start
	 */
	public void dfs(FindNode fn, GPNode start) {
		Stack<GPNode> q = new Stack<GPNode>();
		q.add(start);
		while (!q.isEmpty() && !fn.done()) {
			GPNode n = q.pop();
			fn.examine(n);
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
		dfs(fn, _root);
	}



	@Override
	public EMState getESParent() {
		return _state;
	}



	/**
	 * Build the tree using a particular tree initializer.
	 * 
	 * @param init
	 *            Tree initializer
	 */
	public void init(GPTreeInitializer init) {
		_root = createNode(null, (Class<?>) getConfig().get("return_type"), new GPNodePos(), init);
		_root.init(init);
	}



	/**
	 * The EMState initializer doesn't do anything.
	 */
	@Override
	public void init() {
	}



	/**
	 * The EMState finisher doesn't do anything
	 */
	@Override
	public void finish() {
	}



	@Override
	/**
	 * Get Random uses the initialzing EvoPool (or VM's) random number generator.
	 */
	public RandomGenerator getRandom() {
		return _state.getRandom();
	}



	@Override
	public EMThreader getThreader() {
		return _state.getThreader();
	}



	@Override
	public Notifier getNotifier() {
		return _state.getNotifier();
	}

}
