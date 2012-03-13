package evoman.ec;


import java.io.*;



public interface Representation {

	public Object eval(Object o) throws BadEvaluation;



	@Override
	public String toString();



	public String toString(Object context) throws BadEvaluation;



	public void serializeRepresentation(ObjectOutputStream out) throws IOException;



	public Object clone();

}
