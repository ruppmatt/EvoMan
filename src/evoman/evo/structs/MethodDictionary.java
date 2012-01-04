package evoman.evo.structs;

import java.lang.reflect.*;

import evoict.graphs.*;

public class MethodDictionary extends EMHNode {

	protected MethodHNode _root;
	
	public MethodDictionary(String name){
		super(name,null);
	}
	
	public MethodDictionary(String name, HNode parent) {
		super(name, parent);
		_root = new MethodHNode(this);
	}
	
	public MethodHNode getRoot(){
		return _root;
	}
	
	public void addMethod(String path, Method m){
		_root.addMethod(path, m);
	}
	
	public Object evaluate(String path, Object o, Object ... args){
		return _root.evaluate(path,o,args);
	}
	
	public String getRandomPath(){
		return _root.getRandomPath();
	}
	
	public void resetCache(){
		_root.resetCache();
	}
	
	
}
