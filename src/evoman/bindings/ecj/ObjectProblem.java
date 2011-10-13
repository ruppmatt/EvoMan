package evoman.bindings.ecj;

import ec.*;

public class ObjectProblem extends Problem {


	private static final long serialVersionUID = 1L;
	
	protected Object _obj = null;
	
	ObjectProblem(Object obj){
		_obj = obj;
	}
	
	public Object getObject(){
		return _obj;
	}
	
}
