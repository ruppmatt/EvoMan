package evoman.ec.gp.find;


import java.util.*;

import evoman.ec.gp.*;



public class FindLeaves implements FindNode {

	protected ArrayList<GPNode>	leaves	= new ArrayList<GPNode>();



	@Override
	public void examine(GPNode n) {
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
