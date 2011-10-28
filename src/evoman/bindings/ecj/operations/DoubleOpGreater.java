package evoman.bindings.ecj.operations;

import evoman.bindings.ecj.*;
import evoman.tools.dtype.*;

public class DoubleOpGreater implements Operation{

	@Override
	public DType calculate(DType[] operands) {
		if (operands.length == 0){
			return new DoubleType(Double.NaN);
		} else {
			Double retval = operands[0].asDouble();
			for (int k = 1; k < operands.length; k++){
				retval = (operands[k].asDouble() > retval) ? operands[k].asDouble() : retval;
			}
			return new DoubleType(retval);
		}
	}
	
	public String toString(){
		return ">";
	}
	
	

}
