package evoman.ec.gp;


import java.io.*;

import evoict.*;
import evoman.ec.gp.init.*;
import evoman.evo.*;
import evoman.evo.structs.*;



/**
 * GPNode is the base class for all node types in a GPTree. It also contains the
 * logic for node construction and cloning, child node information, and links to
 * the parents and the children. GPNodes are created at runtime from GPNode
 * configurations.
 * 
 * @author ruppmatt
 * 
 */
public abstract class GPNode implements Constants, Serializable {

	private static final long	serialVersionUID	= 1L;

	protected GPNode[]			_children;					// List
															// of
															// children
	protected GPNodeConfig		_conf;						// Configuration
															// of
															// node
	protected GPNodePos			_pos;						// Position
															// of
															// node
															// in
															// the
															// treez
	protected GPNode			_parent;					// Link
															// to
															// the
															// parent
															// node



	/**
	 * Construct a new GPNode using a particular configuration
	 * 
	 * @param conf
	 * @param parent
	 * @param pos
	 */
	public GPNode(GPNodeConfig conf, GPNode parent, GPNodePos pos) {
		_pos = pos;
		_conf = conf;
		_parent = parent;
		int num_children = _conf.getConstraints().numChildren();
		_children = new GPNode[num_children];
	}



	/**
	 * Initialize the node itself (e.g. set the state of the node)
	 * 
	 * @param state
	 *            State information needed for initialization
	 * @throws BadConfiguration
	 */
	public abstract void init(EMState state) throws BadConfiguration;



	/**
	 * Create and initialize the node's descendents.
	 * 
	 * @return
	 * @throws BadConfiguration
	 */
	public void buildDescendents(EMState state, GPTree t, GPTreeInitializer init) throws BadConfiguration {
		byte count = 0;
		int num_children = _conf.getConstraints().numChildren();
		for (int k = 0; k < num_children; k++) {
			Class<?> cl = _conf.getConstraints().getChildTypes()[k];
			GPNode child_node = GPTree.createNode(state, t, this, cl, _pos.newPos(count), init);
			child_node.init(state);
			_children[k] = child_node;
			child_node.buildDescendents(state, t, init);
			count += 1;
		}
	}



	/**
	 * Clone the subtree starting with this node
	 * 
	 * @param t
	 *            The tree to clone into
	 * @param parent
	 *            Parent node (must be in receiving tree)
	 * @return
	 */
	public abstract GPNode clone(GPNode parent);



	/**
	 * Clone the children
	 * 
	 * @param t
	 *            Tree receiving clone
	 * @param clone
	 *            Node node new clone of this node
	 */
	protected void doClone(GPNode clone) {
		int num_children = numChildren();
		clone._children = (num_children > 0) ? new GPNode[num_children] : null;
		for (int k = 0; k < num_children; k++) {
			clone._children[k] = _children[k].clone(clone);
		}
	}



	/**
	 * Evaluation function for node
	 * 
	 * @param context
	 *            The context to evaluate using
	 * @return
	 *         The results of the node
	 * @throws BadNodeValue
	 *             Something is wrong during evaluation
	 */
	abstract public Object eval(Object context) throws BadNodeValue;



	/**
	 * Return the children nodes
	 * 
	 * @return
	 */
	public GPNode[] getChildren() {
		return _children;
	}



	/**
	 * Return the parent node
	 * 
	 * @return
	 */
	public GPNode getParent() {
		return _parent;
	}



	/**
	 * Return the node configuration
	 * 
	 * @return
	 */
	public GPNodeConfig getConfig() {
		return _conf;
	}



	/**
	 * Return position information
	 * 
	 * @return
	 */
	public GPNodePos getPosition() {
		return _pos;
	}



	/**
	 * Return the depth of the node using position information
	 * 
	 * @return
	 */
	public int getDepth() {
		return _pos.getDepth();
	}



	public abstract String toString(Object context) throws BadNodeValue;



	/**
	 * Helper method to print the last value tree in Newick format
	 * 
	 * @param value
	 * @return
	 */
	protected String toString(Object context, String value) throws BadNodeValue {
		StringBuffer buf = new StringBuffer();
		int num_children = numChildren();
		if (num_children > 0) {
			buf.append("(");
			for (int k = 0; k < num_children; k++) {
				GPNode child = _children[k];
				buf.append(child.toString(context));
				if (k < num_children - 1) {
					buf.append(",");
				}
			}
			buf.append(")");
		}
		buf.append(value);
		return buf.toString();
	}



	/**
	 * Return the number of children
	 * 
	 * @return
	 */
	public int numChildren() {
		return (_children == null) ? 0 : _children.length;
	}



	/**
	 * Swap a child with another node
	 * 
	 * @param child
	 * @param replace
	 * @return
	 */
	public boolean swap(GPNode child, GPNode replace) {
		for (int k = 0; k < numChildren(); k++) {
			if (_children[k] == child) {
				_children[k] = replace;
				replace.rebase(this, (byte) k);
				return true;
			}
		}
		return false;
	}



	/**
	 * Rebase the node and all descendants with a new position specified by the
	 * parent
	 * 
	 * @param tree
	 * @param parent
	 * @param pos
	 */
	protected void rebase(GPNode parent, byte pos) {
		_pos = (parent == null) ? new GPNodePos() : parent.getPosition().newPos(pos);
		for (int k = 0; k < numChildren(); k++) {
			_children[k].rebase(this, (byte) k);
		}
	}



	/**
	 * Print the tree
	 */
	@Override
	abstract public String toString();



	/**
	 * Helper method to print a string value of the entire tree in Newick format
	 * 
	 * @param value
	 * @return
	 */
	protected String toString(String value) {
		StringBuffer buf = new StringBuffer();
		int num_children = numChildren();
		if (num_children > 0) {
			buf.append("(");
			for (int k = 0; k < num_children; k++) {
				GPNode child = _children[k];
				buf.append(child.toString());
				if (k < num_children - 1) {
					buf.append(",");
				}
			}
			buf.append(")");
		}
		buf.append(value);
		return buf.toString();
	}

}
