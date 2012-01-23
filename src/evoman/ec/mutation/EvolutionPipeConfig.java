package evoman.ec.mutation;


import evoict.*;
import evoman.interpreter.*;



@ConfigDescriptor(name = "EvolutionPipe")
public class EvolutionPipeConfig extends KeyValueStore {

	private static final long	serialVersionUID	= 1L;
	protected EvolutionOpConfig	_from;
	protected EvolutionOpConfig	_to;



	@ConfigRequired(names = { "from", "to" })
	protected EvolutionPipeConfig(EvolutionOpConfig from, EvolutionOpConfig to) {
		_from = from;
		_to = to;
	}



	public EvolutionOpConfig getSender() {
		return _from;
	}



	public EvolutionOpConfig getReceiver() {
		return _to;
	}



	public double getWeight() {
		if (validate("weight", Double.class)) {
			return D("weight");
		} else {
			return 1.0;
		}
	}



	public int getPriority() {
		if (validate("priority", Integer.class)) {
			return I("priority");
		} else {
			return 1;
		}
	}

}
