package evoman.bindings.ecj;

import ec.*;
import ec.gp.*;
import evoman.evo.*;

public class EMERC extends ERC {

	private static final long serialVersionUID = 1L;
	protected MethodHNode _method_dict = null;
	protected String _method_path = null;
	
	public EMERC(MethodHNode dict){
		_method_dict = dict;
	}
	
	public String name(){
		return _method_path;
	}
	
	@Override
	public void resetNode(EvolutionState state, int thread) {
		mutateERC(state,thread);
	}
	
	public void mutateERC(EvolutionState state, int thread){
		_method_path = _method_dict.getRandomPath();
	}

	@Override
	public boolean nodeEquals(GPNode node) {
		return (((EMERC) node)._method_path == _method_path) ? true : false;
	}

	@Override
	public String encode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eval(EvolutionState state, int thread, GPData input,
			ADFStack stack, GPIndividual individual, Problem problem) {
		ObjectProblem obj_prob = (ObjectProblem) problem;
		( (EMGPData) input).fromDType(obj_prob.evaluate(_method_path));
	}

}
