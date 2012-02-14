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



public class GPTree implements Representation, EMState, Serializable {

	protected static void validate(GPTreeConfig conf) throws BadConfiguration {
		BadConfiguration bad = new BadConfiguration();
		if (!conf.validate("max_depth", Integer.class) || conf.getMaxDepth() < 1) {
			bad.append("GPTree: max_depth not set.");
		}
		if (conf._init == null) {
			bad.append("GPTree: no initializer specified in configuration.");
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



	public GPTree(EMState state, GPTreeConfig conf) {
		_state = state;
		_config = conf;
	}



	public GPNode getRoot() {
		return _root;
	}



	public GPTreeConfig getConfig() {
		return _config;
	}



	public GPNodeDirectory getNodeDirectory() {
		return _config.getNodeDirectory();
	}



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



	public Object last() {
		return _root.last();
	}



	@Override
	public String toString() {
		return _root.toString();
	}



	@Override
	public String lastEval() {
		return _root.lastEval();
	}



	public GPNode createNode(GPNode parent, Class<?> ret_type, GPNodePos pos, GPTreeInitializer init) {
		int depth = (parent != null) ? parent.getDepth() + 1 : 1;
		boolean terminal = init.createTerminal(this, parent, ret_type);// ||
																		// depth
																		// ==
																		// getConfig().getMaxDepth();
		GPNodeConfig cl_con = null;

		if (!terminal && depth > getConfig().getMaxDepth()) {
			System.err.println("In createNode -- exceeding maximum depth.");
		}
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



	@Override
	public void serializeRepresentation(ObjectOutputStream out) throws IOException {
		EMState temp = _state;
		_state = null;
		out.defaultWriteObject();
		_state = temp;
	}



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



	@Override
	public Object clone() {
		GPTree newtree = new GPTree(_state, _config);
		newtree._root = _root.clone(newtree, null);
		return newtree;
	}



	public boolean reRoot(GPNode newroot) {
		_root = newroot;
		_root.rebase(null, -1);
		return true;
	}



	public boolean canAlter(GPNode n) {
		return true;
	}



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



	public void bfs(FindNode fn) {
		bfs(fn, _root);
	}



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



	public void dfs(FindNode fn) {
		dfs(fn, _root);
	}



	@Override
	public EMState getESParent() {
		return _state;
	}



	@Override
	public void init() {
		_root = createNode(null, (Class<?>) getConfig().get("return_type"), new GPNodePos(), getConfig()
				.getInitializer());
		_root.init();
	}



	@Override
	public void finish() {
	}



	@Override
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
