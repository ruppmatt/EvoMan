package evoman.ec.composite;


import java.io.*;

import evoict.*;
import evoict.io.*;
import evoman.ec.*;
import evoman.evo.pop.*;
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

public abstract class Composite implements Representation, EMState, Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	protected Representation[]	_components			= null;

	protected EvoPool[]			_sources			= null;

	protected CompositeConfig	_conf;

	protected EMState			_state;



	protected Composite(EMState state, CompositeConfig conf) {
		_state = state;
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



	@Override
	public Object eval(Object o) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String toString(Object context) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void serializeRepresentation(ObjectOutputStream out) throws IOException {
		// TODO Auto-generated method stub

	}



	@Override
	public EMState getESParent() {
		return _state;
	}



	@Override
	public void init() {
	}



	@Override
	public void finish() {
	}



	@Override
	public RandomGenerator getRandom() {
		return _state.getRandom();
	}



	@Override
	public EMThreader getThreader() {
		return _state.getThreader();
	}



	@Override
	public Notifier getNotifier() {
		return _state.getNotifier();
	}

}
