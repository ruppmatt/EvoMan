package evoman.evo.structs;


import evoict.*;
import evoict.graphs.*;
import evoict.io.*;



/**
 * 
 * @author ruppmatt
 * 
 *         EMConfigurables are Configurable/Hierarchical objects that share
 *         state information.
 */
public class EMConfigurableHNode extends ConfigurableHNode implements EMState {

	private static final long	serialVersionUID	= 1L;
	protected EMState			_emparent			= null;
	protected RandomGenerator	_rand				= null;
	protected Notifier			_notifier			= null;
	protected EMThreader		_threader			= null;



	/**
	 * Construct a parentless EMConfigurable
	 * 
	 * @param name
	 */
	public EMConfigurableHNode(String name) {
		this(name, null);
	}



	/**
	 * Construct an EMConfigurable with a particular parent
	 * 
	 * @param name
	 * @param parent
	 */
	public EMConfigurableHNode(String name, HNode parent) {
		super(name, parent);
		set("rand_seed", (Integer) null);
		if (parent instanceof EMState)
			_emparent = (EMState) parent;
	}



	@Override
	public EMState getESParent() {
		return _emparent;
	}



	@Override
	public void init() {
		for (HNode child : getChildren()) {
			if (child instanceof EMState) {
				((EMState) child).init();
			}
		}
	}



	@Override
	public void finish() {
		for (HNode child : getChildren()) {
			if (child instanceof EMState) {
				((EMState) child).finish();
			}
		}
	}



	@Override
	public RandomGenerator getRandom() {
		if (_rand == null) {
			if (_emparent != null)
				_emparent.getRandom();
			else {
				if (_kv.isSet("seed"))
					_rand = new RandomGenerator(_kv.I("seed"));
				else
					_rand = new RandomGenerator();
			}
		}
		return _rand;
	}



	@Override
	public EMThreader getThreader() {
		if (_threader == null) {
			if (_emparent != null)
				_emparent.getThreader();
			else {
				if (_kv.isSet("max_threads"))
					_threader = new EMThreader(this, I("max_threads"));
				else
					_threader = new EMThreader(this, 1);
			}
		}
		return _threader;
	}



	@Override
	public Notifier getNotifier() {
		if (_notifier == null) {
			if (_emparent != null)
				_emparent.getThreader();
			else {
				_notifier = new Notifier();
			}
		}
		return _notifier;
	}
}
