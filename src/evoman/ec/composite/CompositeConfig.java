package evoman.ec.composite;


import java.util.*;

import evoict.*;
import evoman.evo.structs.*;



public class CompositeConfig extends KeyValueStore {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= 1L;

	protected ArrayList<EvoPool>	_sources			= new ArrayList<EvoPool>();



	public void addSource(EvoPool pool) {
		_sources.add(pool);
	}



	public EvoPool[] getSources() {
		return (EvoPool[]) _sources.toArray();
	}



	public int numSources() {
		return _sources.size();
	}
}
