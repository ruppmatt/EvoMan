package evoman.evo.pop;

import java.io.*;


public interface Representation {

	public Object eval(Object o);



	@Override
	public String toString();



	public String lastEval();



	public void serializeRepresentation(ObjectOutputStream out) throws IOException;
}
