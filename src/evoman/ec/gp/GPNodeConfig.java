package evoman.ec.gp;


import java.io.*;

import evoict.*;
import evoman.config.*;



/**
 * GPNode configuration contains information about a particular type of node and
 * its constraints. It is used to construct and configure nodes in GPTrees at
 * run-time.
 * 
 * @author ruppmatt
 * 
 */
@ConfigRegister(name = "GPNodeConfig")
public class GPNodeConfig implements Configurable, Cloneable, Serializable {

	private static final long			serialVersionUID	= 1L;
	protected GPNodeConstraints			_constraints;								// Node
																					// constraints
	protected Class<? extends GPNode>	_class;									// Node
																					// type
	protected KeyValueStore				_kv					= new KeyValueStore();



	/**
	 * Construct a new GPNode configuration from a GPNode class
	 * 
	 * @param cl
	 */
	public GPNodeConfig(Class<? extends GPNode> cl) {
		super();
		_class = cl;
		_constraints = GPNodeConstraints.scan(cl);
	}



	@Override
	public Object clone() {
		GPNodeConfig clone = new GPNodeConfig(_class);
		clone._kv = (KeyValueStore) _kv.clone();
		return clone;
	}



	/**
	 * Get the class of the GPNode
	 * 
	 * @return
	 */
	public Class<? extends GPNode> getNodeClass() {
		return _class;
	}



	/**
	 * Get the constraints for the GPNode
	 * 
	 * @return
	 */
	public GPNodeConstraints getConstraints() {
		return _constraints;
	}



	@Override
	public Object get(String name) {
		return _kv.get(name);
	}



	@Override
	public void set(String name, Object value) {
		_kv.set(name, value);
	}



	@Override
	public void unset(String name) {
		_kv.unset(name);
	}



	@Override
	public Boolean isSet(String name) {
		return _kv.isSet(name);
	}



	@Override
	public Integer I(String name) {
		return _kv.I(name);
	}



	@Override
	public Double D(String name) {
		return _kv.D(name);
	}



	@Override
	public String S(String name) {
		return _kv.S(name);
	}



	@Override
	public Boolean validate(String name, Class<?> cl) {
		return _kv.validate(name, cl);
	}

}
