package evoman.bindings.ecj.operators;

import evoman.bindings.ecj.*;
import evoman.tools.dtype.*;



public class DoubleOpSubtract implements Operation{

	@Override
	public DType calculate(DType[] operands) {
		if (operands.length == 0){
			return new DoubleType(Double.NaN);
		} else if (operands.length > 1){
			double difference = operands[1].asDouble();
			for (int k = 1; k < operands.length; k++)
				difference -= operands[k].asDouble();
			return new DoubleType(difference);
		} else {
			return new DoubleType(-1.0 * operands[1].asDouble());
		}
	}
	
	public String toString(){
		return "-";
	}
	
}
