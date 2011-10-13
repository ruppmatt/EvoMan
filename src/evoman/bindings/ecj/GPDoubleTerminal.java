package evoman.bindings.ecj;

import java.lang.reflect.*;

import ec.*;
import ec.gp.*;

public class GPDoubleTerminal extends GPNode {


	private static final long serialVersionUID = 1L;
	protected Method _m = null;
	
	GPDoubleTerminal(Method m){
		m = _m;
	}

	@Override
	public String toString() {
		return _m.getName();
	}

	@Override
	public void eval(EvolutionState state, int thread, GPData input,
			ADFStack stack, GPIndividual individual, Problem problem) {
		try {
			input = (DoubleGP) _m.invoke( ((ObjectProblem) problem).getObject() );
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
