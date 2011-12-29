package evoman.evo.structs;

import java.lang.reflect.*;
import java.util.*;

public class MethodCache {

	protected LinkedHashMap<Object, LinkedHashMap<Method, Object>> _lhm_cache = new LinkedHashMap<Object, LinkedHashMap<Method,Object>>();
	protected Map<Object, LinkedHashMap<Method, Object>> _cache = Collections.synchronizedMap(_lhm_cache);
	
	public MethodCache(){
	}
	
	public Object get(Object o, Method m){
		if (_cache.containsKey(o) && _cache.get(o).containsKey(m)){
				return _cache.get(o).get(m);
			} else{
				return null;
			}
	}
	
	public void put(Object o, Method m, Object retval){
		if (!_cache.containsKey(o)){
			_cache.put(o, new LinkedHashMap<Method,Object>());
		}
		_cache.get(o).put(m, retval);
	}
	
	public void reset(){
		_cache.clear();
	}
	
	public boolean contains(Object o, Method m){
		return ( !_cache.containsKey(o) ) ? false : (_cache.get(o).containsKey(m)) ? true : false;
	}
}
