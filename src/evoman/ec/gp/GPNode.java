package evoman.ec.gp;


import java.io.*;
import java.util.*;

import evoman.evo.*;



public abstract class GPNode implements Constants, Serializable {

	private static final long	serialVersionUID	= 1L;
	protected GPTree			_tree;
	protected ArrayList<GPNode>	_children			= new ArrayList<GPNode>();
	protected GPNodeConfig		_conf;
	protected GPNodePos			_pos;
	protected int				_num_descendents	= 0;
	protected GPNode			_parent;



	public GPNode(GPTree t, GPNodeConfig conf, GPNode parent, GPNodePos pos) {
		_tree = t;
		_pos = pos;
		_conf = conf;
		_parent = parent;
	}



	protected GPNode(GPTree t) {
		_tree = t;
	}



	public abstract GPNode clone(GPTree t, GPNode parent);



	protected void doClone(GPTree t, GPNode clone) {
		clone._num_descendents = _num_descendents;
		for (GPNode child : _children) {
			_children.add(child.clone(t, this));
		}
	}



	abstract public Object eval(Object context);



	ArrayList<GPNode> getChildren() {
		return _children;
	}



	public GPNode getParent() {
		return _parent;
	}



	public GPNodeConfig getConfig() {
		return _conf;
	}



	public GPNodePos getPosition() {
		return _pos;
	}



	public int getDepth() {
		return _pos.getDepth();
	}



	public int init() {
		int count = 0;
		for (Class<?> cl : _conf.getConstraints().getChildTypes()) {
			GPNode child_node = _tree.createNode(this, cl, _pos.newPos(count), _tree.getConfig().getInitializer());
			_children.add(child_node);
			count += 1 + child_node.init();
		}
		return count;
	}



	abstract public String lastEval();



	abstract public Object last();



	protected String lastEval(String value) {
		StringBuffer buf = new StringBuffer();
		if (numChildren() > 0) {
			buf.append("(");
			Iterator<GPNode> it = _children.iterator();
			while (it.hasNext()) {
				GPNode child = it.next();
				buf.append(child.lastEval());
				if (it.hasNext()) {
					buf.append(",");
				}
			}
			buf.append(")");
		}
		buf.append(value);
		return buf.toString();
	}



	public int numChildren() {
		return _children.size();
	}



	public boolean swap(GPNode child, GPNode replace) {
		for (int k = 0; k < numChildren(); k++) {
			if (_children.get(k) == child) {
				_children.set(k, replace);
				return true;
			}
		}
		return false;
	}



	@Override
	abstract public String toString();



	protected String toString(String value) {
		StringBuffer buf = new StringBuffer();
		if (numChildren() > 0) {
			buf.append("(");
			Iterator<GPNode> it = _children.iterator();
			while (it.hasNext()) {
				GPNode child = it.next();
				buf.append(child.toString());
				if (it.hasNext()) {
					buf.append(",");
				}
			}
			buf.append(")");
		}
		buf.append(value);
		return buf.toString();
	}

}
