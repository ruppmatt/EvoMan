package evoman.evo;

import evoman.tools.Configurable;
import evoman.tools.Hierarchical;
import evoman.tools.MersenneTwisterFast;

public class EMConfigurable extends Configurable implements EvoState {

	EvoState _esparent = null;
	MersenneTwisterFast _rand = null;
	
	public EMConfigurable(String name){
		this(name, null);
	}
	
	public EMConfigurable(String name, Hierarchical parent){
		super(name, parent);
		addDefault("rand_seed", (Integer) null, "The random number seed.");
		if (parent instanceof EvoState)
			_esparent = (EvoState) parent;
	}

	@Override
	public MersenneTwisterFast getRandom() {
		if ( _rand == null ){
			if (_esparent != null)
				_esparent.getRandom();
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
	public EvoState getESParent() {
		return _esparent;
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
}
