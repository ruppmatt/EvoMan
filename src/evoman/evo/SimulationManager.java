package evoman.evo;

public class SimulationManager extends EMConfigurable {
	
	EvoPool _epool;
	
	public SimulationManager(String name, EvoPool parent){
		super(name, parent);
		_epool = parent;
		addDefault("threads", 1, "Number of processing threads");
	}

}
