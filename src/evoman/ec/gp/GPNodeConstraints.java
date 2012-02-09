package evoman.ec.gp;


import java.io.*;
import java.util.*;



public class GPNodeConstraints implements Serializable {

	private static final long	serialVersionUID	= 1L;



	public static ArrayList<Class<?>> fromArray(Class<?>[] arr) {
		ArrayList<Class<?>> retval = new ArrayList<Class<?>>();
		for (Class<?> cl : arr) {
			retval.add(cl);
		}
		return retval;
	}



	public static GPNodeConstraints scan(Class<? extends GPNode> n) {
		GPNodeDescriptor desc = n.getAnnotation(GPNodeDescriptor.class);
		if (desc != null) {
			return new GPNodeConstraints(desc);
		} else {
			return null;
		}
	}

	protected Class<?>				_return_type;

	protected ArrayList<Class<?>>	_child_types;

	protected boolean				_mutable;



	public GPNodeConstraints(GPNodeDescriptor desc) {
		_return_type = desc.return_type();
		_child_types = fromArray(desc.child_types());
	}



	public Collection<Class<?>> getChildTypes() {
		return _child_types;
	}



	public Class<?> getReturnType() {
		return _return_type;
	}



	public boolean isMutable() {
		return _mutable;
	}



	public int numChildren() {
		return _child_types.size();
	}



	public void setChildTypes(Class<?>[] types) {
		_child_types = new ArrayList<Class<?>>();
		for (Class<?> cl : types) {
			_child_types.add(cl);
		}
	}

}
