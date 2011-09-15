package evoman.tools;

import java.util.LinkedHashMap;

public class Configurable extends Hierarchical {

	protected LinkedHashMap<String, Parameter> _params = new LinkedHashMap<String,Parameter>();
	
	
	public Configurable(String name){
		this(name, null);
	}
	
	
	public Configurable(String name, Hierarchical parent){
		super(name, parent);
	}
	
	
	public void addDefault(String name, Object value, String descr){
		if (isSet(name)){
			_params.remove(name);
		}
		_params.put(name, new DefaultParameter(name,value,descr));
	}
	
	
	public Object get(String name){
		if (isSet(name)){
			return _params.get(name);
		} else
			return null;
	}
	
	
	public void set(String name, Object value){
		if (isSet(name))
			_params.get(name).set(value);
		else{
			Parameter p = new Parameter(name, value);
			_params.put(name,p);
		}
	}
	
	
	public void unset(String name){
		if (isSet(name)){
			_params.remove(name);
		}
	}
	
	
	public PType getType(String name){
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
	
	public Boolean isSet(String name){
		return (_params.containsKey(name));
	}
	
	public Integer I(String name){
		return (_params.containsKey(name)) ? (Integer) _params.get(name).value() : null;
	}
	
	public Double D(String name){
		return (_params.containsKey(name)) ? (Double) _params.get(name).value() : null;
	}
	
	public String S(String name){
		return (_params.containsKey(name)) ? (String) _params.get(name).value() : null;
	}
	

	
	protected int getMaxName(){
		int max = 0;
		for (String s : _params.keySet())
			max = (s.length() > max) ? s.length() : max;
		return max;
	}
	

	
	protected Boolean isDefault(String name){
		Boolean retval = false;
		if ( isSet(name) && _params.get(name) instanceof DefaultParameter)
			retval = true;
		return retval;
	}
	
	
	public String toString(int level){
		StringBuffer sb = new StringBuffer();
		
		//Print the type and name of the configurable
		for (int k=0; k<level; k++)
			sb.append("   ");
		sb.append(this.getClass().getSimpleName() + " " + getName() + ENDL);
		
		//Indent and print each paramter
		for (String s : _params.keySet()){
			StringBuffer pstring = new StringBuffer();
			for (int k=0; k<level+1; k++)
				pstring.append("   ");
			pstring.append(s + " ");
			switch (getType(s)){
				case INTEGER: pstring.append( (Integer) _params.get(s).value()); break;
				case DOUBLE:  pstring.append( (Double)  _params.get(s).value()); break;
				case STRING:  pstring.append( (String)  _params.get(s).value()); break;
				default: pstring.append( "null" );
			}
			pstring.append(ENDL);
			sb.append(pstring);
		}
		for (Hierarchical h : _children.values())
			sb.append(h.toString(level+1));
		return sb.toString();
	}
	
	
}
