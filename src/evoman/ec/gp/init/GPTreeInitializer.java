package evoman.ec.gp.init;


import evoman.ec.*;
import evoman.ec.gp.*;
import evoman.evo.structs.*;



/**
 * GPTreeInitializer constructs a new (sub)tree.
 * 
 * @author ruppmatt
 * 
 */
public abstract class GPTreeInitializer extends RepresentationInitializer {

	private static final long	serialVersionUID	= 1L;



	/**
	 * Create a new initializer
	 * 
	 * @param state
	 *            EMState for random number generation
	 * @param conf
	 *            Initializer configuration
	 */
	public GPTreeInitializer() {
		super();
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
