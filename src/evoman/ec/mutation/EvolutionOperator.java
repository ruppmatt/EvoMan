package evoman.ec.mutation;


import java.io.*;
import java.util.*;

import evoman.evo.pop.*;



public abstract class EvolutionOperator implements Serializable {

	private static final long									serialVersionUID	= 1L;
	protected EvolutionPipeline									_pipeline;
	protected EvolutionOpConfig									_conf;
	protected LinkedHashMap<EvolutionPipeConfig, Population>	_received			= new LinkedHashMap<EvolutionPipeConfig, Population>();



	public EvolutionOperator(EvolutionPipeline pipeline, EvolutionOpConfig conf) {
		_pipeline = pipeline;
		_conf = conf;
	}



	public boolean isReady() {
		boolean ready = true;
		for (EvolutionPipeConfig epc : _conf._epc) {
			ready = ready && !_pipeline.getPipe(epc).empty();
		}
		return ready;
	}



	protected boolean drainPipes() {
		if (!isReady()) {
			return false;
		} else {
			for (EvolutionPipeConfig epc : _conf._epc) {
				_received.put(epc, _pipeline.getPipe(epc).receive());
			}
			return true;
		}
	}



	/*
	 * The populations in _received can be shared by other operators.
	 * Modifying these populations (or those genotypes) should not be done.
	 * In other words, clone() the populations and genotypes if you wish to
	 * modify either in produce().
	 */
	abstract public Population produce();



	public EvolutionOpConfig getConfig() {
		return _conf;
	}
}
