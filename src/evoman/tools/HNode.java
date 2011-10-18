package evoman.tools;

import java.util.*;

/**
 * 
 * @author ruppmatt
 *
 *		HNode objects may have a single parent and may have multiple children. 
 * 		They also have a local name and a full name (to place them in their hierarchy.
 */

public class HNode extends Identifiable implements Printable {
	
	protected HNode _parent = null;
	protected String       _full_name; 
	protected LinkedHashMap<String, HNode> _lhm_children  = new LinkedHashMap<String,HNode>();
	protected Map<String,HNode> _children = Collections.synchronizedMap(_lhm_children);

	/**
	 * Construct a HNode with no parent.
	 * @param name
	 */
	public HNode(String name){
		this(name, null);
	}
	
	/**
	 * Construct a HNode with a particular parent
	 * @param name
	 * @param parent
	 */
	public HNode(String name, HNode parent){
		super(name);
		_parent = parent;
		if (parent != null)
			_full_name = parent.getFullName() + "." + name;
		else
			_full_name = name;
	}
	
	
	/**
	 * Returns the full name (path) of the Hierarhcical
	 * @return
	 */
	public String getFullName(){
		return _full_name;
	}
	
	/**
	 * Return the local name of the Hierarhcical
	 */
	public String getName(){
		return _name;
	}
	
	/**
	 * Add a child
	 * @param child : HNode
	 */
	public void addChild(HNode child){
		_children.put(child.getName(), child);
	}
	
	/**
	 * Remove a child if present
	 * @param child
	 */
	public void removeChild(HNode child){
		String name = child.getName();
		if (_children.containsKey(name))
			_children.remove(name);
		child._full_name = child.getName();
	}
	
	/**
	 * Move this object to another HNode as a parent
	 * @param parent
	 */
	public void moveTo(HNode parent){
		if (_parent != null)
			_parent.removeChild(this);
		_parent = parent;
		_full_name = parent.getFullName() + getName();
	}
	
	/**
	 * Get a child with a particular name
	 * @param name
	 * @return
	 */
	public HNode child(String name){
		if ( _children.containsKey(name) ){
			return _children.get(name);
		} else {
			return null;
		}
	}
	
	/**
	 * Returns true if a child with a particular name is present
	 * @param name
	 * @return
	 */
	public boolean contains(String name){
		return _children.containsKey(name);
	}
	
	/**
	 * Returns a this object's children
	 * @return
	 */
	public Collection<HNode> getChildren(){
		return _children.values();
	}
	
	/**
	 * Returns this object's parent
	 * @return
	 */
	public HNode parent(){
		return _parent;
	}
	
	/**
	 * Returns the root of this Hierarchy
	 * @return
	 */
	public HNode root(){
		if (_parent == null)
			return this;
		else
			return _parent.root();
	}
	
	/**
	 * Resolve a name (beginning with the current node) to a HNode object; null returned if not found
	 * @param name
	 * @return
	 */
	public HNode resolveName(String name){
		String prefix = namePrefix(name);	
		if (prefix == null && _children.containsKey(name)){
			return _children.get(name);
		} else if (prefix != null && _children.containsKey(prefix)){
			return _children.get(prefix).resolveName(extractPrefix(name));
		} else{
			return null;
		}
	}
	

	/**
	 * Return a prefix of a path or null if not present
	 * @param name
	 * @return
	 */
	protected static String namePrefix(String name){
		int ndx = name.indexOf('.');
		return (ndx < 0) ? null : name.substring(0,ndx);
	}
	
	/**
	 * Return a prefix-free path
	 * @param name
	 * @return
	 */
	protected static String extractPrefix(String name){
		int ndx = name.indexOf('.');
		return (ndx < 0) ? name : name.substring(ndx+1);
	}
	
	/**
	 * Print this Hierarchy as a string
	 */
	public String toString(){
		return toString(0);

	}
	
	/**
	 * Internal toString that handles proper indentation levels
	 * @param level
	 * @return
	 */
	protected String toString(int level){
		StringBuffer sb = new StringBuffer();
		for (int k=0; k<level; k++){
			sb.append("   ");
		}
		sb.append(this.getClass().getSimpleName() + " " + getName() + " " + _children.size() + ENDL);
		for (HNode h : _children.values())
			sb.append(h.toString(level+1));
		return sb.toString();
	}
	
}
