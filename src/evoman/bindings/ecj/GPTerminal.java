package evoman.bindings.ecj;

import ec.*;
import ec.gp.*;

public class GPTerminal extends GPNode {


	private static final long serialVersionUID = 1L;
	protected String _path = null;
	
	GPTerminal(String path){
		_path = path;
	}

	@Override
	public String toString() {
		return _path;
	}

	@Override
	public void eval(EvolutionState state, int thread, GPData input,
			ADFStack stack, GPIndividual individual, Problem problem) {
		ObjectProblem obj_prob = (ObjectProblem) problem;
		( (EMGPData) input).fromDType(obj_prob.evaluate(_path));
	}

}
