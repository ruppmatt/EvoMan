package evoman.evo;

import evoman.tools.MersenneTwisterFast;

public interface EvoState {
	
	public MersenneTwisterFast getRandom();
	public EvoState getESParent();

}
