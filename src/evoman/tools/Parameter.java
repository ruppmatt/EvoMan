package evoman.tools;

public class Parameter {
	
	protected final String _key;
	Object _value;
	
	public Parameter(String key, Object value){
		_key = key;
		_value = value;
	}
	
	public Object value(){
		return _value;
	}
	
	public String key(){
		return _key;
	}
	
	public void set(Object value){
		_value = value;
	}

}
