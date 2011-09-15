package evoman.evo;

import evoman.tools.MersenneTwisterFast;

public interface EvoState {
	
	public MersenneTwisterFast getRandom();
	public EvoState getESParent();
	public void notify(String msg);
	public void warn(String msg);
	public void fatal(String msg);

}
