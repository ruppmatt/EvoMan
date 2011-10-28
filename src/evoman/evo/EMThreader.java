package evoman.evo;

import evoman.tools.Constants;

public class EMThreader implements Constants{
	protected int _running_threads = 0;
	protected int _maximum_threads = EM_MAX_THREADS;
	protected EMState _owner = null;
	
	public EMThreader(EMState owner, int max_threads){
		_maximum_threads = max_threads;
		_owner = owner;
	}
	
	public int getMaxThreads() {
		return _maximum_threads;
	}

		
	public synchronized int getAvailableThreads(){
		return _maximum_threads - _running_threads;
	}
		
	public synchronized int getRunningThreads() {
		return _running_threads;
	}
	
	public synchronized void incThreadCount() {
		_running_threads++;
		if (_owner != null && _owner.getESParent() != null) 
			_owner.getESParent().getThreader().incThreadCount();
	}

	public synchronized void decThreadCount() {
		_running_threads++;
		if (_owner != null && _owner.getESParent() != null) 
			_owner.getESParent().getThreader().incThreadCount();
	}

}
