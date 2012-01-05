package evoman.ec.gp;


import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import evoict.*;
import evoict.io.*;
import evoman.ec.gp.init.*;
import evoman.evo.*;
import evoman.evo.pop.*;
import evoman.evo.structs.*;



public class GPTree implements Representation, EMState, Serializable {

	private static final long	serialVersionUID	= 1L;
	protected GPNode			_root				= null;
	protected GPNodeDirectory	_node_classes;
	protected ArrayList<GPNode>	_mutable_nodes		= new ArrayList<GPNode>();
	protected GPTreeInitializer	_init				= null;
	protected EMState			_state;
	protected KeyValueStore		_kv					= new KeyValueStore();



	public GPTree(EMState state, GPTreeInitializer init, GPNodeDirectory nodes) {
		_node_classes = new GPNodeDirectory(state);
		_state = state;
		_init = init;
		_node_classes = nodes;
	}



	public GPNode getRoot() {
		return _root;
	}



	@Override
	public Object eval(Object o) {
		return _root.eval(o);
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



	public GPNode initCreateNode(GPNode n, Class<?> ret_type) {
		boolean terminal = _init.createTerminal(this, n, ret_type);
		GPNodeConfig cl_con = null;
		if (terminal) {
			cl_con = _node_classes.randomTerminal(ret_type);
			if (cl_con == null) {
				getNotifier().fatal("No terminal nodes found with type: " + ret_type.getName());
			}
		} else {
			cl_con = _node_classes.randomFunction(ret_type);
			if (cl_con == null) {
				getNotifier().fatal("No function nodes for return type found: " + ret_type.getName());
			}
		}
		return createNode(n, cl_con);
	}



	public GPNode createNode(GPNode parent, GPNodeConfig conf) {
		try {
			int depth = (parent == null) ? -1 : parent.getDepth();
			Constructor<? extends GPNode> construct =
					conf.getNodeClass().getConstructor(GPTree.class, GPNodeConfig.class, int.class);
			GPNode new_node = construct.newInstance(this, conf, depth + 1);
			if (new_node.getConfig().getConstraints().isMutable())
				_mutable_nodes.add(new_node);
			return new_node;

		} catch (Exception e) {
			e.printStackTrace();

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



	@Override
	public EMState getESParent() {
		return _state;
	}



	@Override
	public void init() {
		_root = initCreateNode(null, Double.class);
		_root.init();
	}



	@Override
	public void finish() {
	}



	@Override
	public MersenneTwisterFast getRandom() {
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



class NodeComparator implements Comparator<GPNode> {

	@Override
	public int compare(GPNode lhs, GPNode rhs) {
		int l = lhs.getDepth();
		int r = rhs.getDepth();
		if (l > r) {
			return 1;
		} else if (l < r) {
			return -1;
		} else {
			return 0;
		}
	}

}
