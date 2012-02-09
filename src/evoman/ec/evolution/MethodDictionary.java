package evoman.ec.evolution;


import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import evoict.*;
import evoict.reflection.*;



public class MethodDictionary implements Serializable {

	private static final long		serialVersionUID	= 1L;
	protected DescriptionManager	_manager			= new DescriptionManager();
	protected final Class<?>		_context_class;



	public MethodDictionary(Class<?> cl) throws BadConfiguration {
		_context_class = cl;
		_manager.scanClass(cl);
	}



	public void addMethod(String name, Class<?> cl, Method m) throws BadConfiguration {
		_manager.addMethod(name, m, cl);
	}



	public Object evaluate(String path, Object o, Object... args) throws UnresolvableException {
		return _manager.resolve(path, o, args);
	}



	public String getRandomPath(RandomGenerator gen, int max_length) throws BadConfiguration {
		Class<?> lookup = _context_class;
		String prefix = "";
		ArrayList<String> paths = enumeratePaths(prefix, lookup, max_length);
		int ndx = gen.nextInt(paths.size());
		return paths.get(ndx);
	}



	protected ArrayList<String> enumeratePaths(String prefix, Class<?> lookup, int max_length) {
		return null;
	}

}
