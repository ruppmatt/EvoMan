package evoman.bindings.ecj;

import java.io.*;

import ec.*;
import ec.gp.*;
import evoman.evo.*;

public class EMERC extends ERC {

	private static final long serialVersionUID = 1L;
	protected String _method_path = null;
	

	public String name(){
		return _method_path;
	}
	
	@Override
	public void resetNode(EvolutionState state, int thread) {
		mutateERC(state,thread);
	}
	
	public void mutateERC(EvolutionState state, int thread){
		MethodHNode method_dict = ((EMEvolutionState) state).getMethodDictionary();
		_method_path = method_dict.getRandomPath();
	}

	@Override
	public boolean nodeEquals(GPNode node) {
		return (node.getClass() == this.getClass() && 
				( (EMERC) node)._method_path == _method_path);
	}
	
	public void readNode(EvolutionState state, DataInput input) throws IOException {
		_method_path = input.toString();
	}
	
	public void writeNode(EvolutionState state, DataOutput output) throws IOException {
		output.writeChars(_method_path);
	}

	@Override
	public String encode() {
		return _method_path;
	}
	
	

	@Override
	public void eval(EvolutionState state, int thread, GPData input,
			ADFStack stack, GPIndividual individual, Problem problem) {
		ObjectProblem obj_prob = (ObjectProblem) problem;
		( (EMGPData) input).fromDType(obj_prob.evaluate(_method_path));
	}

}
