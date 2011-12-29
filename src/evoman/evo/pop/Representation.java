package evoman.evo.pop;

import evoman.evo.structs.*;

public interface Representation {
	public Object evaluate(Object o);
	public MethodHNode getDictionary();
	public void setDictionary();
}
