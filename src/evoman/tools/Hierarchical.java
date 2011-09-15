package evoman.tools;

import java.util.Collection;
import java.util.LinkedHashMap;

public class Hierarchical extends Identifiable implements Printable {
	
	protected Hierarchical _parent = null;
	protected String       _full_name; 
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
		child._full_name = child.getName();
	}
	
	public void moveTo(Hierarchical parent){
		if (_parent != null)
			_parent.removeChild(this);
		_parent = parent;
		_full_name = parent.getFullName() + getName();
	}
	
	public Hierarchical child(String name){
		if ( _children.containsKey(name) ){
			return _children.get(name);
		} else {
			return null;
		}
	}
	
	public boolean contains(String name){
		return _children.containsKey(name);
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
		if (prefix == null && _children.containsKey(name)){
			return _children.get(name);
		} else if (prefix != null && _children.containsKey(prefix)){
			return _children.get(prefix).resolveName(extractPrefix(name));
		} else{
			return null;
		}
	}
	

	
	protected static String namePrefix(String name){
		int ndx = name.indexOf('.');
		return (ndx < 0) ? null : name.substring(0,ndx);
	}
	
	protected static String extractPrefix(String name){
		int ndx = name.indexOf('.');
		return (ndx < 0) ? name : name.substring(ndx+1);
	}
	
	
	public String toString(){
		return toString(0);

	}
	
	protected String toString(int level){
		StringBuffer sb = new StringBuffer();
		for (int k=0; k<level; k++){
			sb.append("   ");
		}
		sb.append(this.getClass().getSimpleName() + " " + getName() + " " + _children.size() + ENDL);
		for (Hierarchical h : _children.values())
			sb.append(h.toString(level+1));
		return sb.toString();
	}
	
}
