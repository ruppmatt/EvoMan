package evoman.bindings.ecj.operations;

import java.util.*;

import ec.*;
import ec.gp.*;
import evoman.bindings.ecj.*;
import evoman.tools.dtype.*;

public class DoubleOpSelector extends GPNode{

	private static final long serialVersionUID = 1L;
	public static int min_children = 3;

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
		if (operands.length != 3){
			return new DoubleType(Double.NaN);
		} else {
			double first = operands[1].asDouble();
			double second = operands[2].asDouble();
			return new DoubleType( (operands[0].asBoolean()) ? first : second);
		}
	}
	
	public String toString(){
		return "?:";
	}

}
