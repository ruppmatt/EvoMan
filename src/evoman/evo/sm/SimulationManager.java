package evoman.evo.sm;

import evoman.evo.structs.*;


/**
 * 
 * @author ruppmatt
 *
 *		A simulation manager controls the mapping of genetic representations to agents; executs simulations; and notifies EvoPools of
 * 		the results of the simulations (e.g. fitnesses).
 */
public class SimulationManager extends EMConfigurableHNode {
	
	EvoPool _epool;
	
	public SimulationManager(String name, EvoPool parent){
		super(name, parent);
		_epool = parent;
		addDefault("threads", 1, "Number of processing threads");
	}


}
