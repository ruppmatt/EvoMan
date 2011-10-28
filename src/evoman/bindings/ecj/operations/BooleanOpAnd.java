package evoman.bindings.ecj.operations;

import evoman.bindings.ecj.*;
import evoman.tools.dtype.*;

public class BooleanOpAnd implements Operation{

	@Override
	public DType calculate(DType[] operands) {
		if (operands.length == 0){
			return new BooleanType(false);
		} else {
			Boolean result = true;
			for (DType k : operands){
				result = result && k.asBoolean();
			}
			return new BooleanType(result);
		}
	}
	
	public String toString(){
		return "&&";
	}

}
