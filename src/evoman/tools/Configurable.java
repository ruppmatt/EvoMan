package evoman.tools;

import java.util.LinkedHashMap;

public class Configurable extends Hierarchical {

	protected LinkedHashMap<String, Object> _params = new LinkedHashMap<String,Object>();
	protected LinkedHashMap<String, PType> _types  = new LinkedHashMap<String,PType>();
	
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
	
	public PType getType(String name){
		return (_types.containsKey(name)) ? _types.get(name) : PType.NONE;
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
	
	public String toString(){
		return toString(1);
	}
	
	
	public String toString(int level){
		StringBuffer sb = new StringBuffer();
		for (int k=0; k<level; k++)
			sb.append("   ");
		sb.append(this.getClass().getSimpleName() + " " + getName() + ENDL);
		for (String s : _params.keySet()){
			for (int k=0; k<level+1; k++)
				sb.append("   ");
			sb.append(s + " ");
			switch (_types.get(s)){
				case INTEGER: sb.append( (Integer) _params.get(s) + ENDL); break;
				case DOUBLE:  sb.append( (Double)  _params.get(s) + ENDL); break;
				case STRING:  sb.append( (String)  _params.get(s) + ENDL); break;
			}
		}
		for (Hierarchical h : _children.values())
			h.toString(level+1);
		return sb.toString();
	}
	
	
}
