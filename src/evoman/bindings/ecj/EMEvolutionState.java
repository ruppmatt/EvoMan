package evoman.bindings.ecj;

import ec.*;
import evoman.evo.*;


public class EMEvolutionState extends EvolutionState {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	MethodHNode _method_dictionary = null;
	
	public void startFresh(){
		setup(this, null);
		population = initializer.initialPopulation(this, 0);
	}
	
	public int evolve(){
		return R_SUCCESS;
	}
	
	public void finish(int result){
	}

	public void setMethodDictionary(MethodHNode dict) {
		_method_dictionary = dict;
	}
	
	public MethodHNode getMethodDictionary(){
		return _method_dictionary;
	}
	

	

}
