package evoman.ecj;

import ec.gp.*;

public interface Operation {
	public Object calculate(GPNode[] children);
	public String toString();
}
