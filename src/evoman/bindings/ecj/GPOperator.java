package evoman.bindings.ecj;

import java.util.*;

import ec.*;
import ec.gp.*;
import evoman.tools.dtype.*;

public class GPOperator extends GPNode {

	private static final long serialVersionUID = 1L;
	Operation _op = null;

	
	GPOperator(Operation op){
		_op = op;
	}
	
	
	@Override
	public String toString() {
		return _op.toString();
	}


	@Override
	public void eval(EvolutionState state, int thread, GPData input,
			ADFStack stack, GPIndividual individual, Problem problem) {
		ArrayList<DType> operands = new ArrayList<DType>();
		EMGPData result = (EMGPData) input;
		for (GPNode child : this.children){
			child.eval(state,  thread, result, stack, individual, problem);
			operands.add(result);
		}
		result = new EMGPData(_op.calculate((DType[]) operands.toArray()));
	}

}
