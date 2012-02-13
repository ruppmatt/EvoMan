package evoman.ec.gp;


import java.io.*;
import java.util.*;



/**
 * GPNodeConstraints contain information about the return type, child types, and
 * number of children at each node. These values an be read from the @GPNodeDescriptor
 * information at the top of a GPNode class or can be changed at run-time.
 * 
 * @author ruppmatt
 * 
 */
public class GPNodeConstraints implements Serializable {

	private static final long	serialVersionUID	= 1L;



	/**
	 * Helper function to convert a normal-array to an array list
	 * 
	 * @param arr
	 * @return
	 */
	public static ArrayList<Class<?>> fromArray(Class<?>[] arr) {
		ArrayList<Class<?>> retval = new ArrayList<Class<?>>();
		for (Class<?> cl : arr) {
			retval.add(cl);
		}
		return retval;
	}



	/**
	 * Scans a GPNode class for constraint annotations
	 * 
	 * @param n
	 *            Class to check for constraint annotations
	 * @return
	 *         Annotated constraints for a class
	 */
	public static GPNodeConstraints scan(Class<? extends GPNode> n) {
		GPNodeDescriptor desc = n.getAnnotation(GPNodeDescriptor.class);
		if (desc != null) {
			return new GPNodeConstraints(desc);
		} else {
			return null;
		}
	}

	protected Class<?>		_return_type;	// Node return type

	protected Class<?>[]	_child_types;	// Child return types

	protected boolean		_mutable;		// True if the node is
											// directly mutable



	/**
	 * Create a GPNodeConstraint from annotation information
	 * 
	 * @param desc
	 */
	public GPNodeConstraints(GPNodeDescriptor desc) {
		_return_type = desc.return_type();
		_child_types = desc.child_types();
	}



	/**
	 * Get the child return types
	 * 
	 * @return
	 */
	public Class<?>[] getChildTypes() {
		return _child_types;
	}



	/**
	 * Get the return type for the node
	 * 
	 * @return
	 */
	public Class<?> getReturnType() {
		return _return_type;
	}



	/**
	 * Check to see if the node supports mutation
	 * 
	 * @return
	 */
	public boolean isMutable() {
		return _mutable;
	}



	/**
	 * Return the number of children
	 * 
	 * @return
	 */
	public int numChildren() {
		return _child_types.length;
	}



	/**
	 * Alter the return type of the children
	 * 
	 * @param types
	 */
	public void setChildTypes(Class<?>[] types) {
		for (int k = 0; k < _child_types.length; k++) {
			_child_types[k] = types[k];
		}
	}



	/**
	 * Alter the return type of the node
	 * 
	 * @param type
	 */
	public void setReturnType(Class<?> type) {
		_return_type = type;
	}

}
