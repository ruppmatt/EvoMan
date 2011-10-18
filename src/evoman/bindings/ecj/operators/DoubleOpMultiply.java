package evoman.bindings.ecj.operators;

import evoman.bindings.ecj.*;
import evoman.tools.dtype.*;

public class DoubleOpMultiply implements Operation {

	@Override
	public DType calculate(DType[] operands) {
		if (operands.length > 0){
			double product = 1.0;
			for (DType k : operands){
				product *= k.asDouble();
			}
			return new DoubleType(product);
		} else
			return new DoubleType(Double.NaN);
	}
	
	public String toString(){
		return "*";
	}

}
