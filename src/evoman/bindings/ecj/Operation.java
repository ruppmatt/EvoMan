package evoman.bindings.ecj;

import evoman.tools.dtype.*;

public interface Operation {
	public DType calculate(DType[] operands);
	public String toString();
}
