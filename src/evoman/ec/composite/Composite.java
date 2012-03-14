package evoman.ec.composite;


import java.io.*;

import evoict.*;
import evoman.ec.*;
import evoman.evo.structs.*;



/**
 * Composite representations contain representations created by other EvoPools.
 * At evaluation time, each of the component representations are evaluated
 * sequentially.
 * 
 * 
 * @author ruppmatt
 * 
 */

public abstract class Composite implements Cloneable, Representation, Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	protected Representation[]	_components			= null;

	protected EvoPool[]			_sources			= null;

	protected CompositeConfig	_conf;



	protected Composite(CompositeConfig conf) {
		_conf = conf;
	}



	public CompositeConfig getConfig() {
		return _conf;
	}



	public int size() {
		return (_components == null) ? 0 : _components.length;
	}



	public Representation[] getComponents() {
		return _components;
	}



	public EvoPool[] getSources() {
		return _sources;
	}



	public abstract void setComponent(Representation r, int ndx, EvoPool src) throws BadConfiguration;



	@Override
	public abstract Object clone();

}
