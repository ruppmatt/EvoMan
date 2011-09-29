package evoman.evo;

import java.util.concurrent.locks.*;

import evoman.tools.MersenneTwisterFast;


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
	
	public final int EM_MAX_THREADS = 1000;
	
	//Startup/Shutdown
	public void init();
	public void finish();
	
	//Stochasticity
	public MersenneTwisterFast getRandom();
	
	//Output
	public void notify(String msg);
	public void warn(String msg);
	public void fatal(String msg);
	
	//Threadding support
	public int getMaxThreads();
	public int getRunningThreads();
	public int getAvailableThreads();
	public void incThreadCount();
	public void decThreadCount();

	
}
