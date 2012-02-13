package evoman.ec.gp.find;


import java.util.*;

import evoman.ec.gp.*;



/**
 * FindAllNodes selects all nodes examined.
 * 
 * @author ruppmatt
 * 
 */

public class FindAllNodes implements FindNode {

	ArrayList<GPNode>	nodes	= new ArrayList<GPNode>();



	@Override
	public void examine(GPNode n) {
		nodes.add(n);
	}



	@Override
	public ArrayList<GPNode> collect() {
		return nodes;
	}



	@Override
	public boolean done() {
		return false;
	}
}
