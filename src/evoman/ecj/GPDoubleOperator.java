package evoman.ecj;

import ec.*;
import ec.gp.*;

public class GPDoubleOperator extends GPNode {

	private static final long serialVersionUID = 1L;
	Operation _op = null;

	
	GPDoubleOperator(Operation op){
		_op = op;
	}
	
	
	@Override
	public String toString() {
		return _op.toString();
	}

	@Override
	public void eval(EvolutionState state, int thread, GPData input,
			ADFStack stack, GPIndividual individual, Problem problem) {
		input = (DoubleGP) _op.calculate(children);
	}
}
