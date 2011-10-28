package evoman.bindings.ecj.operations;

import evoman.bindings.ecj.*;
import evoman.tools.dtype.*;

public class BooleanOpORN implements Operation{

	@Override
	public DType calculate(DType[] operands) {
		if (operands.length == 0){
			return new BooleanType(false);
		} else {
			Boolean retval = true;
			for (DType k : operands){
				retval = retval || k.asBoolean();
			}
			return new BooleanType(!retval);
		}
	}
	
	
	public String toString(){
		return "ORN"; 
	}

	
}
