package evoman.ec;


import java.io.*;

import evoict.*;



public abstract class RepresentationConfig implements Configurable, Cloneable, Validatable, Serializable {

	private static final long	serialVersionUID	= 1L;

	protected KeyValueStore		_kv					= new KeyValueStore();



	@Override
	public abstract void validate() throws BadConfiguration;



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
