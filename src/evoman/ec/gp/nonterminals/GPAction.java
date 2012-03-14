package evoman.ec.gp.nonterminals;


import java.lang.annotation.*;
import java.lang.reflect.*;

import evoict.*;
import evoict.reflection.*;
import evoman.config.*;
import evoman.ec.gp.*;
import evoman.evo.structs.*;



/**
 * 
 * @author ruppmatt
 * 
 *         GPAction is a node of type boolean which returns true on successful
 *         invocation of its configured method. The object used to invoke the
 *         method must be specified in the context passed to the eval method.
 * 
 *         Because the parameter types of the child nodes are not known until
 *         the method is specified, child_types in the GPNodeConstraints will be
 *         established by the validate() method. This method must be called
 *         prior to the construction of the GPTree.
 * 
 *         If multiple methods of a class have the same name, the first
 *         discovered method is used. Methods should be passed in the
 *         GPNodeConfig object in a format similar to:
 *         simternet.agents.asp.processUsage
 *         where the last period delimited field is the method name and the
 *         prefix fields define the package path to the method.
 * 
 *         Configuration required:
 *         "method" a string resource-path to the method to invoke on the actor
 * 
 *         Automatically configured:
 *         "_method" the actual method that will be invoked
 */

@ConfigRegister(name = "Action")
@ConfigProxy(proxy_for = GPNodeConfig.class)
@GPNodeDescriptor(name = "Action", return_type = Boolean.class, child_types = {})
public class GPAction extends GPNode {

	private static final long	serialVersionUID	= 1L;

	protected Method			_method;



	public static void validate(GPNodeConfig conf) throws BadConfiguration {
		BadConfiguration bc = new BadConfiguration();
		if (!conf.validate("method", String.class)) {
			bc.append("GPAction: missing method name");
		} else {
			String method = conf.S("method");
			int last_delim = method.lastIndexOf('.');
			if (last_delim == -1) {
				bc.append("GPAction: method name is improperly formatted.");
			} else {
				String classname = method.substring(0, last_delim - 1);
				String methodname = method.substring(last_delim + 1);
				Class<?> cl = null;
				Method meth = null;
				try {
					cl = Class.forName(classname);
				} catch (ClassNotFoundException e) {
					bc.append("GPAction: " + classname + " not found.");
					bc.validate();
				}
				for (Method m : cl.getMethods()) {
					if (m.getName().equals(methodname)) {
						meth = m;
						break;
					}
				}
				if (meth == null) {
					bc.append("GPAction: Unable to find method " + methodname + " for " + cl.getName());
					bc.validate();
				}
				conf.set("_method", meth);
				Class<?>[] params = meth.getParameterTypes();
				Class<?>[] childs = new Class<?>[params.length + 1];
				childs[0] = Boolean.class;
				for (int k = 0; k < params.length; k++) {
					childs[k + 1] = params[k];
				}
				conf.getConstraints().setChildTypes(params);
			}
		}
		bc.validate();
	}



	public GPAction(GPNodeConfig conf, GPNode parent, GPNodePos pos) {
		super(conf, parent, pos);
		_method = (Method) conf.get("_method");
	}



	@Override
	public void init(EMState state) throws BadConfiguration {
		return;
	}



	@Override
	public GPNode clone(GPNode parent) {
		GPAction n = new GPAction(_conf, parent, (GPNodePos) _pos.clone());
		n._method = _method;
		doClone(n);
		return n;
	}



	@Override
	public Object eval(Object context) throws BadNodeValue {
		try {
			Boolean do_action = (Boolean) _children[0].eval(context);
			if (do_action) {
				Object actor = findActor(context);
				int num_children = getConfig().getConstraints().numChildren();
				int num_params = num_children - 1;
				Object[] params = (num_params > 0) ? new Object[getConfig().getConstraints().numChildren() - 1] : null;
				for (int k = 0; k < num_params; k++) {
					params[k] = _children[k + 1].eval(context);
				}
				if (actor == null) {
					doMethod(context, params);
				} else {
					doMethod(actor, params);
				}
				return true;
			} else {
				return false;
			}
		} catch (BadConfiguration bc) {
			throw new BadNodeValue(bc.getMessage(), this);
		}

	}



	protected Object findActor(Object context) throws BadConfiguration {
		// Search fields then search getters
		Object actor = null;
		Class<?> cl = context.getClass();
		for (Field f : cl.getFields()) {
			for (Annotation a : f.getAnnotations()) {
				if (a instanceof Actor) {
					try {
						return f.get(context);
					} catch (Exception e) {
						BadConfiguration bc = new BadConfiguration(e.getMessage());
						bc.append("Unable to invoke field tagged as actor in " + cl.toString());
						throw bc;
					}
				}
			}
		}
		for (Method m : cl.getMethods()) {
			for (Annotation a : m.getAnnotations()) {
				if (a instanceof Actor) {
					try {
						m.invoke(context);
					} catch (Exception e) {
						BadConfiguration bc = new BadConfiguration(e.getMessage());
						bc.append("Unable to invoke method tagged as actor in " + cl.toString());
						throw bc;
					}
				}
			}
		}
		return actor;
	}



	protected void doMethod(Object actor, Object[] params) throws BadConfiguration {
		try {
			_method.invoke(actor, params);
		} catch (Exception e) {
			BadConfiguration bc = new BadConfiguration(e.getMessage());
			bc.append("Unable to invoke method " + _method.getName() + " on actor " + actor.getClass().getName());
			throw bc;
		}
	}



	@Override
	public String toString(Object context) throws BadNodeValue {
		return super.toString(context, "ACTION<" + _method.toString() + "=" + eval(context).toString() + ">");
	}



	@Override
	public String toString() {
		return super.toString("ACTION<" + _method.toString() + ">");
	}

}
