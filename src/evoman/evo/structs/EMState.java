package evoman.evo.structs;

import java.util.concurrent.locks.*;

import evoman.tools.*;


/**
 * 
 * @author ruppmatt
 *
 *		EMState objects share a particular state.
 */
public interface EMState {
	
	/**
	 * Try to get an EMState's parent (if available).
	 * @return
	 */
	public EMState getESParent();
	
	//Startup/Shutdown
	public void init();
	public void finish();
	
	//Utilities
	public MersenneTwisterFast getRandom();
	public EMThreader getThreader();
	public Notifier getNotifier();

	
}
