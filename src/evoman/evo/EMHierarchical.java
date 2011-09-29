package evoman.evo;

import evoman.tools.*;

/**
 * 
 * @author ruppmatt
 *
 *		An EMHierarchical object contains shared-state information.
 */
public class EMHierarchical extends Hierarchical implements EvoState {

	EvoState _esparent = null;
	MersenneTwisterFast _rand = null;
	Notifier _notifier = null;
	
	/**
	 * Construct a parentless EMHierarchical
	 * @param name
	 */
	public EMHierarchical(String name){
		this(name, null);
	}
	
	/**
	 * Construct an EMHierarchical with a particular parent
	 * @param name
	 * @param parent
	 */
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
