package evoman.bindings.ecj.operators;

import evoman.bindings.ecj.*;
import evoman.tools.dtype.*;

public class DoubleOpSelector implements Operation{

	@Override
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
