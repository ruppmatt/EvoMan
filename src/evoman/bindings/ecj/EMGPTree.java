package evoman.bindings.ecj;

import ec.*;
import ec.gp.*;
import ec.util.*;

public class EMGPTree extends GPTree{
	
	public Parameter defaultBase(){
		return new Parameter("evoman.bindings.ecj.EMGPTree");
	}
	
	public void setup(final EvolutionState state, final Parameter base){
		Parameter def = defaultBase();
		System.err.println("Setting tree constraints.");
		String s = state.parameters.getString(base.push(P_TREECONSTRAINTS),
	            def.push(P_TREECONSTRAINTS));
		System.err.println(s);
	        if (s==null)
	            state.output.fatal("No tree constraints are defined for the GPTree " + base + ".");
	        else 
	            constraints = GPTreeConstraints.constraintsFor(s,state).constraintNumber;
	     System.err.println("Done with EMGPTree setup.");
	}

}
