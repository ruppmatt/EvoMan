package evoman.evo.structs;


import java.lang.reflect.*;
import java.util.*;

import evoict.*;
import evoict.io.*;
import evoman.evo.*;

public class MethodHNode implements EMState {

	LinkedHashMap<String,Method> _lhm_methods = new LinkedHashMap<String,Method>();
	Map<String,Method> _methods = Collections.synchronizedMap(_lhm_methods);
	LinkedHashMap<String,MethodHNode> _children = new LinkedHashMap<String,MethodHNode>();
	MethodCache _cache = new MethodCache();
	protected int _total_weight = 0;
	protected int _node_weight = 0;
	EMState _parent;
	
	
	public MethodHNode(EMState parent){
		_parent = parent;
	}
	
	public boolean addMethod(String path, Method m){
		String prefix = Resolver.getPrefix(path);
		String name = Resolver.extractPrefix(path);
		if (prefix == null){
			if (_methods.containsKey(name)){
				_total_weight++;
				_node_weight++;
			}
			_methods.put(name, m);
			return true;
		} else {
			if (!_children.containsKey(prefix)){
				_children.put(prefix, new MethodHNode(this));
			} 				
			boolean added =  ((MethodHNode) _children.get(prefix)).addMethod(name, m);
			if (added == true){
				_total_weight++;
			}
			return added;
		}
	}
	
	public Object evaluate(String path, Object o, Object ... args ){
		String prefix = Resolver.getPrefix(path);
		String name   = Resolver.extractPrefix(path);
		if (prefix == null){
			if (_methods.containsKey(name)){
				return doMethod(_methods.get(name),o,args);
			} else {
				getNotifier().warn("Missing method: " + path);
				return null;
			}
		} else {
			if (_children.containsKey(prefix)){
				return ((MethodHNode) _children.get(prefix)).evaluate(name, o, args);
			} else {
				getNotifier().warn("Missing method: " + path);
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
	
	public String getRandomPath(){
		String path = null;
		int selection = getRandom().nextInt(_total_weight);
		if (selection < _node_weight){
			path = (String) _methods.keySet().toArray()[selection];
		} else {
			selection -= _node_weight;
			for (MethodHNode child : _children.values()){
				if (selection < child._node_weight){
					path = child.getRandomPath();
					break;
				} 
				selection -= child._node_weight;
			}
		}
		return path;
	}
	
	protected void resetCache(){
		_cache.reset();
		for (MethodHNode c : _children.values()){
				c.resetCache();
		}
	}

	@Override
	public EMState getESParent() {
		return _parent;
	}

	@Override
	public void init() {
	}

	@Override
	public void finish() {
	}

	@Override
	public MersenneTwisterFast getRandom() {
		return _parent.getRandom();
	}

	@Override
	public EMThreader getThreader() {
		return _parent.getThreader();
	}

	@Override
	public Notifier getNotifier() {
		return _parent.getNotifier();
	}
	
	public String toString(){
		StringBuffer buf = new StringBuffer();
		//ToDo
		return buf.toString();
		
	}

}
