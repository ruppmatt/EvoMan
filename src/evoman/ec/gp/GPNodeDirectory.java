package evoman.ec.gp;


import java.lang.reflect.*;
import java.util.*;

import evoict.io.*;
import evoman.evo.*;
import evoman.evo.structs.*;



public class GPNodeDirectory implements EMState {

	EMState										_parent;

	ArrayList<GPNodeConfig>						_all		= new ArrayList<GPNodeConfig>();

	// Sorted by return type
	HashMap<Class<?>, ArrayList<GPNodeConfig>>	_functions	= new HashMap<Class<?>, ArrayList<GPNodeConfig>>();

	HashMap<Class<?>, ArrayList<GPNodeConfig>>	_terminals	= new HashMap<Class<?>, ArrayList<GPNodeConfig>>();



	public GPNodeDirectory(EMState parent) {
		_parent = parent;
	}



	public boolean addNodeConfig(GPNodeConfig conf) {
		if (conf == null) {
			getNotifier().fatal("Cannot add GPNode with null configuration.");
		} else if (conf.getConstraints() == null) {
			getNotifier().fatal("Cannot add GPNode with no constraints (is there a missing GPNodeDescriptor?)");
		} else if (conf.getNodeClass() == null) {
			getNotifier().fatal("Cannot add GPNode with null class");
		}
		GPNodeConstraints cnstr = conf.getConstraints();
		Class<?> ret = cnstr.getReturnType();
		int num_child = cnstr.numChildren();

		_all.add(conf);

		if (validateConfig(conf) == false)
			return false;

		if (num_child == 0) {
			if (!_terminals.containsKey(ret)) {
				_terminals.put(ret, new ArrayList<GPNodeConfig>());
			}
			_terminals.get(ret).add(conf);
		} else {
			if (!_functions.containsKey(ret)) {
				_functions.put(ret, new ArrayList<GPNodeConfig>());
			}
			_functions.get(ret).add(conf);
		}

		return true;
	}



	public GPNodeConfig random() {
		if (_all.size() == 0) {
			return null;
		} else {
			return random(_all);
		}
	}



	public GPNodeConfig random(ArrayList<GPNodeConfig> avail) {
		int num = avail.size();
		int ndx = getRandom().nextInt(num);
		return avail.get(ndx);
	}



	public GPNodeConfig randomTerminal(Class<?> return_type) {
		if (!_terminals.containsKey(return_type)) {
			return null;
		} else {
			return random(_terminals.get(return_type));
		}
	}



	public GPNodeConfig randomFunction(Class<?> return_type) {
		if (!_functions.containsKey(return_type)) {
			return null;
		} else {
			return random(_functions.get(return_type));
		}
	}



	public boolean validateConfig(GPNodeConfig conf) {
		boolean valid = false;
		try {
			Method m = conf.getNodeClass().getDeclaredMethod("validate", GPNodeConfig.class);
			try {
				valid = (Boolean) m.invoke(null, conf);
			} catch (Exception e) {
				getNotifier().fatal("Unable to invoke validate(...) on " + conf.getNodeClass());
				e.printStackTrace();
			}
		} catch (SecurityException e) {
			getNotifier().fatal(
					"Unable to validate node configuration.  validate(...) is not visible for " + conf.getNodeClass());
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			getNotifier().warn("No validation for node: " + conf.getNodeClass());
			valid = true;
		}
		return valid;
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
}
