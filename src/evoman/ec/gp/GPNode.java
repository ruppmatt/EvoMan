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



	public GPNode(GPTree t, GPNodeConfig conf, GPNodePos pos) {
		_tree = t;
		_pos = pos;
		_conf = conf;
	}



	abstract public Object eval(Object context);



	ArrayList<GPNode> getChildren() {
		return _children;
	}



	public GPNodeConfig getConfig() {
		return _conf;
	}



	public GPNodePos getPosition() {
		return _pos;
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
