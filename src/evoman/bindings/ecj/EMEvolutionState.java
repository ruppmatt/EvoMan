package evoman.bindings.ecj;

import ec.*;


public class EMEvolutionState extends EvolutionState {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void startFresh(){
		setup(this, null);
		population = initializer.initialPopulation(this, 0);
	}
	
	public int evolve(){
		return R_SUCCESS;
	}
	
	public void finish(int result){
	}
	

	

}
