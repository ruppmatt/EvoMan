package evoman.ec.composite;


import java.io.*;

import evoict.*;
import evoman.ec.*;
import evoman.ec.composite.init.*;
import evoman.evo.structs.*;



/**
 * An AutoFixedComposite representation creates a fixed-length composite with a
 * 1:1 mapping of source EvoPools to components.
 * 
 * @author ruppmatt
 * 
 */

public class AutoFixedComposite extends Composite {

	private static final long	serialVersionUID	= 1L;



	/**
	 * Validate the composite configuration. For this particular composite, data
	 * sources and length are set by the GPVariationManager and mapped 1:1
	 * between EvoPool sources and representation components.
	 * 
	 * @param config
	 */
	public static void validate(CompositeConfig config) {
		return;
	}



	/**
	 * Construct and initialize (build) FixedComposite reprsentation
	 * 
	 * @param config
	 *            Configuration to use in representation
	 * @param init
	 *            Initializer to build representation
	 */
	public AutoFixedComposite(EMState state, CompositeConfig config, CompositeInitializer init) throws BadConfiguration {
		super(config);
		init(state, init);
	}



	protected void init(EMState state, CompositeInitializer init) throws BadConfiguration {
		int n = getConfig().numSources();
		_components = new Representation[n];
		_sources = new EvoPool[n];
		for (int k = 0; k < n; k++) {
			EvoPool source = getConfig().getSources()[k];
			try {
				_components[k] = init.retrieve(this, source, k, state);
				_sources[k] = source;
			} catch (BadConfiguration bc) {
				bc.prepend("AutoFixedComposite: Unable to construct an AutoFixedComposite due to initialization failure.");
				throw bc;
			}
		}
	}



	/**
	 * Clone constructor
	 * 
	 * @param state
	 *            EMState to use
	 * @param config
	 *            Configuration of Composite
	 */
	protected AutoFixedComposite(CompositeConfig config) {
		super(config);
	}



	@Override
	public Object clone() {
		AutoFixedComposite new_clone = new AutoFixedComposite(_conf);
		new_clone._components = _components.clone();
		return new_clone;
	}



	@Override
	public void setComponent(Representation r, int ndx, EvoPool src) throws BadConfiguration {
		if (ndx < size()) {
			if (src == _sources[ndx]) {
				_components[ndx] = r;
			} else {
				throw new BadConfiguration("AutoFixedComposite: Representation is not from same EvoPool.");
			}
		} else {
			throw new BadConfiguration("AutoFixedComposite: Requested index out of bounds.");
		}
	}



	@Override
	public Object eval(Object o) throws BadEvaluation {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("{ ");
		for (int k = 0; k < size(); k++) {
			buf.append(_components[k].toString());
			if (k < size() - 1)
				buf.append(", ");
		}
		buf.append(" }");
		return buf.toString();
	}



	@Override
	public String toString(Object context) throws BadEvaluation {
		return null;
	}



	@Override
	public void serializeRepresentation(ObjectOutputStream out) throws IOException {
		// TODO Auto-generated method stub
	}

}
