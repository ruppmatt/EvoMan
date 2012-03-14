package evoman.ec.gp;


import java.lang.reflect.*;
import java.util.*;

import evoict.*;
import evoman.config.*;
import evoman.evo.structs.*;



/**
 * The GPNodeDirectory contains a listing of GP node configurations. It also
 * performs validation checks on the nodes and provides an interface to get
 * different types of nodes out of the directory.
 * 
 * @author ruppmatt
 * 
 */
@ConfigRegister(name = "AvailableNodes")
public class GPNodeDirectory implements Cloneable {

	// A list of all configuration objects
	ArrayList<GPNodeConfig>						_all		= new ArrayList<GPNodeConfig>();

	// Sorted by return type
	HashMap<Class<?>, ArrayList<GPNodeConfig>>	_functions	= new HashMap<Class<?>, ArrayList<GPNodeConfig>>();

	HashMap<Class<?>, ArrayList<GPNodeConfig>>	_terminals	= new HashMap<Class<?>, ArrayList<GPNodeConfig>>();



	/**
	 * Create a new GPNode directory. An EMState parent is required.
	 * 
	 */
	public GPNodeDirectory() {
	}



	/**
	 * Clone the directory.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object clone() {
		GPNodeDirectory clone = new GPNodeDirectory();
		clone._all = (ArrayList<GPNodeConfig>) _all.clone();
		clone._functions = (HashMap<Class<?>, ArrayList<GPNodeConfig>>) _functions.clone();
		clone._terminals = (HashMap<Class<?>, ArrayList<GPNodeConfig>>) _terminals.clone();
		return clone;
	}



	/**
	 * Add a new node configuration to the directory. The configuration attempt
	 * be validated. Invalid configurations will throw a BadConfiguration.
	 * 
	 * @param conf
	 *            GPNode configuration to add
	 * @throws BadConfiguration
	 *             If there is something wrong with the configuration
	 */
	@ConfigOptional()
	public void addNodeConfig(GPNodeConfig conf) throws BadConfiguration {

		// Check the contents of the configuration
		if (conf == null) {
			throw new BadConfiguration("Cannot add GPNode with null configuration.");
		} else if (conf.getConstraints() == null) {
			throw new BadConfiguration("Cannot add GPNode with no constraints (is there a missing GPNodeDescriptor?)");
		} else if (conf.getNodeClass() == null) {
			throw new BadConfiguration("Cannot add GPNode with null class");
		}

		// Try to validate the configuration
		try {
			validateNodeConfig(conf);
		} catch (BadConfiguration e) {
			throw e;
		}

		// Collect information about the configuration for putting it into the
		// directory correctly
		GPNodeConstraints cnstr = conf.getConstraints();
		Class<?> ret = cnstr.getReturnType();
		int num_child = cnstr.numChildren();

		// Add to the directory
		_all.add(conf);

		if (num_child == 0) { // Terminal check
			if (!_terminals.containsKey(ret)) {
				_terminals.put(ret, new ArrayList<GPNodeConfig>());
			}
			_terminals.get(ret).add(conf);
		} else { // Function (internal) check
			if (!_functions.containsKey(ret)) {
				_functions.put(ret, new ArrayList<GPNodeConfig>());
			}
			_functions.get(ret).add(conf);
		}

	}



	/**
	 * Get a random configuration from the directory.
	 * 
	 * @return
	 */
	public GPNodeConfig random(EMState state) {
		if (_all.size() == 0) {
			return null;
		} else {
			return random(state, _all);
		}
	}



	/**
	 * Get a random configuration from a list of configurations
	 * 
	 * @param avail
	 *            List of configurations to select randomly from
	 * @return
	 */
	public GPNodeConfig random(EMState state, ArrayList<GPNodeConfig> avail) {
		int num = avail.size();
		int ndx = state.getRandom().nextInt(num);
		return avail.get(ndx);
	}



	/**
	 * Get a random terminal node configuration
	 * 
	 * @param return_type
	 *            The required return type of the terminal
	 * @return
	 */
	public GPNodeConfig randomTerminal(EMState state, Class<?> return_type) {
		if (!_terminals.containsKey(return_type)) {
			return null;
		} else {
			return random(state, _terminals.get(return_type));
		}
	}



	/**
	 * Get a random function (internal) node configuration
	 * 
	 * @param return_type
	 *            The required return type of the function (internal) node
	 * @return
	 */
	public GPNodeConfig randomFunction(EMState state, Class<?> return_type) {
		if (!_functions.containsKey(return_type)) {
			return null;
		} else {
			return random(state, _functions.get(return_type));
		}
	}



	/**
	 * Validate a node's configuration locally. If no static validate(..)
	 * function is
	 * present, no action is performed.
	 * 
	 * @param conf
	 *            Node configuration to validate
	 * @throws BadConfiguration
	 *             If there is a problem using the validation function or if the
	 *             configuration isn't valid.
	 */
	public static void validateNodeConfig(GPNodeConfig conf) throws BadConfiguration {
		try {
			Method m = conf.getNodeClass().getDeclaredMethod("validate", GPNodeConfig.class);
			try {
				m.invoke(null, conf);
			} catch (InvocationTargetException e) {
				throw (((BadConfiguration) e.getCause()));
			} catch (Exception e) {
				throw new BadConfiguration("Unable to invoke validation method for " + conf.getNodeClass());
			}
		} catch (SecurityException e) {
			throw new BadConfiguration("Unable to validate node configuration.  validate(...) is not visible for " +
					conf.getNodeClass());
		} catch (NoSuchMethodException e) {
		}
	}



	/**
	 * Validates the entire node directory
	 * 
	 * @throws BadConfiguration
	 */
	public void validate() throws BadConfiguration {
		BadConfiguration bc = new BadConfiguration();
		for (GPNodeConfig conf : _all) {
			try {
				validateNodeConfig(conf);
			} catch (BadConfiguration valid_bc) {
				bc.append(valid_bc.getMessage());
			}
			for (Class<?> cl : conf.getConstraints().getChildTypes()) {
				if (!_terminals.containsKey(cl)) {
					bc.append(conf.getNodeClass().toString() + " requires a terminal node of class " + cl.getName());
				}
			}
		}
		bc.validate();
	}

}
