package evoman.ec.composite.init;


import evoict.*;
import evoman.ec.*;
import evoman.ec.composite.*;
import evoman.evo.structs.*;



public abstract class CompositeInitializer extends RepresentationInitializer {

	private static final long	serialVersionUID	= 1L;



	public CompositeInitializer() {
	}



	public abstract Representation retrieve(Composite dst, EvoPool src, int loc, EMState state) throws BadConfiguration;

}
