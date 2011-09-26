package evoman.evo;

import evoman.tools.*;

public class EMHierarchical extends Hierarchical implements EvoState {

	EvoState _esparent = null;
	MersenneTwisterFast _rand = null;
	Notifier _notifier = null;
	
	public EMHierarchical(String name){
		this(name, null);
	}
	
	public EMHierarchical(String name, Hierarchical parent){
		super(name, parent);
		if (parent instanceof EvoState)
			_esparent = (EvoState) parent;
	}

	@Override
	public MersenneTwisterFast getRandom() {
		if ( _rand == null ){
			if (_esparent != null)
				_esparent.getRandom();
			else
				_rand = new MersenneTwisterFast();
		}
		return _rand;
	}

	@Override
	public EvoState getESParent() {
		return _esparent;
	}

	@Override
	public void notify(String msg) {
		if (_notifier == null)
			if (_esparent != null){
				_esparent.notify(msg);
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
	
	
}
