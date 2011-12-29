package evoman.bindings.ecj;

import ec.*;
import evoman.evo.*;
import evoman.evo.structs.*;
import evoman.tools.dtype.*;

public class ObjectProblem extends Problem {

	private static final long serialVersionUID = 1L;
	
	protected Object _obj = null;
	protected MethodHNode _dict = null;
	
	ObjectProblem(Object obj, MethodHNode dict){
		_obj = obj;
		_dict = dict;
	}
	
	public Object getObject(){
		return _obj;
	}
	
	public Object getMethodDictionary(){
		return _dict;
	}
	
	public DType evaluate(String path, Object ... args){
		Object retval = _dict.evaluate(path, _obj, args);
		if (retval instanceof Boolean){
			return new BooleanType((Boolean) retval);
		} else if (retval instanceof Integer){
			return new IntegerType((Integer) retval);
		} else if (retval instanceof Double){
			return new DoubleType((Double) retval);
		} else if (retval instanceof String){
			return new StringType((String) retval);
		} else{
			return null;
		}
	}
	
}
