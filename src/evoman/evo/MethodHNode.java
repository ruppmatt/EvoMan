package evoman.evo;


import java.lang.reflect.*;
import java.util.*;

import sun.reflect.generics.scope.*;

import evoman.tools.*;

public class MethodHNode extends EMHNode {

	LinkedHashMap<String,Method> _lhm_methods = new LinkedHashMap<String,Method>();
	Map<String,Method> _methods = Collections.synchronizedMap(_lhm_methods);
	MethodCache _cache = new MethodCache();
	
	public MethodHNode(String name){
		this(name, null);
	}
	
	public MethodHNode(String name, HNode parent) {
		super(name, parent);
	}
	
	public boolean addMethod(String path, Method m){
		String prefix = namePrefix(path);
		String name = extractPrefix(path);
		if (prefix == null){
			_methods.put(name, m);
			return true;
		} else {
			if (!_children.containsKey(prefix)){
				_children.put(prefix, new MethodHNode(prefix));
			} 				
			return ((MethodHNode) _children.get(prefix)).addMethod(name, m);
		}
	}
	
	public Object evaluate(String path, Object o, Object ... args ){
		String prefix = namePrefix(path);
		String name   = extractPrefix(path);
		if (prefix == null){
			if (_methods.containsKey(name)){
				return doMethod(_methods.get(name),o,args);
			} else {
				warn("Missing method: " + path);
				return null;
			}
		} else {
			if (_children.containsKey(prefix)){
				return ((MethodHNode) _children.get(prefix)).evaluate(name, o, args);
			} else {
				warn("Missing method: " + path);
				return null;
			}
		}
	}
	
	protected Object doMethod(Method m, Object o, Object ... args){
		if (args.length == 0 && _cache.contains(o,m)){
			return _cache.get(o,m);
		} else {
			Object retval = null;
			try {
				retval = m.invoke(o, args);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			if (args.length == 0){
				_cache.put(o,m,retval);
			}
			return retval;
		}	
	}
	
	protected void resetCache(){
		_cache.reset();
		for (HNode c : _children.values()){
			if (c instanceof MethodHNode){
				((MethodHNode) c).resetCache();
			}
		}
	}

}
