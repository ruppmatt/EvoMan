package evoman.ec.evolution;


import java.lang.reflect.*;
import java.util.*;

import evoict.*;
import evoman.config.*;



public class EvolutionOpConfig extends KeyValueStore implements Identifiable {

	private static final long						serialVersionUID	= 1L;
	protected EvolutionOpConstraints				_constr;
	protected Class<? extends EvolutionOperator>	_cl;
	protected String								_name;
	protected HashSet<EvolutionPipeConfig>			_epc				= new HashSet<EvolutionPipeConfig>();



	public EvolutionOpConfig(Class<? extends EvolutionOperator> cl) {
		this(null, cl);
	}



	@ConfigConstructor(args = { ConfigArgs.NAME, ConfigArgs.PROXY })
	public EvolutionOpConfig(String name, Class<? extends EvolutionOperator> cl) {
		_name = name;
		_constr = EvolutionOpConstraints.scan(cl);
		_cl = cl;
	}



	public EvolutionOpConstraints getConstraints() {
		return _constr;
	}



	public Class<? extends EvolutionOperator> getOpClass() {
		return _cl;
	}



	public void registerPipe(EvolutionPipeConfig epc) {
		_epc.add(epc);
	}



	public void unregisterPipe(EvolutionPipeConfig epc) {
		if (_epc.contains(epc)) {
			_epc.remove(epc);
		}
	}



	protected void setName(String name) {
		_name = name;
	}



	@Override
	public String getName() {
		return _name;
	}



	public void validate() throws BadConfiguration {
		Method v = null;
		try {
			v = getOpClass().getMethod("validate", EvolutionOpConfig.class);
		} catch (SecurityException e) {
			throw new BadConfiguration(getName() + ": check validation visibility.");
		} catch (NoSuchMethodException e) {
			return;
		}
		try {
			v.invoke(null, this);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.getCause().printStackTrace();
			throw (BadConfiguration) e.getTargetException();
		}

	}
}
