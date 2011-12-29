package evoman.bindings.ecj;

import java.util.*;

import evoman.tools.*;

public class GPOperator extends ConfigurableHNode implements Constants{
	
	protected String _operator_path = null;
	protected KeyValueStore _kv = new KeyValueStore();
	protected ArrayList<String> _child_types = new ArrayList<String>();
	protected String _node_constraint = null;
	
	public GPOperator(String name){
		super(name);
		_kv.addDefault("return_type", "DType", "Default return type of operator.");
		_kv.addDefault("path", "null", "Path to operator class.");
	}
	
	public String getReturnType(){
		return _kv.S("return_type");
	}
	
	
	public String getPath(){
		return _kv.S("path");
	}
	
	public void setReturnType(String type){
		_kv.set("return_type", type);
	}
	
	
	public void setPath(String path){
		_kv.set("path", path);
	}
	
	public void addChild(String returntype){
		_child_types.add(returntype);
	}
	
	public void setNumChildren(int num){
		_child_types.clear();
		for (int k = 0; k < num; k++)
			_child_types.add("DType");
	}
	
	public void setConstraint(String name){
		_node_constraint = name;
	}
	
	public String getConstraint(){
		return _node_constraint;
	}
	
	public int getNumChildren(){
		return _child_types.size();
	}
	
	public ArrayList<String> getChildTypes(){
		return _child_types;
	}
	

	@Override
	public void addDefault(String name, Object value, String descr) {
		_kv.addDefault(name,value,descr);
		
	}

	@Override
	public Object get(String name) {
		if (name == "child_type"){
			return _child_types;
		}
		return _kv.get(name);
	}

	@Override
	public void set(String name, Object value) {
		if (name == "child_type"){
			_child_types.add((String) value);
		}
		_kv.set(name,value);
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
