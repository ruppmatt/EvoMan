package evoman.ec.gp.find;


import java.util.*;

import evoman.ec.gp.*;



public class FindMutables implements FindNode {

	protected ArrayList<GPNode>	mutables	= new ArrayList<GPNode>();



	@Override
	public void examine(GPNode n) {
		if (n instanceof GPMutableNode) {
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
