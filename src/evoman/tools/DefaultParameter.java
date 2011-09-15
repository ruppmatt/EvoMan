package evoman.tools;

public class DefaultParameter extends Parameter {
	
	protected final Object _defvalue;
	protected final String _description;
	
	DefaultParameter(String name, Object value, String description){
		super(name,value);
		_defvalue = value;
		_description = description;
	}
	
	public Object defvalue(){
		return _defvalue;
	}
	
	public String description(){
		return _description;
	}

}
