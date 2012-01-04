package evoman.ec.gp.init;


import java.io.*;

import evoict.io.*;
import evoman.ec.gp.*;
import evoman.evo.*;
import evoman.evo.structs.*;



public abstract class GPTreeInitializer implements EMState, Serializable {

	private static final long	serialVersionUID	= 1L;
	EMState						_state;
	GPInitConfig				_conf;



	public GPTreeInitializer(EMState state, GPInitConfig conf) {
		_state = state;
		_conf = conf;
	}



	public abstract boolean createTerminal(GPTree t, GPNode n, Class<?> cl);



	public GPInitConfig getConfig() {
		return _conf;
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
	public MersenneTwisterFast getRandom() {
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
