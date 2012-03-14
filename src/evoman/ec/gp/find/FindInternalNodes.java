package evoman.ec.gp.find;


import java.util.*;

import evoman.ec.gp.*;



/**
 * FindInternalNodes collects all non-terminal nodes in a tree.
 * 
 * @author ruppmatt
 * 
 */
public class FindInternalNodes implements FindNode {

	ArrayList<GPNode>	_internal	= new ArrayList<GPNode>();



	@Override
	public void examine(GPTree t, GPNode n) {
		if (n.getConfig().getConstraints().numChildren() > 0) {
			_internal.add(n);
		}
	}



	@Override
	public ArrayList<GPNode> collect() {
		return _internal;
	}



	@Override
	public boolean done() {
		return false;
	}

}
