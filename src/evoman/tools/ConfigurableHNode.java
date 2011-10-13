package evoman.tools;

import java.util.LinkedHashMap;

/**
 * 
 * @author ruppmatt
 *
 *		A Configurable is an extension of the Hierarchical class.  It has configuration parameters associated with it.
 */

public class ConfigurableHNode extends HNode implements Configurable {

	protected KeyValueStore _kv = new KeyValueStore();
	

	/**
	 * Constuct a parentless Configurable
	 * @param name
	 */
	public ConfigurableHNode(String name){
		this(name, null);
	}
	
	
	/**
	 * Construct a Configurable with a particular parent
	 * @param name
	 * @param parent
	 */
	public ConfigurableHNode(String name, HNode parent){
		super(name, parent);
	}
	

	/**
	 * Returns the length of the largest key
	 * @return
	 */
	protected int getMaxName(){
		int max = 0;
		for (String s : _kv.getParams().keySet())
			max = (s.length() > max) ? s.length() : max;
		return max;
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
		for (String s : _kv.getParams().keySet()){
			StringBuffer pstring = new StringBuffer();
			for (int k=0; k<level+1; k++)
				pstring.append("   ");
			pstring.append(s + " ");
			switch (getType(s)){
				case INTEGER: pstring.append( (Integer) _kv.getParams().get(s).value()); break;
				case DOUBLE:  pstring.append( (Double)  _kv.getParams().get(s).value()); break;
				case STRING:  pstring.append( (String)  _kv.getParams().get(s).value()); break;
				default: pstring.append( "null" );
			}
			pstring.append(ENDL);
			sb.append(pstring);
		}
		for (HNode h : _children.values())
			sb.append(h.toString(level+1));
		return sb.toString();
	}


	@Override
	public void addDefault(String name, Object value, String descr) {
		_kv.addDefault(name, value, descr);
	}


	@Override
	public Object get(String name) {
		return _kv.get(name);
	}


	@Override
	public void set(String name, Object value) {
		_kv.set(name,  value);
	}


	@Override
	public void unset(String name) {
		_kv.unset(name);
	}


	@Override
	public PType getType(String name) {
		return _kv.getType(name);
	}


	@Override
	public Boolean isSet(String name) {
		return _kv.isSet(name);
	}


	@Override
	public Integer I(String name) {
		return _kv.I(name);
	}


	@Override
	public Double D(String name) {
		return _kv.D(name);
	}


	@Override
	public String S(String name) {
		return _kv.S(name);
	}


	@Override
	public Boolean isDefault(String name) {
		return _kv.isDefault(name);
	}
	
	
}
