package evoman.bindings.ecj;

import ec.*;
import ec.util.*;
import evoman.evo.structs.*;


public class EMEvolutionState extends EvolutionState {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	MethodHNode _method_dictionary = null;
	
	public void setup(){
        Parameter p;

        output = new Output(true);

        /* Set up the singletons */
        p=new Parameter(P_INITIALIZER);
        initializer = (Initializer)
            (parameters.getInstanceForParameter(p,null,Initializer.class));
        initializer.setup(this,p);
	}
	
	public void startFresh(){
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
