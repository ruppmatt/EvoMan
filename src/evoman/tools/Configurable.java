package evoman.tools;

import java.util.LinkedHashMap;

public class Configurable extends Hierarchical {

	protected LinkedHashMap<String, Object> _params = new LinkedHashMap<String,Object>();
	
	public Configurable(String name){
		this(name, null);
	}
	
	public Configurable(String name, Hierarchical parent){
		super(name, parent);
	}
	
	public Object get(String name){
		if (_params.containsKey(name)){
			return _params.get(name);
		} else
			return null;
	}
	
	public Integer I(String name){
		return (_params.containsKey(name)) ? (Integer) _params.get(name) : null;
	}
	
	public Double D(String name){
		return (_params.containsKey(name)) ? (Double) _params.get(name) : null;
	}
	
	public String S(String name){
		return (_params.containsKey(name)) ? (String) _params.get(name) : null;
	}
	
	
}
