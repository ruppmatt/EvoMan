package evoman.ec.gp.find;


import java.util.*;

import evoman.ec.gp.*;



public class FindReturnType implements FindNode {

	ArrayList<GPNode>	_rettype	= new ArrayList<GPNode>();
	Class<?>			_cl;



	public FindReturnType(Class<?> cl) {
		_cl = cl;
	}



	@Override
	public void examine(GPNode n) {
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
