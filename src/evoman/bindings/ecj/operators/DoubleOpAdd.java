package evoman.bindings.ecj.operators;

import evoman.bindings.ecj.*;
import evoman.tools.dtype.*;

public class DoubleOpAdd implements Operation {

	@Override
	public DType calculate(DType[] operands) {
		if (operands.length > 0){
			double sum = 0.0;
			for (DType k : operands){
				sum += k.asDouble();
			}
			return new DoubleType(sum);
		} else {
			return new DoubleType(Double.NaN);
		}
	}
	
	public String toString(){
		return "+";
	}

}
