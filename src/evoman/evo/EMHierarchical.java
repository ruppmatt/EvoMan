package evoman.evo;

import evoman.tools.Hierarchical;
import evoman.tools.MersenneTwisterFast;

public class EMHierarchical extends Hierarchical implements EvoState {

	EvoState _esparent = null;
	MersenneTwisterFast _rand = null;
	
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
}
