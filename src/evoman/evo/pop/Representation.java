package evoman.evo.pop;


import java.io.*;



public interface Representation {

	public Object eval(Object o);



	@Override
	public String toString();



	public String toString(Object context);



	public void serializeRepresentation(ObjectOutputStream out) throws IOException;



	public Object clone();

}
