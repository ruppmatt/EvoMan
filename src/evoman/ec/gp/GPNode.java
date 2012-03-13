package evoman.ec.gp;


import java.io.*;

import evoman.ec.gp.init.*;
import evoman.evo.*;



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
	protected GPTree			_tree;						// Link
															// to
															// the
															// GPTree
															// (and
															// EMState
															// information)
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
	 * @param t
	 * @param conf
	 * @param parent
	 * @param pos
	 */
	public GPNode(GPTree t, GPNodeConfig conf, GPNode parent, GPNodePos pos) {
		_tree = t;
		_pos = pos;
		_conf = conf;
		_parent = parent;
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
	public abstract GPNode clone(GPTree t, GPNode parent);



	/**
	 * Clone the children
	 * 
	 * @param t
	 *            Tree receiving clone
	 * @param clone
	 *            Node node new clone of this node
	 */
	protected void doClone(GPTree t, GPNode clone) {
		// System.err.println("\t\tCloning " + clone.getClass());
		int num_children = numChildren();
		clone._children = (num_children > 0) ? new GPNode[num_children] : null;
		for (int k = 0; k < num_children; k++) {
			clone._children[k] = _children[k].clone(t, clone);
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



	/**
	 * Initialize the node's children
	 * 
	 * @return
	 */
	public void init(GPTreeInitializer init) {
		byte count = 0;
		int num_children = _conf.getConstraints().numChildren();
		_children = new GPNode[num_children];
		for (int k = 0; k < num_children; k++) {
			Class<?> cl = _conf.getConstraints().getChildTypes()[k];
			GPNode child_node = _tree.createNode(this, cl, _pos.newPos(count), init);
			_children[k] = child_node;
			child_node.init(init);
			count += 1;
		}
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
	 * Get the tree this node belongs to.
	 * 
	 * @return
	 */
	public GPTree getTree() {
		return _tree;
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
