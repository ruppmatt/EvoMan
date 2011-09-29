package evoman.tools;

import java.util.LinkedHashMap;

/**
 * 
 * @author ruppmatt
 *
 *		A Configurable is an extension of the Hierarchical class.  It has configuration parameters associated with it.
 */

public class Configurable extends Hierarchical {

	protected LinkedHashMap<String, Parameter> _params = new LinkedHashMap<String,Parameter>();
	

	/**
	 * Constuct a parentless Configurable
	 * @param name
	 */
	public Configurable(String name){
		this(name, null);
	}
	
	
	/**
	 * Construct a Configurable with a particular parent
	 * @param name
	 * @param parent
	 */
	public Configurable(String name, Hierarchical parent){
		super(name, parent);
	}
	
	/**
	 * Add a default parameter to this Configurable
	 * @param name
	 * @param value
	 * @param descr
	 */
	public void addDefault(String name, Object value, String descr){
		if (isSet(name)){
			_params.remove(name);
		}
		_params.put(name, new DefaultParameter(name,value,descr));
	}
	
	
	/**
	 * Get a Parameter with a particular name (null if not set)
	 * @param name
	 * @return
	 */
	public Object get(String name){
		if (isSet(name)){
			return _params.get(name);
		} else
			return null;
	}
	
	
	/**
	 * Set a key/value Parameter
	 * @param name
	 * @param value
	 */
	public void set(String name, Object value){
		if (isSet(name))
			_params.get(name).set(value);
		else{
			Parameter p = new Parameter(name, value);
			_params.put(name,p);
		}
	}
	
	
	/**
	 * Unset (remove) a parameter if it is present
	 * @param name
	 */
	public void unset(String name){
		if (isSet(name)){
			_params.remove(name);
		}
	}
	
	/**
	 * Returns the type of the Parameter value
	 * @param name
	 * @return
	 */
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
	
	/**
	 * Returns true if a parameter with a particular name is set
	 * @param name
	 * @return
	 */
	public Boolean isSet(String name){
		return (_params.containsKey(name));
	}
	
	/**
	 * Helper function to return a Parameter with a particular name to an Integer (or null if not present)
	 * @param name
	 * @return
	 */
	public Integer I(String name){
		return (_params.containsKey(name)) ? (Integer) _params.get(name).value() : null;
	}
	
	/**
	 * Helper function to return a Parameter with a particular name to an Double (or null if not present)
	 * @param name
	 * @return
	 */
	public Double D(String name){
		return (_params.containsKey(name)) ? (Double) _params.get(name).value() : null;
	}
	
	/**
	 * Helper function to return a Parameter with a particular name to an String (or null if not present)
	 * @param name
	 * @return
	 */
	public String S(String name){
		return (_params.containsKey(name)) ? (String) _params.get(name).value() : null;
	}
	

	/**
	 * Returns the length of the largest key
	 * @return
	 */
	protected int getMaxName(){
		int max = 0;
		for (String s : _params.keySet())
			max = (s.length() > max) ? s.length() : max;
		return max;
	}
	

	/**
	 * Returns true if the Paramter (if present) has a default value.
	 * @param name
	 * @return
	 */
	protected Boolean isDefault(String name){
		Boolean retval = false;
		if ( isSet(name) && _params.get(name) instanceof DefaultParameter)
			retval = true;
		return retval;
	}
	
	
	/**
	 * Converts this object to a string.
	 */
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
