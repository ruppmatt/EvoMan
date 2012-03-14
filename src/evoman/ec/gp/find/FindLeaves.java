package evoman.ec.gp.find;


import java.util.*;

import evoman.ec.gp.*;



/**
 * FindLeaves collects the leaves of a tree.
 * 
 * @author ruppmatt
 * 
 */

public class FindLeaves implements FindNode {

	protected ArrayList<GPNode>	leaves	= new ArrayList<GPNode>();



	@Override
	public void examine(GPTree t, GPNode n) {
		if (n.numChildren() == 0) {
			leaves.add(n);
		}
	}



	@Override
	public ArrayList<GPNode> collect() {
		return leaves;
	}



	@Override
	public boolean done() {
		return false;
	}

}
