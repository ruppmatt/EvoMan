package evoman.ec.gp;


import evoman.evo.structs.*;



/**
 * A mutable node provides a mutation method. Keep in mind that constraint
 * information at the top of the node is also used to determine whether or not
 * the node is actually mutable.
 * 
 * @author ruppmatt
 * 
 */
public abstract class GPMutableNode extends GPNode {

	private static final long	serialVersionUID	= 1L;



	public GPMutableNode(GPNodeConfig conf, GPNode parent, GPNodePos pos) {
		super(conf, parent, pos);
	}



	/**
	 * Mutate the node
	 */
	public abstract void mutate(EMState state);

}
