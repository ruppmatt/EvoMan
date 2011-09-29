package evoman.evo;

import evoman.tools.Configurable;
import evoman.tools.Hierarchical;
import evoman.tools.MersenneTwisterFast;

/**
 * 
 * @author ruppmatt
 *
 *		EMConfigurables are Configurable/Hierarchical objects that share state information.
 */
public class EMConfigurable extends Configurable implements EMState  {

	EMState _emparent = null;
	MersenneTwisterFast _rand = null;
	protected int runningThreads = 0;
	
	/**
	 * Construct a parentless EMConfigurable
	 * @param name
	 */
	public EMConfigurable(String name){
		this(name, null);
	}
	
	
	
	/**
	 * Construct an EMConfigurable with a particular parent
	 * @param name
	 * @param parent
	 */
	public EMConfigurable(String name, Hierarchical parent){
		super(name, parent);
		addDefault("rand_seed", (Integer) null, "The random number seed.");
		if (parent instanceof EMState)
			_emparent = (EMState) parent;
	}

	
	
	@Override
	public MersenneTwisterFast getRandom() {
		if ( _rand == null ){
			if (_emparent != null)
				_emparent.getRandom();
			else{
				if (_params.containsKey("seed"))
					_rand = new MersenneTwisterFast((Integer) _params.get("seed").value());
				else
					_rand = new MersenneTwisterFast();
			}
		}
		return _rand;
	}

	
	
	@Override
	public EMState getESParent() {
		return _emparent;
	}

	
	
	@Override
	public void notify(String msg) {
		System.out.println(msg);
	}

	
	
	@Override
	public void warn(String msg) {
		System.out.println("Warning: " + msg);
	}

	
	
	@Override
	public void fatal(String msg) {
		System.out.println("Fatal error: " + msg);
	}

	
	
	@Override
	public int getMaxThreads() {
		if (_params.containsKey("max_threads")){
			return I("max_threads");
		} else {
			if (_emparent != null)
				return _emparent.getMaxThreads();
			else
				return EM_MAX_THREADS;
		}
	}

	
	public synchronized int getAvailableThreads(){
		return getMaxThreads() - runningThreads;
	}
	
	@Override
	public synchronized int getRunningThreads() {
		return runningThreads;
	}
	
	@Override
	public synchronized void incThreadCount() {
		runningThreads++;
		if (_emparent != null) _emparent.incThreadCount();
	}

	@Override
	public synchronized void decThreadCount() {
		runningThreads--;
		if (_emparent != null) _emparent.decThreadCount();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}
}
