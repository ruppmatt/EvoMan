package evoman.tools;

import java.util.Collection;
import java.util.LinkedHashMap;

public class Hierarchical extends Identifiable{
	
	protected Hierarchical _parent = null;
	protected final String       _full_name; 
	protected LinkedHashMap<String, Hierarchical> _children = new LinkedHashMap<String,Hierarchical>();

	public Hierarchical(String name){
		this(name, null);
	}
	
	public Hierarchical(String name, Hierarchical parent){
		super(name);
		_parent = parent;
		if (parent != null)
			_full_name = parent.getFullName() + "." + name;
		else
			_full_name = name;
	}
	
	
	public String getFullName(){
		return _full_name;
	}
	
	
	public String getName(){
		return _name;
	}
	
	
	public void addChild(Hierarchical child){
		_children.put(child.getName(), child);
	}
	
	public void removeChild(Hierarchical child){
		String name = child.getName();
		if (_children.containsKey(name))
			_children.remove(name);
	}
	
	public Hierarchical child(String name){
		if ( _children.containsKey(name) ){
			return _children.get(name);
		} else {
			return null;
		}
	}
	
	public Collection<Hierarchical> getChildren(){
		return _children.values();
	}
	
	public Hierarchical parent(){
		return _parent;
	}
	
	public Hierarchical root(){
		if (_parent == null)
			return this;
		else
			return _parent.root();
	}
	
	public Hierarchical resolveName(String name){
		String prefix = namePrefix(name);
		if (prefix == null && name == _name){
			return this;
		} else if (_children.containsKey(prefix)){
			return _children.get(prefix).resolveName(extractPrefix(name));
		} else {
			return null;
		}
	}
	
	protected static String namePrefix(String name){
		int ndx = name.indexOf('.');
		return (ndx < 0) ? null : name.substring(ndx+1);
	}
	
	protected static String extractPrefix(String name){
		int ndx = name.indexOf('.');
		return (ndx < 0) ? name : name.substring(0,ndx);
	}
	
}
