package evoman.ec.gp.init;


import evoict.*;
import evoman.ec.gp.*;
import evoman.evo.structs.*;
import evoman.evo.vm.*;



/**
 * GPTreeInitializer constructs a new (sub)tree.
 * 
 * @author ruppmatt
 * 
 */
public abstract class GPTreeInitializer extends RepresentationInitializer {

	private static final long	serialVersionUID	= 1L;
	KeyValueStore				_kv;



	/**
	 * Create a new initializer
	 * 
	 * @param state
	 *            EMState for random number generation
	 * @param conf
	 *            Initializer configuration
	 */
	public GPTreeInitializer() {
		_kv = new KeyValueStore();
	}



	/**
	 * Check to see whether or not a terminal node should be created.
	 * Parent is allowed to be null
	 * 
	 * @param t
	 *            GPTree
	 * @param parent
	 *            Parent node requesting child
	 * @param cl
	 *            Expected return type of child
	 * @return
	 */
	public abstract boolean createTerminal(GPTree t, GPNode parent, Class<?> cl, EMState state);

}
