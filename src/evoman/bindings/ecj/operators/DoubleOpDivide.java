package evoman.bindings.ecj.operators;

import evoman.bindings.ecj.*;
import evoman.tools.dtype.*;

public class DoubleOpDivide implements Operation {

	@Override
	public DType calculate(DType[] operands) {
		if (operands.length == 0){
			return new DoubleType(Double.NaN);
		}  else {
			double quotient = operands[1].asDouble();
			for (int k = 1; k < operands.length; k++){
				quotient /= operands[k].asDouble();
			}
			return new DoubleType(quotient);
		}
	}
	
	public String toString(){
		return "/";
	}
	

}
