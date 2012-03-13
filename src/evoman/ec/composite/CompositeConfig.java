package evoman.ec.composite;


import java.util.*;

import evoict.*;
import evoman.config.*;
import evoman.ec.*;
import evoman.evo.structs.*;



/**
 * A CompositeConfig contains configuration information about a composite type.
 * A composite contains components that are created and managed (created,
 * mutated, but not selected) by lower-level EvoPools.
 * 
 * Because there may be different types of composites (e.g. fixed-length), the
 * class of the Composite needs to be passed as a constructor argument.
 * 
 * @author ruppmatt
 * 
 */

public class CompositeConfig extends RepresentationConfig {

	/**
	 * 
	 */
	private static final long					serialVersionUID	= 1L;

	protected final Class<? extends Composite>	_composite_type;
	protected ArrayList<EvoPool>				_sources			= new ArrayList<EvoPool>();



	@ConfigConstructor()
	public CompositeConfig(Class<? extends Composite> cl) {
		_composite_type = cl;
	}



	public Class<? extends Composite> compositeType() {
		return _composite_type;
	}



	public void addSource(EvoPool pool) {
		_sources.add(pool);
	}



	public EvoPool[] getSources() {
		return (EvoPool[]) _sources.toArray();
	}



	public int numSources() {
		return _sources.size();
	}



	@Override
	public void validate() throws BadConfiguration {
		if (_composite_type == null)
			throw new BadConfiguration("CompositeConfig: composite type is null.");
	}
}
