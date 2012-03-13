package evoman.ec;


import evoict.*;



public abstract class RepresentationConfig extends KeyValueStore implements Validatable {

	private static final long	serialVersionUID	= 1L;



	@Override
	public abstract void validate() throws BadConfiguration;

}
