package evoman.ec.gp;


import evoict.*;



/**
 * GPNode configuration contains information about a particular type of node and
 * its constraints. It is used to construct and configure nodes in GPTrees at
 * run-time.
 * 
 * @author ruppmatt
 * 
 */
public class GPNodeConfig extends KeyValueStore {

	private static final long			serialVersionUID	= 1L;
	protected GPNodeConstraints			_constraints;				// Node
																	// constraints
	protected Class<? extends GPNode>	_class;					// Node type



	/**
	 * Construct a new GPNode configuration from a GPNode class
	 * 
	 * @param cl
	 */
	public GPNodeConfig(Class<? extends GPNode> cl) {
		super();
		_class = cl;
		_constraints = GPNodeConstraints.scan(cl);
	}



	/**
	 * Get the class of the GPNode
	 * 
	 * @return
	 */
	public Class<? extends GPNode> getNodeClass() {
		return _class;
	}



	/**
	 * Get the constraints for the GPNode
	 * 
	 * @return
	 */
	public GPNodeConstraints getConstraints() {
		return _constraints;
	}

}
