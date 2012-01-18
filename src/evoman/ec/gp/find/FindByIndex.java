package evoman.ec.gp.find;


import java.util.*;

import evoman.ec.gp.*;



public class FindByIndex implements FindNode {

	protected int		found;
	protected int		ndx;
	protected GPNode	result	= null;



	public FindByIndex(int ndx) {
		found = 0;
	}



	@Override
	public void examine(GPNode n) {
		if (ndx == found)
			result = n;
		ndx++;
	}



	@Override
	public ArrayList<GPNode> collect() {
		ArrayList<GPNode> retval = new ArrayList<GPNode>();
		retval.add(result);
		return retval;
	}



	@Override
	public boolean done() {
		return (!(result == null));
	}

}
