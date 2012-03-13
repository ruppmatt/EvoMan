package evoman.ec.composite;


import evoict.*;
import evoman.ec.composite.init.*;
import evoman.evo.pop.*;
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
	 * @param state
	 *            EMState for random numbers, notification
	 * @param config
	 *            Configuration to use in representation
	 * @param init
	 *            Initializer to build representation
	 */
	public AutoFixedComposite(EMState state, CompositeConfig config, CompositeInitializer init) {
		super(state, config);
		build(init);

	}



	protected void build(CompositeInitializer init) {
		int n = getConfig().numSources();
		_components = new Representation[getConfig().numSources()];
		for (int k = 0; k < n; k++) {
			EvoPool source = getConfig().getSources()[k];
			try {
				_components[k] = init.retrieve(this, source, k, _state);
				_sources[k] = source;
			} catch (BadConfiguration bc) {
				getNotifier().fatal(
						"Unable to construct an AutoFixedComposite due to initialization failure.  Reason: "
								+ bc.getMessage());
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
	protected AutoFixedComposite(EMState state, CompositeConfig config) {
		super(state, config);
	}



	@Override
	public Object clone() {
		AutoFixedComposite new_clone = new AutoFixedComposite(_state, _conf);
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

}
