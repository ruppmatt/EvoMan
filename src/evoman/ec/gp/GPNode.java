package evoman.ec.gp;


import java.io.*;
import java.util.*;

import evoman.evo.*;



public abstract class GPNode implements Constants, Serializable {

	private static final long	serialVersionUID	= 1L;
	protected GPTree			_tree;
	protected ArrayList<GPNode>	_children			= new ArrayList<GPNode>();
	protected GPNodeConfig		_conf;
	protected int				_depth;
	protected boolean			_evaluated;



	public GPNode(GPTree t, GPNodeConfig conf, int depth) {
		_tree = t;
		_depth = depth;
		_conf = conf;
		_evaluated = false;
	}



	abstract public Object eval(Object context);



	ArrayList<GPNode> getChildren() {
		return _children;
	}



	public GPNodeConfig getConfig() {
		return _conf;
	}



	public int getDepth() {
		return _depth;
	}



	public void init() {
		for (Class<?> cl : _conf.getConstraints().getChildTypes()) {
			GPNode child_node = _tree.initCreateNode(this, cl);
			_children.add(child_node);
			child_node.init();
		}
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



	public abstract void mutate();



	public int numChildren() {
		return _children.size();
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
