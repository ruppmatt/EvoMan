package evoman.tools;

import java.util.*;

public class KeyValueStore implements Configurable {

	protected LinkedHashMap<String, Parameter> _params = new LinkedHashMap<String,Parameter>();
	
	public KeyValueStore(){
	}
	
	public LinkedHashMap<String,Parameter> getParams(){
		return _params;
	}
	
	
	@Override
	public void addDefault(String name, Object value, String descr) {
		if (isSet(name)){
			_params.remove(name);
		}
		_params.put(name, new DefaultParameter(name,value,descr));
	}

	@Override
	public Object get(String name) {
		if (isSet(name)){
			return _params.get(name);
		} else
			return null;
	}

	@Override
	public void set(String name, Object value) {
		if (isSet(name))
			_params.get(name).set(value);
		else{
			Parameter p = new Parameter(name, value);
			_params.put(name,p);
		}
	}

	@Override
	public void unset(String name) {
		if (isSet(name)){
			_params.remove(name);
		}
	}

	@Override
	public PType getType(String name) {
		PType retval = PType.NONE;
		Object val = _params.get(name).value();
		if (isSet(name)){
			if (val instanceof Integer)
				retval = PType.INTEGER;
			else if (val instanceof Double)
				retval = PType.DOUBLE;
			else if (val instanceof String)
				retval = PType.STRING;
		}
		return retval;
	}

	@Override
	public Boolean isSet(String name) {
		return (_params.containsKey(name));
	}

	@Override
	public Integer I(String name) {
		return (_params.containsKey(name)) ? (Integer) _params.get(name).value() : null;
	}

	@Override
	public Double D(String name) {
		return (_params.containsKey(name)) ? (Double) _params.get(name).value() : null;
	}

	@Override
	public String S(String name) {
		return (_params.containsKey(name)) ? (String) _params.get(name).value() : null;
	}

	@Override
	public Boolean isDefault(String name) {
		Boolean retval = false;
		if ( isSet(name) && _params.get(name) instanceof DefaultParameter)
			retval = true;
		return retval;
	}

}
