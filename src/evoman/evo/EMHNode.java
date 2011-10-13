package evoman.evo;

import evoman.tools.*;

/**
 * 
 * @author ruppmatt
 *
 *		An EMHierarchical object contains shared-state information.
 */
public class EMHNode extends HNode implements EMState {

	EMState _emparent = null;
	MersenneTwisterFast _rand = null;
	Notifier _notifier = null;
	int _running_threads = 0;
	
	
	/**
	 * Construct a parentless EMHierarchical
	 * @param name
	 */
	public EMHNode(String name){
		this(name, null);
	}
	
	/**
	 * Construct an EMHierarchical with a particular parent
	 * @param name
	 * @param parent
	 */
	public EMHNode(String name, HNode parent){
		super(name, parent);
		if (parent instanceof EMState)
			_emparent = (EMState) parent;
	}

	@Override
	public MersenneTwisterFast getRandom() {
		if ( _rand == null ){
			if (_emparent != null)
				_emparent.getRandom();
			else
				_rand = new MersenneTwisterFast();
		}
		return _rand;
	}

	@Override
	public EMState getESParent() {
		return _emparent;
	}

	@Override
	public void notify(String msg) {
		if (_notifier == null)
			if (_emparent != null){
				_emparent.notify(msg);
				return;
			} else{
				_notifier = new Notifier();
			}
		_notifier.notify(msg);
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
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getMaxThreads() {
		return (_emparent != null) ?  _emparent.getMaxThreads() : EM_MAX_THREADS;
	}

	@Override
	public synchronized int getRunningThreads() {
		return _running_threads;
	}

	@Override
	public synchronized int getAvailableThreads() {
		return getMaxThreads() - _running_threads;
	}

	@Override
	public synchronized void incThreadCount() {
		_running_threads++;
		if (_emparent != null) _emparent.incThreadCount();
	}

	@Override
	public synchronized void decThreadCount() {
		_running_threads--;
		if (_emparent != null) _emparent.decThreadCount();
	}
	
	
}
