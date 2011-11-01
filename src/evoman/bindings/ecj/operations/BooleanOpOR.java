package evoman.bindings.ecj.operations;

import java.util.*;

import ec.*;
import ec.gp.*;
import evoman.bindings.ecj.*;
import evoman.tools.dtype.*;

public class BooleanOpOR extends GPNode {

	private static final long serialVersionUID = 1L;

	public void eval(EvolutionState state, int thread, GPData input,
			ADFStack stack, GPIndividual individual, Problem problem) {
		ArrayList<DType> operands = new ArrayList<DType>();
		EMGPData result = (EMGPData) input;
		for (GPNode child : this.children){
			child.eval(state,  thread, result, stack, individual, problem);
			operands.add(result);
		}
		result = new EMGPData(calculate((DType[]) operands.toArray()));
	}
	
	public DType calculate(DType[] operands) {
		if (operands.length == 0){
			return new BooleanType(false);
		} else {
			Boolean retval = true;
			for (DType k : operands){
				retval = retval || k.asBoolean();
			}
			return new BooleanType(retval);
		}
	}
	
	public String toString(){
		return "||";
	}

}
