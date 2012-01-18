package evoman.evo.structs;


import evoict.*;
import evoict.io.*;



/**
 * 
 * @author ruppmatt
 * 
 *         EMState objects share a particular state.
 */
public interface EMState {

	/**
	 * Try to get an EMState's parent (if available).
	 * 
	 * @return
	 */
	public EMState getESParent();



	// Startup/Shutdown
	public void init();



	public void finish();



	// Utilities
	public RandomGenerator getRandom();



	public EMThreader getThreader();



	public Notifier getNotifier();

}
