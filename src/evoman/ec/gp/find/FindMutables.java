package evoman.ec.gp.find;


import java.util.*;

import evoman.ec.gp.*;



/**
 * FindMutables collects nodes that are mutable.
 * 
 * @author ruppmatt
 * 
 */
public class FindMutables implements FindNode {

	protected ArrayList<GPNode>	mutables	= new ArrayList<GPNode>();



	@Override
	public void examine(GPTree t, GPNode n) {
		if (n instanceof GPMutableNode && t.canAlter(n)) {
			mutables.add(n);
		}
	}



	@Override
	public ArrayList<GPNode> collect() {
		return mutables;
	}



	@Override
	public boolean done() {
		return false;
	}

}
