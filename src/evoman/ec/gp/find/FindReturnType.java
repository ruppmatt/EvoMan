package evoman.ec.gp.find;


import java.util.*;

import evoman.ec.gp.*;



/**
 * Find return type collects nodes that return a particular class.
 * 
 * @author ruppmatt
 * 
 */
public class FindReturnType implements FindNode {

	ArrayList<GPNode>	_rettype	= new ArrayList<GPNode>();
	Class<?>			_cl;



	/**
	 * 
	 * @param cl
	 *            The return type of the nodes being collected
	 */
	public FindReturnType(Class<?> cl) {
		_cl = cl;
	}



	@Override
	public void examine(GPTree t, GPNode n) {
		if (n.getConfig().getConstraints().getReturnType() == _cl)
			_rettype.add(n);
	}



	@Override
	public ArrayList<GPNode> collect() {
		return _rettype;
	}



	@Override
	public boolean done() {
		return false;
	}

}
